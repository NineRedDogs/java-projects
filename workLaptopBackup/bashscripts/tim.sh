#!/bin/bash

if [ -z "$1" ]
  then
    echo "Usage: tim <bash command(s)>"
    exit 1
fi

bashCommands=$1
STARTTIME=$(date +%s)
#command block that takes time to complete...
$bashCommands |& tee /tmp/tim.out
#........
ENDTIME=$(date +%s)
echo "Took $(($ENDTIME - $STARTTIME)) seconds to complete $1 ..."
