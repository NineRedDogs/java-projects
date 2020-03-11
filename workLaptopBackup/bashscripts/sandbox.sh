#!/bin/bash
#

SANDBOX_BASE_DIR="/home/agrahame/sandbox"

declare -a sbDates=(today yest lwd)

usage() {
   echo
   echo ----------------------------------------------------
   echo
   echo "   sandbox [ today | yest | lwd ]"
   echo 
   echo "      where: yest  = yesterday"
   echo "             lwd   = last working day"
   echo
   echo ----------------------------------------------------
   echo
   exit
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


checkTodaysSandboxDir() {
   dirToCheck=$1
   contextMsg=$2

   if [ ! -d $dirToCheck ] 
   then
      echo "$contextMsg directory not found -- $dirToCheck ..... Creating ..."
      mkdir -p $dirToCheck
   fi

   if [ -d $dirToCheck ] 
   then
      sandboxDirExists=true
   else 
      sandboxDirExists=false
   fi
}

checkOldSandboxDir() {
   dirToCheck=$1
   contextMsg=$2

   if [ -d $dirToCheck ] 
   then
      sandboxDirExists=true
   else 
      echo "$contextMsg directory not found -- $dirToCheck ..... "
      sandboxDirExists=false
   fi
}


#
# assumes running only on a weekday
getLastWorkingDate() {
   day_of_week=`date +%w`
   if [ $day_of_week == 1 ] ; then
      # today is monday, so go back 3 days for friday
      look_back=3
   else
      # not monday, so yesterday was a week day, so go back 1 day
      look_back=1
   fi

   lwdDate=$(date -d "$look_back day ago" +'%b%d')
   lwdDateLowerCase=${lwdDate,,}
}

################
#
# 1. check if any params passed in
#
##
# set up default date : today
sbDate="today"
#
## 
# check args
if [ $# -gt 1 ]
  then
     usage "Unexpected number of args : $#"
fi
#
if [ $# -eq 1 ]
   then
      checkArg "Build Type" sbDates[@] $1
      sbDate=$1
   fi
#
##########
#echo "date used : $sbDate"

echo " "

case "$sbDate" in
   today) echo "Moving to todays sandbox dir ..."; echo
          monthAndDate=$(date +"%b%d")
          monthAndDateLowerCase=${monthAndDate,,}
          sandboxDir=$SANDBOX_BASE_DIR/$monthAndDateLowerCase
          checkTodaysSandboxDir $sandboxDir "Todays Sandbox directory"
          ;;
   yest)  echo "Moving to yesterdays sandbox dir ..."; echo
          yestMonthAndDate=$(date -d "yesterday" "+%b%d")
          yestMonthAndDateLowerCase=${yestMonthAndDate,,}
          sandboxDir=$SANDBOX_BASE_DIR/$yestMonthAndDateLowerCase
          checkOldSandboxDir $sandboxDir "Yesterdays Sandbox directory"
          ;;
   lwd)   echo "Moving to last-working-day sandbox dir ..."; echo
          getLastWorkingDate
          sandboxDir=$SANDBOX_BASE_DIR/$lwdDateLowerCase
          checkOldSandboxDir $sandboxDir "Last-working-day Sandbox directory"
          ;;
   *)     echo unknown arg
          ;;
esac

#
# 2. cd to sandbox

if $sandboxDirExists ; then
   cd $sandboxDir

   echo
   pwd
   echo

   ls -la

fi


echo; echo


