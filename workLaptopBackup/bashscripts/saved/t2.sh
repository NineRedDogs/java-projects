#!/bin/bash

# use in conjunction with t1.sh

STARTTIME=$(cat /tmp/starttime)
ENDTIME=$(date +%s)
echo " "
echo " ------- "
echo " "
echo "Took $(($ENDTIME - $STARTTIME)) seconds to complete"
echo " "
echo " ------- "
echo " "
