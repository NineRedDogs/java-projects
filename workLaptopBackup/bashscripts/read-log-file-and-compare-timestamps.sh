#!/bin/bash
filename="$1"
prevTime=""
while read -r line
do

# typical log line :
# 2016-06-08 07:07:24,572 INFO main  source version Un-controlled build

    date=`echo $line | awk '{print $1}'`
    time=`echo $line | awk '{print $2}'`
    echo "date ($date) time ($time)"

# produces :
# date (2016-06-07) time (19:47:36,410)

    SEC1=`date +%s -d ${prevTime}`
    SEC2=`date +%s -d ${time}`

    # Use expr to do the math, let's say TIME1 was the start and TIME2 was the finish
    DIFFSEC=`expr ${SEC2} - ${SEC1}`

    #echo Start ${prevTime}
    #echo Finish ${time}

    echo Took ${DIFFSEC} seconds.


    #echo "Name read from file - $name"

    prevTime=$time
done < "$filename"
