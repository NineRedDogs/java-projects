#!/bin/bash
#
##################################################################
#
#  Script to simplify sending of REST messages to
#  the License Server via the LB (need to set up a HTTPS re-enc service)
#
#
###########################

##################################################################
#
# functions
#
usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : sendLbLicenseRest <operation>"
    echo " "
    exit -1
}

##########################
#########
##

if [ $# -ne 1 ]
  then
     usage "Unexpected number of args"
fi

operation=$1

/home/agrahame/scripts/sendLicenseRest.sh lb $operation

echo " "; echo " "

##
#
#
