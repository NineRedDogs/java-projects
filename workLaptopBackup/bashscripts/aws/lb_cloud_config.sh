#!/bin/bash
#
#  Script to deal with tuning a Dialogic PowerVille LB node on first
#  boot in a cloud envirnment
#

. "$(dirname $0)/cloud_config_functions.sh"

function usage()
{
	printf "\n%s\n" "Standalone LB:"
	printf "\n%s\n" "Usage : TBD"
}

echo "-----------------------------------------------------------"
echo "Dialogic PowerVille LB Cloud Config Script"

TEMP=`getopt -o he:n: --long help,wait-time:,environment:,role: -n $(basename $0) -- "$@"`

if [ $? != 0 ] ; then usage >&2 ; exit 1 ; fi

eval set -- "$TEMP"

do_notify="false"
do_env_script="false"
notify_script=":"
ROLE="STANDALONE"
[ -z "${WAIT_TIME}" ] && WAIT_TIME=180

while true; do
	case "$1" in
		-h|--help) 		usage; exit 0 ;;
		-n|--notify-script)	do_notify="true"
					notify_script="$2"
					shift 2 ;;
		-e|--environment)	do_env_script="true"
					env_script=$2; shift 2 ;;
		--role)                 ROLE="${2^^}"
					shift 2 ;;
		--wait-time)	        WAIT_TIME=$2; shift 2 ;;
                --) 			shift ; break ;;
                *) 			fail "Internal error!" ; exit 1 ;;
        esac
done


process_environment

# ensure hostname and /etc/hostname are in sync
hostname "$(cat /etc/hostname)"

case "$ROLE" in
	STANDALONE) : ;;
	PRIMARY)    : ;;
	BACKUP)     : ;;
	*)	fail "Role must be set to one of  STANDALONE|PRIMARY|BACKUP (found ${ROLE})"
		exit 1 ;;
esac

if [ -z "${PRIVATE_ADDRESS}" ]
then
	# If there is only one interface on the machine we can use that
	if [[ $(hostname -I | wc -w) -eq 1 ]]
	then
		PRIVATE_ADDRESS=$(hostname -I | awk '{ print $1 }')
	else
		fail "Multiple interfaces found on instance and no private address specified"
		exit 2
	fi
fi
if [ -n "${PRIVATE_ADDRESS}" ]
then
	if validate_ip "${PRIVATE_ADDRESS}"
	then
		# Check that the PRIVATE_ADDRESS is bound to an interface...
		if ! private_if="$(get_interface "${PRIVATE_ADDRESS}")"
		then
			fail "Private address \"${PRIVATE_ADDRESS}\" is not bound to an interface"
			exit 2
		fi
	else
		fail "Private Address \"${PRIVATE_ADDRESS}\" is not a valid IP address"
		exit 1
	fi
fi

if [ -z "${IP_ADDRESS}" ]
then
	# If there is only one interface on the machine we can use that (We can also exlude the PRIVATE Interface)...
	if [[ $(hostname -I | wc -w) -eq 1 ]]
	then
		IP_ADDRESS=$(hostname -I | awk '{ print $1 }')
	elif [[ $(hostname -I | sed "s/${PRIVATE_ADDRESS}[[:space:]]*//" | wc -w) -eq 1 ]]
	then
		IP_ADDRESS=$(hostname -I | sed "s/${PRIVATE_ADDRESS}[[:space:]]*//" | awk '{ print $1 }')
	else
		fail "Multiple interfaces found on instance and no ip address specified"
		exit 2
	fi
fi
if [ -n "${IP_ADDRESS}" ]
then
	if validate_ip "${IP_ADDRESS}"
	then
		# Check that the IP is bound to an interface...
		if ! primary_if="$(get_interface "${IP_ADDRESS}")"
		then
			fail "IP address \"${IP_ADDRESS}\" is not bound to an interface"
			exit 2
		fi
	else
		fail "IP Address \"${IP_ADDRESS}\" is not a valid IP address"
		exit 1
	fi
fi

# TODO if The address isn't specified, we need to create some uniqueness between systems by default, probably in the 2nd or 3rd octect.
if [ -z "$JGROUP_MCAST_BASE_ADDRESS" ]
then
	JGROUP_MCAST_BASE_ADDRESS=228.8.8.0
fi
if [ -n "$JGROUP_MCAST_BASE_ADDRESS" ]
then
	if validate_ip "$JGROUP_MCAST_BASE_ADDRESS"
	then
		# TODO Specific MCAST range validation??
		:
	else
		fail "JGROUP_MCAST_BASE_ADDRESS \"${JGROUP_MCAST_BASE_ADDRESS}\" is not a valid MCAST IP address"
		exit 1
	fi
fi

