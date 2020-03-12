#!/bin/bash
# Get current swap usage for all running processes
# Erik Ljungstrom 27/05/2011

while [ "true" ]
do

   echo ==============================
   date
   echo - - - - - - - - - - - -
   echo "processes using swap right now : "

   SUM=0
   OVERALL=0
   for DIR in `find /proc/ -maxdepth 1 -type d | egrep "^/proc/[0-9]"` ; do
   PID=`echo $DIR | cut -d / -f 3`
   PROGNAME=`ps -p $PID -o comm --no-headers`
   for SWAP in `grep Swap $DIR/smaps 2>/dev/null| awk '{ print $2 }'`
   do
   let SUM=$SUM+$SWAP
   done

   if ((SUM > 0)); then
      echo "PID=$PID - Swap used: $SUM - ($PROGNAME )"
   fi

   let OVERALL=$OVERALL+$SUM
   SUM=0

   done
   echo - - - - - - - - - - - -
   echo "Overall swap used: $OVERALL"
   echo - - - - - - - - - - - -
   echo "memory info (in MB)"
   free -m
   echo - - - - - - - - - - - -
   echo "process info: all: `ps -ef | wc | awk '{print $1}'` : postgres : `ps -ef | grep ^postgres | wc | awk '{print $1}'` : root : `ps -ef | grep ^root | wc | awk '{print $1}'`"
   echo - - - - - - - - - - - -
   echo open files info....
   sysctl fs.file-nr
   echo "Total LSOF: `lsof | wc | awk '{print $1}'`"
   echo Loadbalancer processes 
   LBPROCS=`ls -la /proc/ | grep -w loadbalancer  | awk '{print $9}'`
   for lbproc in $LBPROCS
   do
      echo "    LB pid : $lbproc - lsof : `lsof | grep -w $lbproc | wc | awk '{print $1}'`"
   done
   echo - - - - - - - - - - - -


   sleep 10
done


