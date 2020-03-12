#!/bin/bash

MAX_STARTUP_DELAY=10
MAX_BLOCKING_DELAY=10
NODE_TO_BLOCK="192.168.188.176"
PORT_TO_BLOCK=8080
OUTFILE=/root/ag/ag-iptabs.out


echo >> $OUTFILE
echo "------------------" >> $OUTFILE
date >> $OUTFILE
echo "doing iptables stuff ..." >> $OUTFILE
echo >> $OUTFILE
echo >> $OUTFILE

#######
## random delay before adding blocking iptable rule to generate many different start up scenarios to reproduce errors
START_DELAY=$(( (RANDOM % $MAX_STARTUP_DELAY) + 1 ))
date >> $OUTFILE
echo "Waiting .... start delay : $START_DELAY secs" >> $OUTFILE
sleep $START_DELAY
date >> $OUTFILE


#######
## now add iptable blocking rule
iptables -A OUTPUT -p tcp -d $NODE_TO_BLOCK --dport $PORT_TO_BLOCK -j DROP >> $OUTFILE
echo "------------------" >> $OUTFILE
echo -e "Adding blocking rule : \n" >> $OUTFILE
iptables -L >> $OUTFILE
date >> $OUTFILE
echo -e "\n------------------" >> $OUTFILE

#######
## random delay before removing blocking iptable rule to generate many different start up scenarios to reproduce errors
START_DELAY=$(( (RANDOM % $MAX_BLOCKING_DELAY) + 1 ))
date >> $OUTFILE
echo "Waiting .... delay while blocking rule is in place : $START_DELAY secs" >> $OUTFILE
sleep $START_DELAY
date >> $OUTFILE

#######
## now remove iptable blocking rule
iptables -D OUTPUT -p tcp -d $NODE_TO_BLOCK --dport $PORT_TO_BLOCK -j DROP >> $OUTFILE
echo "------------------" >> $OUTFILE
echo -e "Adding blocking rule : \n" >> $OUTFILE
iptables -L >> $OUTFILE
date >> $OUTFILE
echo -e "\n------------------" >> $OUTFILE


#######
## all done .....
echo >> $OUTFILE
echo >> $OUTFILE
echo "... blocking rule removed, now free to send to node $NODE:$PORT" >> $OUTFILE
echo "------------------------ " >> $OUTFILE
echo " "  >> $OUTFILE