if [ $# -ge 1 ]
then
	PRIMARY_ADDRESS=$1
	if ! validate_ip "${PRIMARY_ADDRESS}"
	then
		fail "Primary LB IP Address \"${PRIMARY_ADDRESS}\" is not a valid IP address"
		exit 1
	fi
fi

if [ $# -ge 2 ]
then
	PRIMARY_PRIVATE_ADDRESS=$2
	if ! validate_ip "${PRIMARY_PRIVATE_ADDRESS}"
	then
		fail "Primary LB Private IP Address \"${PRIMARY_PRIVATE_ADDRESS}\" is not a valid IP address"
		exit 1
	fi
fi

echo "-----------------------------------------------------------"
echo "lb_cloud_config using the following values"
echo "Role                                    = ${ROLE}"
echo "Management IP Address                   = ${IP_ADDRESS}"
echo "Management Interface                    = ${primary_if}"
echo "Private IP Address                      = ${PRIVATE_ADDRESS}"
echo "Private Interface                       = ${private_if}"
echo "JGROUP_MCAST_BASE_ADDRESS               = ${JGROUP_MCAST_BASE_ADDRESS}"
echo "Wait Time                               = ${WAIT_TIME}"
if [ "$do_env_script" == "true" ]
then
	echo "Environment Script                      = ${env_script}"
fi
if [ "$do_notify" == "true" ]
then
	echo "Performing Notifications via            = ${notify_script}"
	echo "Notification diagnostics:"
	echo "     " "$("$notify_script" -p)"
fi
echo "-----------------------------------------------------------"

echo "Adding " "$(hostname)" " to /etc/hosts"
cp /etc/hosts /etc/hosts.xms
echo "${IP_ADDRESS} " "$(hostname -s)" " " "$(hostname)" > /etc/hosts
cat /etc/hosts.xms >> /etc/hosts

echo "Updating VIP Manager properties file /etc/sysconfig/nst-vip-manager.properties"
if [ -f "/etc/sysconfig/nst-vip-manager.properties" ]
then 
    sed -i -e "s/jgroupsBindAddress=.*/jgroupsBindAddress=${PRIVATE_ADDRESS}/g" \
           -e "s/baseJGroupsMcastAddress=.*/baseJGroupsMcastAddress=${JGROUP_MCAST_BASE_ADDRESS}/g" \
           /etc/sysconfig/nst-vip-manager.properties
    cat /etc/sysconfig/nst-vip-manager.properties
else
    echo "/etc/sysconfig/nst-vip-manager.properties File NOT Found"
fi

echo "Updating LB properties file /etc/sysconfig/nst-loadbalancer.properties"
if [ -f "/etc/sysconfig/nst-loadbalancer.properties" ]
then
    sed -i -e "s/jmxHostname=.*/jmxHostname=${PRIVATE_ADDRESS}/g" \
           -e "s/baseJGroupsMcastAddress=.*/baseJGroupsMcastAddress=${JGROUP_MCAST_BASE_ADDRESS}/g" \
           /etc/sysconfig/nst-loadbalancer.properties
else
    echo "/etc/sysconfig/nst-loadbalancer.properties File NOT Found"
fi


do_wait="false"

INSTALL_DIR=/opt/nst-loadbalancer
CONF_DIR="${INSTALL_DIR}/config"
CONF_FILE="${CONF_DIR}/nst-bootstrap-config.json"
sudo -u loadbalancer mkdir -p "$CONF_DIR"

echo "Creating LB Config file ${CONF_FILE}"
if [[ "$ROLE" == "STANDALONE" ]]
then
	cat > "${CONF_FILE}" <<_EOF_
{
  "bootstrap": {
    "system-description": "Load Balancer Cloud Base Configuration Set",
    "hostname": "${PRIVATE_ADDRESS}",
    "load-balancer-addresses": {
      "jmx": {
        "host": "${PRIVATE_ADDRESS}",
        "port": 5101
      },
      "jmx-paired": {
        "host": "",
        "port": 5101
      }
    },
    "flags": {
      "log-all": false,
      "fail-as-stack": false,
      "ha-enabled": false,
      "master": false
    }
  }
}
_EOF_
elif [[ "$ROLE" == "PRIMARY" ]]
then
	cat > "$CONF_FILE" <<_EOF_
{
  "bootstrap": {
    "system-description": "Load Balancer Cloud Base Configuration Set",
    "hostname": "${PRIVATE_ADDRESS}",
    "load-balancer-addresses": {
      "jmx": {
        "host": "${PRIVATE_ADDRESS}",
        "port": 5101
      },
      "jmx-paired": {
        "host": "",
        "port": 5101
      }
    },
    "flags": {
      "log-all": false,
      "fail-as-stack": false,
      "ha-enabled": false,
      "master": false
    }
  }
}
_EOF_

	uuidgen > /tmp/cluster_id
	cluster_id="$(cat /tmp/cluster_id)"
	echo "clusterId reset to ${cluster_id} - Syncing to backup LB"
	sed -i -e "s/clusterId=.*/clusterId=${cluster_id}/g" /etc/sysconfig/nst-loadbalancer.properties
	# 
	# A work around is required to restart the Primary LB after the 
	# back up has started and shared initial config.  This also signals
	# the backup to restart it's bootstrap service once this is complete
	cat > /opt/dialogic/loadbalancer_pairing_monitor << _EOF_
#!/bin/bash
touch /opt/dialogic/loadbalancer_pairing_monitor.lck
trap "rm -f /opt/dialogic/loadbalancer_pairing_monitor.lck" EXIT

CONFIG_FILE="${CONF_FILE}"
### Set initial time of file
LTIME=\$(stat -c %Z \$CONFIG_FILE)

while true    
do
   ATIME=\$(stat -c %Z \$CONFIG_FILE)

   if [[ "\$ATIME" != "\$LTIME" ]]
   then    
       LTIME=\$ATIME
       paired_IP="\$(node <<__SCRIPT__
       		var fs = require('fs');
       		var myConfig = JSON.parse(fs.readFileSync('\$CONFIG_FILE', 'utf8'));
       		
       		console.log(myConfig["bootstrap"]["load-balancer-addresses"]["jmx-paired"]["host"]);
__SCRIPT__
		)"
       if [[ -n "\$paired_IP" ]]
       then
		ncat -s $PRIVATE_ADDRESS \$paired_IP 9496 < /tmp/cluster_id
		
		exit $?
       fi
   fi
   sleep 5
done
_EOF_

	chown loadbalancer:loadbalancer /opt/dialogic/loadbalancer_pairing_monitor
	chmod 755 /opt/dialogic/loadbalancer_pairing_monitor
	/opt/dialogic/loadbalancer_pairing_monitor &
	if [[ $? -ne 0 ]]
	then
		fail "Failed to run pairing monitor"
	fi
	do_wait="true"
elif [ "$ROLE" == "BACKUP" ] 
then
	CONF_FILE="${CONF_FILE}.import"
	echo "Creating LB Config import file ${CONF_FILE}"
	cat > "${CONF_FILE}" <<_EOF_
{
  "bootstrap": {
    "system-description": "Load Balancer Cloud Base Configuration Set",
    "hostname": "${PRIVATE_ADDRESS}",
    "load-balancer-addresses": {
      "jmx": {
        "host": "${PRIVATE_ADDRESS}",
        "port": 5101
      },
      "jmx-paired": {
        "host": "${PRIMARY_PRIVATE_ADDRESS}",
        "port": 5101
      }
    },
    "flags": {
      "log-all": false,
      "fail-as-stack": false,
      "ha-enabled": true,
      "master": true
    }
  }
}
_EOF_

	cat > /opt/dialogic/loadbalancer_pairing_monitor << _EOF_
#!/bin/bash
touch /opt/dialogic/loadbalancer_pairing_monitor.lck
trap "rm -f /opt/dialogic/loadbalancer_pairing_monitor.lck" EXIT
# Wait for the Primary LB to signal when it has our config and then restart.
ncat --allow $PRIMARY_PRIVATE_ADDRESS -l $PRIVATE_ADDRESS 9496 >/tmp/cluster_id
if [[ $? -eq 0 ]]
then
	sleep 5
	service nst-vip-manager stop
	service nst-loadbalancer stop
	sleep 5
	cluster_id="\$(cat /tmp/cluster_id)"
	echo "clusterId reset to \${cluster_id} - received from primary LB"
	sed -i -e "s/clusterId=.*/clusterId=\${cluster_id}/g" /etc/sysconfig/nst-loadbalancer.properties
	service nst-vip-manager start
	service nst-loadbalancer start
fi
_EOF_

	chown loadbalancer:loadbalancer /opt/dialogic/loadbalancer_pairing_monitor
	chmod 755 /opt/dialogic/loadbalancer_pairing_monitor
	/opt/dialogic/loadbalancer_pairing_monitor &
	if [[ $? -ne 0 ]]
	then
		fail "Failed to run pairing monitor"
	fi
	do_wait="true"
fi

chown loadbalancer:loadbalancer "$CONF_FILE"
cat "$CONF_FILE"

echo "Enabling services"
systemctl enable nst-vip-manager.service
systemctl enable nst-loadbalancer.service
systemctl enable jetty.service

systemctl start nst-vip-manager.service
echo "VIP Manager service started."
systemctl start nst-loadbalancer.service
echo "LoadBalancer service started."
systemctl start jetty.service
echo "Jetty sevice started."

# Wait here for the service restarts to have completed
echo "Waiting for Load Balancer Services..."
if [[ "$do_wait" == "true" ]]
then
	COUNTER=0
	while [ -f /opt/dialogic/loadbalancer_pairing_monitor.lck ]
	do
		sleep 5
		COUNTER=$(( COUNTER + 5 ))
		if [[ $COUNTER -ge $WAIT_TIME ]]
		then
			fail "Timed out waiting for LB Service Restart"
			exit 3
		fi
		echo "Still waiting for Load Balancer Services..."
	done
fi

$notify_script SUCCESS
