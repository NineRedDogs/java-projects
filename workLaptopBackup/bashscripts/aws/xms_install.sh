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
XMS_INSTALL_TAR=dialogic_xms_3.5.17956-0.c7.tgz
MRB_INSTALL_JAR=dialogic-mrb-installer-3.5.0-958a4b33.jar
XMS_ACTIVATE_LICENSE=activate-license
#@@@SCRIPT_MOD@@@

shopt -s nullglob # have globs expand to nothing when they don't match

step "Check Properties..."
try $TRYOPT eval MACHINE_IP=$(hostname -I | awk '{print $1}')
try $TRYOPT test -f "/tmp/$XMS_INSTALL_TAR"
try $TRYOPT eval XMS_INSTALL_BASE=$(basename -s .tgz $XMS_INSTALL_TAR)
try $TRYOPT test -f "/tmp/$MRB_INSTALL_JAR"
next || exit 1

step "Run yum updates and installs..."
try $TRYOPT yum -y install perl-core kernel-devel redhat-lsb gcc
next || exit 1

step "Install XMS..."
try $TRYOPT pushd /tmp
try $TRYOPT tar xzf $XMS_INSTALL_TAR
try $TRYOPT cd $XMS_INSTALL_BASE
try $TRYOPT ./xms_install.pl -y
try $TRYOPT popd
next || exit 1

step "Removing gcc post install..."
try $TRYOPT yum -y remove gcc
next || exit 1

step "Stop services and disable..."
try $TRYOPT stop_service nodecontroller
try $TRYOPT stop_service adaptor
try $TRYOPT stop_service httpd

try $TRYOPT disable_service nodecontroller
try $TRYOPT disable_service adaptor
try $TRYOPT disable_service httpd
next || exit 1

step "Update MS-Adaptor install..."
try $TRYOPT eval JAVA_EXE=$(echo /opt/dialogic/jre*/bin/java)
if [ -z "$JAVA_EXE" ]
then
	try $TRYOPT eval JAVA_EXE=$(echo /opt/dialogic/jdk*/bin/java)
fi
try $TRYOPT test -x "$JAVA_EXE"
try $TRYOPT cat > /tmp/msAdaptorInstall.properties <<_EOF_
ismrbinstall=false
javaExe=$JAVA_EXE
ipAddress=127.0.0.1
adaptorIndependentService=true
INSTALL_PATH=/opt/adaptor
_EOF_
try $TRYOPT $JAVA_EXE -jar /tmp/$MRB_INSTALL_JAR -options /tmp/msAdaptorInstall.properties

step "Waiting for MS-Adaptor to start..."
while [ ! -f /opt/adaptor/nst-ms-adaptor-config.xml ] ;
do
      sleep 2
      if [ $((startwaitcount++)) -gt "30" ]
          then
             "Failed to locate /opt/adaptor/ns-ms-adaptor.xml file"
             exit
      fi
done

try $TRYOPT stop_service adaptor
try $TRYOPT disable_service adaptor

next || exit 1

step "Update MS-Adaptor config..."
try $TRYOPT sed -i "s/${MACHINE_IP}/127.0.0.1/g" /opt/adaptor/nst-ms-adaptor-config.xml 
try $TRYOPT cat /opt/adaptor/nst-ms-adaptor-config.xml
try $TRYOPT sed -i "s/${MACHINE_IP}/127.0.0.1/g" /etc/sysconfig/adaptor.properties 
try $TRYOPT cat /etc/sysconfig/adaptor.properties
next || exit 1

step "Fix for license checkout with no DNS connectivity..."
try $TRYOPT printf "\n%s\n%s\n" "# Prevent FlexNet publisher Client IP resolution" "export FNP_IP_ENV=1" >> /etc/profile.d/ct_intel.sh 
next || exit 1

# Intermittent behaviour - commented out for now
#step "Modify Nodecontroller scripts..."
#	if [[ "$PACKER_BUILDER_TYPE" == "amazon-ebs" ]]
#	then
#		     try $TRYOPT echo "Modifying mrcp client stack limit for AWS environment"
#		     if [ -r /etc/xms/nodecontroller/scripts/start-mrcpclient.sh ]"
#		     then
#			 try $TRYOPT mv /etc/xms/nodecontroller/scripts/start-mrcpclient.sh /etc/xms/nodecontroller/scripts/start-mrcpclient.sh.orig
#			 try $TRYOPT awk '
#			     /# Set the stack size for this process./ { in_cmd=1 }
#			     /^ulimit -s/ { if ($3 < 4112) { $3=4112; in_cmd=0 }}
#			     {print}
#			     ' /etc/xms/nodecontroller/scripts/start-mrcpclient.sh.orig >/etc/xms/nodecontroller/scripts/start-mrcpclient.sh
#			 try $TRYOPT chmod 555 /etc/xms/nodecontroller/scripts/start-mrcpclient.sh
#		     else
#			 try $TRYOPT echo "/etc/xms/nodecontroller/scripts/start-mrcpclient.sh not found"
#		     fi
#	fi
#next || exit 1


step "Clean up /etc/hosts file..."
try $TRYOPT sed -i 1d /etc/hosts
next || exit 1

step "Remove /etc/resolv.conf..."
try $TRYOPT rm -f /etc/resolv.conf
next || exit 1

step "Tidy up log/config files..."
try $TRYOPT rm -f /opt/adaptor/*.out /opt/adaptor/*.log
try $TRYOPT rm -rf cloud-init* dialogic/* messages httpd/* lighttpd/*
try $TRYOPT rm -f /usr/dialogic/cfg/mitconfig/ipms_rtp_addr_cfg.xml
next || exit 1 

step "Zero disk to allow compression..."
# No try on the dd, we are expecting it to fail when we run out of disk!
# This seams to work better than just using virt-sparsify
dd if=/dev/zero of=/filldisk
try $TRYOPT rm -f /filldisk
next || exit 1

exit 0
