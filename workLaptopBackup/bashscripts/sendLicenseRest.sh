#!/bin/bash
#
##################################################################
#
#  Script to simplify sending of REST messages to
#  the License Server
#
#
###########################

LB_ADDR="https://192.168.188.251:8443"
LIC_ADDR="https://192.168.246.50:40443"
#ENGINE_NAME="lb_eng_`hostname -I`" 
ENGINE_NAME="lb_eng_01" 
LICENSE_ID="lb_lic_77"
FEATURE_NAME="numsipservices"

declare -a targetTypes=(lb ld)
declare -a operations=(creng crlic crfeat getlic retlic co ci end)

##################################################################
#
# functions
#
usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : sendLbRest <target> <operation>"
    echo "        target     : ${targetTypes[@]}"
    echo "        operation  : ${operations[@]}"
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


##########################
#########
##

if [ $# -ne 2 ]
  then
     usage "Unexpected number of args"
fi

checkArg "Target" targetTypes[@] $1
checkArg "Operation" operations[@] $2

#if we get here the given args are correct, now lets give them more meaningful names
target=$1
operation=$2

if [ $target = "lb" ]
then
    TARGET_IP=$LB_ADDR
elif [ $target = "ld" ]; then
    TARGET_IP=$LIC_ADDR
else 
    echo "shouldnt get here after the checkArgs call"
    exit -1
fi

CURL_PREFIX="curl -v -k -X"
LIC_API_URL_PREFIX="api/v1/client/test/engine/"

# now do the work
case "$operation" in
    creng)  echo "Creating license engine ..."
            # e.g. curl -k -X POST 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001?engine-type=internal&product-version=3.2&product-id=100'
            HTTP_MSG="POST"
            OPERATION_URL="$ENGINE_NAME?engine-type=internal&product-version=3.2&product-id=100"
            ;;

    crlic)  echo "Creating license ..."
            # e.g. curl -k -X  POST 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001/license/license10?expiration-date=permanent'
            HTTP_MSG="POST"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID?expiration-date=permanent"
            ;;

    crfeat) echo "Creating feature ..."
            # e.g. curl -k -X  POST 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001/license/license10/feature/authorization?type=pool&count-max=100'
            HTTP_MSG="POST"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID/feature/$FEATURE_NAME?type=pool&count-max=100"
            ;;

    getlic) echo "Fetching license ..."
            # e.g. curl -k -X  GET 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001/license/license10'
            HTTP_MSG="GET"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID"
            ;;

    co)     echo "Checking out license feature ..."
            # e.g. curl -k -X  PUT 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001/license/license10/feature/authorization?count-inuse-local=1' 
            HTTP_MSG="PUT"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID/feature/$FEATURE_NAME?count-inuse-local=1"
            ;;

    ci)     echo "Checking in license feature ..."
            # e.g. curl -k -X  PUT 'https://192.168.246.50:40443/api/v1/client/test/engine/lb-001/license/license10/feature/authorization?count-inuse-local=0' 
            HTTP_MSG="PUT"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID/feature/$FEATURE_NAME?count-inuse-local=0"
            ;;

    retlic) echo "Returning license ..."
            # e.g. curl -k -X  DELETE 'https://192.168.246.50:40443/api/v1/client/test/engine/dlgc.2/license/license10'
            HTTP_MSG="DELETE"
            OPERATION_URL="$ENGINE_NAME/license/$LICENSE_ID"
            ;;

    end)    echo "End of license engine ..."
            # e.g. curl -k -X  DELETE 'https://192.168.246.50:40443/api/v1/client/test/engine' 
            HTTP_MSG="DELETE"
            OPERATION_URL=""
            ;;

    *)      echo "Unexpected operation, should have been caught by checkArg ..."
            ;;
esac

echo " "
FULL_CURL_COMMAND="$CURL_PREFIX $HTTP_MSG $TARGET_IP/$LIC_API_URL_PREFIX$OPERATION_URL"
echo " curl command : [$FULL_CURL_COMMAND]"

exec $FULL_CURL_COMMAND

echo " "; echo " "
