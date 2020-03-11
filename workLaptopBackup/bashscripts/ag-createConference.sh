#!/bin/bash
#
###
# set up some static vars
#
sippCommand="/home/agrahame/Downloads/sipp/sipp-3.3/sipp"
mrbSipAddress="192.168.2.117:5070"
sippScriptNoPmrb="./ag_create_conf.xml"
sippScriptPmrb="./ag_create_conf_pmrb_prio.xml"

usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : ag-createConference.sh <test-num>"
    echo " "
    echo "#   deletewhen ?   p-mrb prio "
    echo "#-----------------------------"
    echo "# 1.  nomedia        "
    echo "#-----------------------------"
    echo "# 2.  nomedia             0  "
    echo "#-----------------------------"
    echo "# 3.  nomedia            22  "
    echo "#-----------------------------"
    echo "# 4.  nocontrol        "
    echo "#-----------------------------"
    echo "# 5.  nocontrol           0  "
    echo "#-----------------------------"
    echo "# 6.  nocontrol          22  "
    echo "#-----------------------------"
    echo " "
    exit -1
}

function cont {
    echo " "
    echo $1
    select yn in "Yes" "No"; do
        case $yn in
            Yes ) echo "continuing ..."; echo; break;;
            No ) echo "quitting ..."; echo; exit;;
        esac
    done
}

runTest() {
   sippScript=$1
   deleteOpt=$2
   if [ $# -eq 3 ]
     then
        pmrbPriorityArgText="-set confPrio $3"
   fi
   echo "Sipp script : $sippScript"
   echo "delete option : $deleteOpt"
   echo "pmrbPriority  : $pmrbPriorityArgText"
   echo " CMD: $sippCommand $mrbSipAddress -sf $sippScript -t t1 -m 1 -p 5000 -set confid 1234 -set deleteOpt $deleteOpt $pmrbPriorityArgText"
   cont "Happy with the test params ?"
   $sippCommand $mrbSipAddress -sf $sippScript -t t1 -m 1 -p 5000 -set confid 1234 -set deleteOpt $deleteOpt $pmrbPriorityArgText
}

if [ $# -ne 1 ]
  then
     usage "Unexpected number of args"
fi

#if we get here the given args are correct, now lets give them more meaningful names
testNum=$1

# now do the work
case "$testNum" in
   1) echo "Executing test # 1"
      runTest $sippScriptNoPmrb nomedia
      ;;

   2) echo "Executing test # 2"
      runTest $sippScriptPmrb nomedia 0
      ;;

   3) echo "Executing test # 3"
      runTest $sippScriptPmrb nomedia 22
      ;;

   4) echo "Executing test # 4"
      runTest $sippScriptNoPmrb nocontrol
      ;;

   5) echo "Executing test # 5"
      runTest $sippScriptPmrb nocontrol 0
      ;;

   6) echo "Executing test # 6"
      runTest $sippScriptPmrb nocontrol 22
      ;;

   *) usage "Unexpected test num provided : $testNum"
      ;;
esac

