#!/bin/bash

############################################################################################
#
# This script *MUST* be run on the MRB machine - where rtpengine has been installed
#
# 
#    pre-requisites :
#                         MRB has been installed to $MRB_INSTALL_DIR
#                         The RTP Engine tarball has been produced by executing the following command : 
#                           $ tar cvzf rtpengine.v4.tgz rtpengine.v4
#                           from the <local-git-repo>/rtpengine directory on your RTP Engine development machine
#                         The RTP Engine tarball (i.e. rtpengine.v4.tgz) has been copied to the MRB machine to a directory defined by $HOME_DIR
#
############################################################################################


##################################
## Constants
###############
##
HOME_DIR=/root/rtpengine_new
MRB_INSTALL_DIR=/opt/mrb

BACKUP_DIR=/tmp/rtpengine-files
RTPENGINE_TARBALL=$HOME_DIR/rtpengine.v4.tgz
SRC_DIR=$HOME_DIR/rtpengine.v4
DAEMON_SRC_DIR=$SRC_DIR/daemon
IPT_SRC_DIR=$SRC_DIR/iptables-extension
KERNEL_SRC_DIR=$SRC_DIR/kernel-module
RTPENGINE_DIR=/usr/bin/
RTPENGINE_EXE=rtpengine
V3_KERNEL_MODULE_PREFIX=xt_MEDIAPROXY
V3_KERNEL_MODULE_FILENAME=$V3_KERNEL_MODULE_PREFIX.ko
V4_KERNEL_MODULE_PREFIX=xt_RTPENGINE
V4_KERNEL_MODULE_FILENAME=$V4_KERNEL_MODULE_PREFIX.ko
BUILT_KERNEL_MODULE=$KERNEL_SRC_DIR/$V4_KERNEL_MODULE_FILENAME
KERNEL_REL="$(uname -r)"
KERNEL_DIR=/lib/modules/$KERNEL_REL/updates/
V3_KERNEL_MODULE=$KERNEL_DIR/$V3_KERNEL_MODULE_FILENAME
V4_KERNEL_MODULE=$KERNEL_DIR/$V4_KERNEL_MODULE_FILENAME
IPT_DIR=/lib64/xtables/
V3_IPT_NAME=libxt_MEDIAPROXY.so
V4_IPT_NAME=libxt_RTPENGINE.so
V3_IPT_MODULE=$IPT_DIR/$V3_IPT_NAME
V4_IPT_MODULE=$IPT_DIR/$V4_IPT_NAME
MRB_START_SCRIPT=/etc/init.d/mrb

MRB_SCRIPT_DIR=$MRB_INSTALL_DIR/scripts
RTPENGINE_START_SCRIPT=$MRB_SCRIPT_DIR/start_rtpengine
RTPENGINE_STOP_SCRIPT=$MRB_SCRIPT_DIR/stop_rtpengine
##
#############################


#####################################################
## copies a file to a tmp area - just in case things go wrong - this gives us a chance to manually recover
#######################
##
makeBackup() {
   fileToBackup=$1

   if [ -f $fileToBackup ] 
   then
      # echo "--- Backing up $fileToBackup ..."
      mv -fv $fileToBackup $BACKUP_DIR/
      echo; echo; echo
   fi
}

#####################################################
## checks if a file exists, program exits if it doesnt
#######################
##
checkFileExists() {
   fileToCheck=$1
   contextMsg=$2

   if [ ! -f $fileToCheck ] 
   then
      echo "ERROR: $contextMsg file not found -- $fileToCheck ..... Exiting !!!"
      exit 
   fi
}

#####################################################
## checks if a directory exists, program exits if it doesnt
#######################
##
checkDirExists() {
   dirToCheck=$1
   contextMsg=$2

   if [ ! -d $dirToCheck ] 
   then
      echo "ERROR: $contextMsg directory not found -- $dirToCheck ..... Exiting !!!"
      exit 
   fi
}

#####################################################
## checks that the expected directories and files are present
#######################
##
checkPreReqs() {
   checkDirExists $DAEMON_SRC_DIR "RTP Engine Daemon source"
   checkDirExists $IPT_SRC_DIR "RTP Engine IPTables Extension source"
   checkDirExists $KERNEL_SRC_DIR "RTP Engine Kernel Module source"
   checkDirExists $IPT_DIR "IPTables"
   checkDirExists $KERNEL_DIR "Kernel Modules"
   checkDirExists $RTPENGINE_DIR "RTPEngine exe"

   checkFileExists $MRB_START_SCRIPT "MRB Service start script"
   checkFileExists $RTPENGINE_START_SCRIPT "RTPEngine start script"
   checkFileExists $RTPENGINE_STOP_SCRIPT "RTPEngine stop script"
}
##
########################



#########################################################
##########   START
###

checkDirExists $MRB_INSTALL_DIR "MRB Installation"

if [ ! -d "$BACKUP_DIR" ]; then
  mkdir $BACKUP_DIR
fi

if [ -d "$SRC_DIR" ]; then
  # src dir already exists, delete contents before we continue
  rm -rf $SRC_DIR
fi

cd $HOME_DIR

#######
#
# (1) unbundle tarball
if [ ! -f $RTPENGINE_TARBALL ] 
then
  echo "RTP Engine tarball file not found, exiting : $RTPENGINE_TARBALL"
  exit 1
fi

tar xzf $RTPENGINE_TARBALL


#######
#
# (2) After unbundling RTP Engine source code - need to check all pre-requisites are in place
checkPreReqs

#######
#
# (3) Build components
#
#     (3a) Build Daemon
#
cd $DAEMON_SRC_DIR
make clean dep; make
if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine DAEMON, exiting ...."; echo
         exit 1;
      fi
