#!/bin/bash

declare -a services=(lb mrb adaptor vipm mrbjetty lbjetty)

exitScript() {
   echo; echo
   exit 1
}

usage() {
    echo "dbg: add debug to NST services"
    echo 
    echo "$@"
    echo " "
    echo "Usage : dbg <service>"
    echo "        service     : ${services[@]}"
    echo " "
    exitScript
}

checkArg() {
   goodArg=false

    declare -a argAry1=("${!2}")
    #echo "${argAry1[@]}"

   for checkType in ${argAry1[@]}
   do
      #echo " testing : $checkType"
      if [[ $3 == $checkType* ]] 
      then
         goodArg=true
         break
      fi
   done

   if [ "$goodArg" = false ] ; then
       usage "Unexpected " $1 " provided [" $3 "] expected " ${argAry1[@]}
   fi
}

addDebug() {
   echo; echo 
   serviceFile=$1
   debugFile=$2
   javaOptions=$3

   if [ ! -f $serviceFile ]; then
      echo "ERROR, Service File ($serviceFile) not found, exitting ..."
      exitScript
   fi

   if [ ! -f $debugFile ]; then
      echo "ERROR, Debug File ($debugFile) not found, exitting ..."
      exitScript
   fi

   echo "(1) `ls -la $serviceFile`"
   ##
   # copied following approach from : http://stackoverflow.com/questions/11243102/using-sed-to-insert-file-content
   #
   sed -i "/^$javaOptions=\"/ {
         h
         r $debugFile
         g
         N
   }" "$serviceFile"
   #
   #
   echo "(2) `ls -la $serviceFile`"
   sed -i "s/^$javaOptions=\"/$javaOptions=\"\$DEBUG_OPTS /" $serviceFile
   echo "(3) `ls -la $serviceFile`"

   echo; echo
   grep "DEBUG_OPTS" $serviceFile
   echo; echo
}


############
# service names
serviceDir="/etc/init.d"
#
lbServiceName="nst-loadbalancer"
lbService="$serviceDir/$lbServiceName"
#
lbJettyService="/opt/nst-loadbalancer/jetty-distribution-8.1.10.v20130312/bin/jetty.sh"
mrbJettyService="/opt/mrb/jetty-distribution-8.1.10.v20130312/bin/jetty.sh"
#
mrbServiceName="mrb"
mrbService="$serviceDir/$mrbServiceName"
#
vipManagerServiceName="nst-vip-manager"
vipManagerService="$serviceDir/$vipManagerServiceName"
#
adaptorServiceName="adaptor"
adaptorService="$serviceDir/$adaptorServiceName"
##
#
############
# debug files
dbgDir="/root/nst-debug-files"
#
lbDbg="$dbgDir/bootstrap.dbg"
jettyDbg="$dbgDir/jetty.dbg"
mrbDbg="$dbgDir/mrb.dbg"
vipManagerDbg="$dbgDir/vip-manager.dbg"
adaptorDbg="$dbgDir/adaptor.dbg"
##
#
#



#########################
# start of script
echo; echo; 
echo "Adding debug to NST services ...."; echo

if [ $# -ne 1 ]
  then
     usage "Unexpected number of args"
fi

checkArg "Service" services[@] $1

#if we get here the given arg is correct, now lets give it a more meaningful name
service=$1

# now do the work
case "$service" in
   lb) echo "Adding debug options to LB Bootstrap service ..."
       addDebug $lbService $lbDbg "JAVA_ARGS"
       service nst-loadbalancer stop
       find /opt/nst-loadbalancer -maxdepth 1 -name "*.log*" | xargs rm -fv
       service nst-loadbalancer start
       ;;

   mrb) echo "Adding debug options to MRB service ..."
       addDebug $mrbService $mrbDbg "JAVA_ARGS"
       service mrb stop
       find /opt/mrb -maxdepth 1 -name "*.log*" | xargs rm -fv
       service mrb start
       ;;

   adaptor) echo "Adding debug options to Adaptor service ..."
       addDebug $adaptorService $adaptorDbg "JAVA_ARGS"
       service adaptor stop
       find /opt/adaptor -maxdepth 1 -name "*.log*" | xargs rm -fv
       service adaptor start
       ;;

   vipm) echo "Adding debug options to Vip Manager service ..."
       addDebug $vipManagerService $vipManagerDbg "JAVA_ARGS"
       service nst-vip-manager stop
       find /opt/ -name "*vip-manager*.log*" | xargs rm -fv
       service nst-vip-manager start
       ;;

   lbjetty) echo "Adding debug options to LB Jetty service ..."
       addDebug $lbJettyService $jettyDbg "JAVA_OPTIONS"
       service jetty stop
       find /opt/nst-loadbalancer/jetty-distribution-8.1.10.v20130312 -name "*.log*" | xargs rm -fv
       service jetty start
       ;;

   mrbjetty) echo "Adding debug options to MRB Jetty service ..."
       addDebug $mrbJettyService $jettyDbg "JAVA_OPTIONS"
       service jetty stop
       find /opt/mrb/jetty-distribution-8.1.10.v20130312 -name "*.log*" | xargs rm -fv
       service jetty start
       ;;

   *) echo "Unknown service: $service, exitting"
      exitScript
      ;;

esac

