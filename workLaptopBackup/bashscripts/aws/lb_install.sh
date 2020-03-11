#!/bin/bash

. /etc/init.d/functions

# Use step(), try(), and next() to perform a series of commands and print
# [  OK  ] or [FAILED] at the end. The step as a whole fails if any individual
# command fails.
#
# Example:
#     step "Remounting / and /boot as read-write:"
#     try mount -o remount,rw /
#     try mount -o remount,rw /boot
#     next

TEMP_FILE=$(mktemp -t step_XXXXXX)

step() {
    echo -n "$@"

    STEP_OK=0
    [[ -f ${TEMP_FILE} ]] && echo $STEP_OK > ${TEMP_FILE}
}

try() {

    # Check for `-b' argument to run command in the background.
    local BG=
    local QUIET=

    [[ $1 == -b ]] && { BG='&'; shift; }
    [[ $1 == -q ]] && { QUIET=">/dev/null 2>&1"; shift; }
    [[ $1 == -- ]] && {       shift; }

    [[ -f ${TEMP_FILE} ]] && STEP_OK=$(< ${TEMP_FILE})    
    [[ $STEP_OK -ne 0 ]] && { [[ -n $LOG_STEPS ]] && { echo "Skipping command due to eairlier failure:"; echo "$@"; }; return $STEP_OK; }

    # Run the command.
    eval "$(printf "%q " "$@")" "$QUIET" "$BG"
    #if [[ -z $BG ]]; then
    #    "$@"
    #else
    #    "$@" &
    #fi

    # Check if command failed and update $STEP_OK if so.
    local EXIT_CODE=$?

    if [[ $EXIT_CODE -ne 0 ]]; then
        STEP_OK=$EXIT_CODE
        [[ -f ${TEMP_FILE} ]] && echo $STEP_OK > ${TEMP_FILE}

        if [[ -n $LOG_STEPS ]]; then
    local FILE=$(readlink -m "${BASH_SOURCE[1]}")
            local LINE=${BASH_LINENO[0]}

            echo "$FILE: line $LINE: Command \`$*' failed with exit code $EXIT_CODE." >> "$LOG_STEPS"
        fi
    fi

    return $EXIT_CODE
}

next() {
    [[ -f ${TEMP_FILE} ]] && { STEP_OK=$(< ${TEMP_FILE}); rm -f ${TEMP_FILE}; }
    [[ $STEP_OK -eq 0 ]]  && echo_success || echo_failure
    echo

    return $STEP_OK
}
#!/bin/bash
#
# Common utility functions
function validate_ip {
	local status=0
	if [[ -z "$1" || "$1" =~ \/ ]] || ! ipcalc -sc "$1"
	then
		status=1
	fi
	return $status
}

function validate_cidr {
	local status=0
	if [[ -z "$1" || ! "$1" =~ \/ ]] || ! ipcalc -sc "$1"
	then
		status=1
	fi
	return $status
}

function get_interface {
	[ $# -ne 1 ] && return 1
	dev="$(/sbin/ip -f inet -o addr show |
		 awk -v ip_address="$1" \
			 '$4 ~ "^"ip_address"/[0-9]+$" { print $2 }')"
	printf "$dev"
	return $([ -n "$dev" ])
}

function start_service {
	[[ -z "$1" ]] && exit 1
	if [[ -L "/sbin/init" ]]
	then
		systemctl start "${1}.service"
	else
		service "$1" start
	fi
}

function stop_service {
	[[ -z "$1" ]] && exit 1
	if [[ -L "/sbin/init" ]]
	then
		systemctl stop "${1}.service"
	else
		service "$1" stop
	fi
}

function enable_service {
	[[ -z "$1" ]] && exit 1
	if [[ -L "/sbin/init" ]]
	then
		systemctl enable "${1}.service"
	else
		chkconfig "$1" on
	fi
}

function disable_service {
	[[ -z "$1" ]] && exit 1
	if [[ -L "/sbin/init" ]]
	then
		systemctl disable "${1}.service"
	else
		chkconfig "$1" off
	fi
}
TRYOPT=
LB_INSTALL_JAR=dialogic-lb-installer-1.6.0-45891cff.jar
JRE_INSTALL_TAR=server-jre-8u152-linux-x64.tar.gz
#@@@SCRIPT_MOD@@@

step "Check Properties..."
try $TRYOPT eval MACHINE_IP=$(hostname -I | awk '{print $1}')
try $TRYOPT test -f "/tmp/$LB_INSTALL_JAR"
try $TRYOPT test -f "/tmp/$JRE_INSTALL_TAR"
next || exit 1

step "Run yum updates and installs..."
try $TRYOPT yum -y install perl nmap tcpdump lsof
next || exit 1

step "Install JRE..."
try $TRYOPT pushd /opt/dialogic
try $TRYOPT tar xf /tmp/$JRE_INSTALL_TAR
try $TRYOPT eval JAVA_EXE=/opt/dialogic/$(tar tf /tmp/$JRE_INSTALL_TAR "*/jre/bin/java")
try $TRYOPT test -x "$JAVA_EXE"
try $TRYOPT popd
next || exit 1

step "Install LB..."
try cat > /tmp/lbInstall.properties <<_EOF_
javaExe=$JAVA_EXE
ipAddress=127.0.0.1
vipInterface=eth0
baseJGroupsMcastAddress=228.8.8.0
INSTALL_PATH=/opt/nst-loadbalancer
isNewJettyInstall=true
jettyLocation=/opt/nst-loadbalancer
clusterId=101
licenseMode=cloud
relayLocation=127.0.0.1
localLicenseServerLocation=127.0.0.1
_EOF_
try $TRYOPT $JAVA_EXE -jar /tmp/$LB_INSTALL_JAR -options /tmp/lbInstall.properties
next || exit 1

step "Stop services and disable..."
try $TRYOPT service nst-loadbalancer stop
try $TRYOPT service nst-vip-manager stop
try $TRYOPT service jetty stop

try $TRYOPT systemctl disable nst-loadbalancer.service
try $TRYOPT systemctl disable nst-vip-manager.service
try $TRYOPT systemctl disable jetty.service
next || exit 1

step "Tidy up log/config files..."
try $TRYOPT rm -rf /opt/nst-loadbalancer/*.out /opt/nst-loadbalancer/*.log /opt/nst-loadbalancer/jetty-distribution-*/logs/* /opt/nst-loadbalancer/jetty-distribution-*/tmp/*
try $TRYOPT rm -rf /opt/nst-loadbalancer/jetty-distribution-*.tar.gz rm -rf /opt/nst-loadbalancer/nst-bootstrap-scripts.tar.gz
#TODO Config reset...
try $TRYOPT rm -rf /opt/nst-loadbalancer/config/nst-bootstrap-config.json
next || exit 1 

step "Remove /etc/resolv.conf..."
try $TRYOPT rm -f /etc/resolv.conf
next || exit 1

step "Link additional vip-manager scripts..."
if [[ "$PACKER_BUILDER_TYPE" == "amazon-ebs" ]]
then
	try $TRYOPT echo "Linking AWS EC2 vip management scripts..."
	try $TRYOPT ln -s /opt/dialogic/vip-manager-scripts/ec2/* /opt/nst-loadbalancer/scripts/
fi
next || exit 1

step "Zero disk to allow compression..."
# No try on the dd, we are expecting it to fail when we run out of disk!
# This seams to work better than just using virt-sparsify
dd if=/dev/zero of=/filldisk
try $TRYOPT rm -f /filldisk
next || exit 1

exit 0
