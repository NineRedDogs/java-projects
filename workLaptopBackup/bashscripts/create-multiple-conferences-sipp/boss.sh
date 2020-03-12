#!/bin/bash

for i in {1..2}
do
   xterm -e /home/agrahame/scripts/create-multiple-conferences-sipp/launch-sipp.sh $i &
done
