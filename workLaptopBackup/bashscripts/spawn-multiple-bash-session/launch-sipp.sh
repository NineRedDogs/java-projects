#!/bin/bash

echo "doing something with param : $1"

myHost="127.0.0.$1"

echo " my Host $myHost"

while [ 1 ]
do
    /home/agrahame/Downloads/sipp/sipp-3.3/sipp 127.0.0.35:5060 -i $myHost -sf /home/agrahame/Downloads/sipp/sipp-3.3/control_channel_call.xml  -t t1 -m 1 
    echo " "
    sleep 30
done
