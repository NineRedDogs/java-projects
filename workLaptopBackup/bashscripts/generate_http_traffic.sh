#!/bin/bash

ITERATION=0

###########################################
# main script body ....
##########

while true
do
   echo "_______________________________________________________"
   echo "     iteration $((ITERATION++))  ::  $(date)"

   ab -n 5000 -c 2000 http://192.168.188.251:8080/andy/z_tiny.txt

done