#
#     (3b) Build IPTables extension
#
cd $IPT_SRC_DIR
make clean; make
if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine iptables-extension, exiting ...."; echo
         exit 1;
      fi
#
#     (3c) Build Kernel module
#
cd $KERNEL_SRC_DIR
make clean; make
if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine KERNEL module, exiting ...."; echo
         exit 1;
      fi
#
#
# if get here, rtpengine built successfully !!


#######
#
# (4) view original iptables config

iptables -L -v -n
echo; echo; echo;

#
# if v3 rtpengine running (and rtpProxyKernelEnabled=true set in /etc/sysconfig/mrb.properties) ....
#
#     Chain INPUT (policy ACCEPT 1278 packets, 1308K bytes)
#      pkts bytes target     prot opt in     out     source               destination         
#       116  9825 MEDIAPROXY  udp  --  *      *       0.0.0.0/0            0.0.0.0/0            MEDIAPROXY id:0
#     
#     Chain FORWARD (policy ACCEPT 0 packets, 0 bytes)
#      pkts bytes target     prot opt in     out     source               destination         
#     
#     Chain OUTPUT (policy ACCEPT 1083 packets, 145K bytes)
#      pkts bytes target     prot opt in     out     source               destination
#     
#
# if v4 rtpengine running (and rtpProxyKernelEnabled=true set in /etc/sysconfig/mrb.properties) ....
#
#     Chain INPUT (policy ACCEPT 1235 packets, 1933K bytes)
#      pkts bytes target     prot opt in     out     source               destination         
#       190 16032 RTPENGINE  udp  --  *      *       0.0.0.0/0            0.0.0.0/0            RTPENGINE id:0
#     
#     Chain FORWARD (policy ACCEPT 0 packets, 0 bytes)
#      pkts bytes target     prot opt in     out     source               destination         
#     
#     Chain OUTPUT (policy ACCEPT 852 packets, 95250 bytes)
#      pkts bytes target     prot opt in     out     source               destination         
#

#######
#
# (5) stop mrb in order to make rtpengine changes
service mrb stop
echo; echo; echo sleeping to allow iptables rules to be removed ....
sleep 10

#######
#
# (6) view iptables - should now have NO MEDIAPROXY/RTPENGINE INPUT entries

echo; echo IPTables rules should now be empty !!!!!; echo; echo
iptables -L -v -n
echo; echo; echo;

#######
#
# (7) update IPTables component

makeBackup $V3_IPT_MODULE
makeBackup $V4_IPT_MODULE

cp -v $IPT_SRC_DIR/$V4_IPT_NAME $IPT_DIR/
#ls -la $IPT_SRC_DIR/$V4_IPT_NAME $IPT_DIR/$V4_IPT_NAME* $IPT_DIR/$V3_IPT_NAME*
echo; echo; echo;

#######
#
# (8) update Kernel module
#
## IF v3 rtpengine module exists
if [ -a $V3_KERNEL_MODULE ] 
then
   ## Remove v3 module
   echo; echo "### Unloading v3 rtpengine kernel module ...."; echo; echo
   modprobe -r -v $V3_KERNEL_MODULE_PREFIX; 
   makeBackup $V3_KERNEL_MODULE
fi

## IF v4 rtpengine module exists
if [ -a $V4_KERNEL_MODULE ] 
then
   ## Remove v4 module
   echo; echo "### Unloading v4 rtpengine kernel module ...."; echo; echo
   modprobe -r -v $V4_KERNEL_MODULE_PREFIX; 
   makeBackup $V4_KERNEL_MODULE
fi

cp -v $BUILT_KERNEL_MODULE $KERNEL_DIR/
depmod -a; 
modprobe -v $V4_KERNEL_MODULE_PREFIX;
modprobe -l $V4_KERNEL_MODULE_PREFIX;
echo; echo;
#ls -la $BUILT_KERNEL_MODULE $V4_KERNEL_MODULE* $V3_KERNEL_MODULE*
echo; echo; echo;
#
#######

#######
#
# (9) update rtpengine executable 
#
makeBackup $RTPENGINE_DIR/$RTPENGINE_EXE
cp -v $DAEMON_SRC_DIR/$RTPENGINE_EXE $RTPENGINE_DIR/
#ls -la $DAEMON_SRC_DIR/$RTPENGINE_EXE $RTPENGINE_DIR/$RTPENGINE_EXE
echo; echo; echo;
#
#######

#######
#
# (10) update rtpengine strings in mrb scripts
#
echo; echo; echo "update kernel module name in mrb start script"
sed -i "s/$V3_KERNEL_MODULE_PREFIX/$V4_KERNEL_MODULE_PREFIX/g" $MRB_START_SCRIPT

echo; echo; echo "updating rtpengine scripts"
sed -i "s/mediaproxy/rtpengine/g" $RTPENGINE_START_SCRIPT
sed -i "s/MEDIAPROXY/RTPENGINE/g" $RTPENGINE_START_SCRIPT
sed -i "s/mediaproxy/rtpengine/g" $RTPENGINE_STOP_SCRIPT
sed -i "s/MEDIAPROXY/RTPENGINE/g" $RTPENGINE_STOP_SCRIPT
#
#######


#######
#
# (11) start mrb for new rtpengine changes to take affect
echo; echo; echo;
service mrb start
echo; echo; echo;
#
#######

# (12) delay to allow mrb to start up
sleep 30
echo; echo; echo;


# (13) show IP tables rules have been created and rtpengine process is running as expected
iptables -L -v -n; echo
echo; echo; echo;
ps -ef | grep listen-ng; echo; 
echo; echo; echo;
find /proc/mediaproxy/ -ls; echo
find /proc/rtpengine/ -ls; echo
echo; echo; echo;

# done

