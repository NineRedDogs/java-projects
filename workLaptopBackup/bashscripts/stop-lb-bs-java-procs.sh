#!/bin/bash

JPROCS="$(ps -ef | grep java | grep "jar nst-bootstrap.jar" | awk '{print $2}')"
echo "$JPROCS"
for javaProc in $JPROCS
do
   echo; echo; echo;
   echo Killing java proc : $javaProc ....
   
   kill -9 $javaProc
   echo ---------------------
   echo ---------------------

done

echo
echo
echo " "
echo "------------------------------------------ "
echo
