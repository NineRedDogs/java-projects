#!/bin/bash

while [ "true" ]
do
   echo - - - - - - - - - - - -
   date
   echo
   wget -O/dev/null http://192.168.188.251:8080
   echo ==============================
   date
   echo - - - - - - - - - - - -
done


