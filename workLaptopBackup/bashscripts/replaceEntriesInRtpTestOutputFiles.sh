#!/bin/bash

usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : replaceEntriesInRtpTestOutputFiles <log-file-name>"
    echo " "
    exit -1
}

if [ $# -ne 1 ]
  then
     usage "Unexpected number of args"
fi

LOG_FILE=$1

sed -i "s/\"last packet\" => [0-9]*/\"last packet\" => xyz/" $LOG_FILE
sed -i "s/\"last signal\" => [0-9]*/\"last signal\" => xyz/"  $LOG_FILE
sed -i "s/\"created\" => [0-9]*/\"created\" => xyz/"  $LOG_FILE
sed -i "s/\"bytes\" => [0-9]*/\"bytes\" => xyz/"  $LOG_FILE
sed -i "s/\"packets\" => [0-9]*/\"packets\" => xyz/"  $LOG_FILE
sed -i "s/^201[0-9]\-[0-1][0-9]\-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9][0-9][0-9]/DATE_TIME/"  $LOG_FILE


cat $LOG_FILE


