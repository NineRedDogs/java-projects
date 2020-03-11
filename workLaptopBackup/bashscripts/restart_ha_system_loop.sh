#!/bin/bash

SYSTEM_TYPE="lb"
INSTALL_DIR="/opt/nst-loadbalancer/"
FILE_TO_CHECK="$INSTALL_DIR/p1/nst-lb.log"
SEARCH_STRING="ConnectException"
## allow for up to 10 second delay before each node is restarted, plus another 50 seconds to allow each node to come up
NODE_STARTED_DELAY=60

#SYSTEM_TYPE="mrb"
#INSTALL_DIR="/opt/mrb/"
#FILE_TO_CHECK="$INSTALL_DIR/nst-mrb-config.xml"
#SEARCH_STRING="\"media-server id=\""
## allow for up to 10 second delay before each node is restarted, plus another 120 seconds to allow each mrb node to come up and connect to any CF adaptors required
#NODE_STARTED_DELAY=120

## --------------------------------------------
#HA_SYSTEM=false
HA_SYSTEM=true
## my local VM settings
#
NODE1=vm1
NODE2=vm2
VIP_ADDRESS_OF_SERVICE="http://192.168.188.251:8080/mrb/Login.jsf"
#
## --------------------------------------------
## bt nst macro settings
#
#NODE1=bt-nst-macro-lb1
#NODE2=bt-nst-macro-lb2
#VIP_ADDRESS_OF_SERVICE="http://10.221.4.201:8080/admin-gui/login.jsf"

ITERATION=0

#####################################################
# method to be called if we detect a connect exception has occurred during the restart
##
foundConnectException() {

   echo " found a $SEARCH_STRING on $1"
   echo " now trying to access the vip ...."

   wget -O/dev/null $VIP_ADDRESS_OF_SERVICE
   WGET_STATUS=$?
   echo "#############################################"
   echo "###          result of wget [ $WGET_STATUS ]         ###"
   echo "#############################################"
   if [ $WGET_STATUS != 0 ]; then
       date
       echo "Failed to hit VIP !!!!, exitting - now capturing logs ..."

       ssh $1 "sh $INSTALL_DIR/bin/logArchiver.sh"
       echo "$1 : archived logs ..."
       ssh $1 "ls -lart $INSTALL_DIR/archived-logs"
    
       echo "Captured logs ...now exiting script ...."
    
       exit -1
   else
       echo ""
       echo "Successfully hit VIP address .... trying again ....."
   fi
}
##
#################



###########################################
# main script body ....
##########

while true
do
   echo "_______________________________________________________"
   echo "     iteration $((ITERATION++))  ::  $(date)"


   ssh $NODE1 "sh /root/ag/restart-$SYSTEM_TYPE.sh" &
   ssh $NODE1 "sh /root/ag/do-iptables-stuff.sh" &
   
   if [ "$HA_SYSTEM" = true ] ; then
      ssh $NODE2 "sh /root/ag/restart-$SYSTEM_TYPE.sh" &
      ssh $NODE2 "sh /root/ag/do-iptables-stuff.sh" &
   fi

   # delay to allow system to start up
   sleep $NODE_STARTED_DELAY


   ######
   # now check how the Nodes have come up .....
   ##############

   #
   ssh $NODE1 "grep -i $SEARCH_STRING $FILE_TO_CHECK"
   NODE1_SEARCH_RESULT=$?
   echo
   
   if [ $NODE1_SEARCH_RESULT == 0 ];
   then
      foundConnectException $NODE1
   else 
      echo " Did *NOT* find a $SEARCH_STRING on $NODE1 ( $NODE1_SEARCH_RESULT ) ...."
   fi
   ##
   ####################


   if [ "$HA_SYSTEM" = true ] ; then

      ssh $NODE2 "grep -i $SEARCH_STRING $FILE_TO_CHECK"
      NODE2_SEARCH_RESULT=$?
      echo
      
      if [ $NODE2_SEARCH_RESULT == 0 ];
      then
         foundConnectException $NODE2
      else 
         echo " Did *NOT* find a $SEARCH_STRING on $NODE2 ( $NODE2_SEARCH_RESULT ) ...."
      fi
      ##
      ####################
   fi

done



