#!/bin/bash

OUT_DIR=/root/ag-deadlock
PID=28362


while true
do
    now=$(date +"%H%M%S-%d%m%Y")
    OUTFILE=$OUT_DIR/threads-$now.dmp
    /opt/jdk1.8.0_92/bin/jstack -F $PID > $OUTFILE

    grep -i "No deadlocks found." $OUTFILE

    if [ $? == 0 ];
    then
        echo "found the no deadlocks string"
        #rm -fv $OUTFILE
    else 
        echo "DID NOT find the no deadlocks string - quitting, outfile : $OUTFILE"
        exit 0
    fi

done

