#!/bin/bash

declare -a productType=(mrb lb)
declare -a expectedVMs=(vm1 vm2 vm71 vm72 vm73 bamboo1 bamboo2)


###
# set up some directory vars
#
lbInstallDir="/opt/nst-loadbalancer"
mrbInstallDir="/opt/mrb"
logArchiverName="bin/logArchiver.sh"
logArchiveDir="archived-logs"
#
#

usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : gl <product-type> <remote-machine-to-copy-to>"
    echo "        product-type   : ${productType[@]}"
    echo "        remote machine : ${expectedVMs[@]}"
    echo " "
    exit -1
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

moveToBaseLogArea() {
   source /home/agrahame/scripts/sandbox.sh today > /dev/null
   logDir=$PWD/logs

   if [ -d "$logDir" ]; then
     # log dir already exists, rename with timestamp
     oldLogDir=$PWD/logs_$(date +_%T)
     mv $logDir $oldLogDir
   fi

   mkdir $logDir
   cd $logDir
}

##########################
#########   START
##

if [ $# -lt 2 ]
  then
     usage "Unexpected number of args"
fi

/home/agrahame/scripts/t1.sh

checkArg "Product Type" productType[@] $1
checkArg "Remote Machine" expectedVMs[@] $2
if [ $# -eq 3 ]
then
   checkArg "Remote Machine (2)" expectedVMs[@] $3
   remoteMachine2=$3
fi

#if we get here the given args are correct, now lets give them more meaningful names
productType=$1
remoteMachine=$2

# move to sandbox area to use as a base for the log file retrieval
moveToBaseLogArea


# now do the work
case "$productType" in

   lb)  echo "Fetching LB logs ..."
         logArchiverScript=$lbInstallDir/$logArchiverName
         logArchiveDir=$lbInstallDir/$logArchiveDir
         toDir=$logDir/lb1
         mkdir $toDir
         ssh $remoteMachine "source $logArchiverScript > /dev/null; echo; echo"
         #
         # cant use the $logArchiveDir var below, as it wont be populated on the remote machine - need a hardcoded path
         laf=`ssh $remoteMachine 'ls -t /opt/nst-loadbalancer/archived-logs | head -1'`
         scp $remoteMachine:$logArchiveDir/$laf $toDir
         
         # move down to lb1 log dir
         cd $toDir

         # unbundle the archive
         tar xzvf $laf > /dev/null

         ## unbundle the LD bundle
         tar xzvf ld.tgz > /dev/null

         ## unbundle the Jetty bundle
         tar xvf JETTY_logs.tar > /dev/null

         cd -

         # did user provide a second remote machine to copy to ?
         if [ -n "$remoteMachine2" ]
         then
            toDir=$logDir/lb2
            mkdir $toDir
            ssh $remoteMachine2 "source $logArchiverScript > /dev/null; echo; echo"
            #
            # cant use the $logArchiveDir var below, as it wont be populated on the remote machine - need a hardcoded path
            laf=`ssh $remoteMachine2 'ls -t /opt/nst-loadbalancer/archived-logs | head -1'`
            #echo "latest archive : $laf"
            scp $remoteMachine2:$logArchiveDir/$laf $toDir
         
            cd $toDir

            # unbundle the archive
            tar xzvf $laf > /dev/null

            ## unbundle the LD bundle
            tar xzvf ld.tgz > /dev/null

            cd -
         fi

         ;;

   mrb)  echo "Fetching MRB logs ..."
         ;;


   *) echo "Unexpected product type : $productType"
      ;;
esac

/home/agrahame/scripts/t2.sh

echo " .... logs have been copied to : $logDir"
echo
echo
echo done
echo
echo
echo " "; echo " "
