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
MRB_INSTALL_JAR=dialogic-mrb-installer-3.5.0-958a4b33.jar
JRE_INSTALL_TAR=server-jre-8u131-linux-x64.tar.gz
#@@@SCRIPT_MOD@@@

INSTALL_DIR=/opt/mrb
CONF_DIR="${INSTALL_DIR}/config"
CONF_FILE="nst-mrb-config.json"

shopt -s nullglob # have globs expand to nothing when they don't match

step "Check Properties..."
try $TRYOPT eval MACHINE_IP="$(hostname -I | awk '{print $1}')"
try $TRYOPT test -f "/tmp/$MRB_INSTALL_JAR"
try $TRYOPT test -f "/tmp/$JRE_INSTALL_TAR"
next || exit 1

step "Run yum updates and installs..."
try $TRYOPT yum -y install perl tcpdump lsof 
# rtpProxy requirements
try $TRYOPT yum -y install glib2-devel glibc-devel glibc-headers zlib-devel openssl-devel pcre-devel libcurl-devel xmlrpc-c xmlrpc-c-devel iptables-devel gcc kernel-devel
if [[ ! -r /etc/yum.repos.d/epel.repo ]]
then
	try $TRYOPT sudo yum -y install epel-release
fi
try $TRYOPT sed -i 's/enabled=./enabled=1/g' /etc/yum.repos.d/epel.repo
try $TRYOPT yum -y install epel hiredis hiredis-devel
try $TRYOPT sed -i 's/enabled=./enabled=0/g' /etc/yum.repos.d/epel.repo
next || exit 1

step "Install JRE..."
try $TRYOPT pushd /opt/dialogic
try $TRYOPT tar xf "/tmp/$JRE_INSTALL_TAR"
try $TRYOPT eval JAVA_EXE="/opt/dialogic/$(tar tf /tmp/$JRE_INSTALL_TAR "*/jre/bin/java")"
try $TRYOPT test -x "$JAVA_EXE"
try $TRYOPT popd
next || exit 1

step "Install MRB..."
try cat > /tmp/mrbInstall.properties <<_EOF_
ismrbinstall=true
isMediaProxyEnabled=true
javaExe=$JAVA_EXE
ipAddress=127.0.0.1
vipInterface=eth0
INSTALL_PATH=$INSTALL_DIR
isNewJettyInstall=true
jettyLocation=$INSTALL_DIR
_EOF_
try $TRYOPT "$JAVA_EXE" -jar "/tmp/$MRB_INSTALL_JAR" -options /tmp/mrbInstall.properties
next || exit 1

step "Stop services and disable..."
try $TRYOPT stop_service mrb
try $TRYOPT stop_service nst-vip-manager
try $TRYOPT stop_service jetty

try $TRYOPT disable_service mrb
try $TRYOPT disable_service nst-vip-manager
try $TRYOPT disable_service jetty
next || exit 1

step "Tidy up log/config files..."
try $TRYOPT rm -rf "${INSTALL_DIR}"/*.out "${INSTALL_DIR}"/*.log "${INSTALL_DIR}"/logs/* "${INSTALL_DIR}"/jetty-distribution-*/logs/* "${INSTALL_DIR}"/jetty-distribution-*/tmp/*


sudo -u mrb mkdir -p "$CONF_DIR"
try sudo -u mrb cat > "${CONF_DIR}/${CONF_FILE}" <<_EOF_
{
    "description": "Cloud Base Configuration Set",
    "mrb-config": {
        "locations": {
            "locations": {
                "1": {
                    "id": "1",
                    "notes": "",
                    "name": "Default"
                }
            }
        },
        "network": {
            "traffic-vip": {
                "vip-address": "",
                "interface-name": "eth0"
            },
            "admin-ui-vip": {
                "vip-address": "",
                "interface-name": "eth0"
            },
            "vip-traffic-port": "5070"
        },
        "flags": {
            "external-loadbalancer-flag": true
        }
    }
}
_EOF_
next || exit 1 

step "Remove /etc/resolv.conf..."
try $TRYOPT rm -f /etc/resolv.conf
next || exit 1

step "Link additional vip-manager scripts..."
if [[ "$PACKER_BUILDER_TYPE" == "amazon-ebs" ]]
then
	try $TRYOPT echo "Linking AWS EC2 vip management scripts..."
	try $TRYOPT ln -s /opt/dialogic/vip-manager-scripts/ec2/* /opt/mrb/scripts/
fi
next || exit 1

step "Zero disk to allow compression..."
# No try on the dd, we are expecting it to fail when we run out of disk!
# This seams to work better than just using virt-sparsify
dd if=/dev/zero of=/filldisk
try $TRYOPT rm -f /filldisk
next || exit 1

exit 0
