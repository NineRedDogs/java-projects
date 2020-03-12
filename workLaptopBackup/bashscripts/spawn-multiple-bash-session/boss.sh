#!/bin/bash

for i in {1..2}
do
   xterm -e /home/agrahame/scripts/spawn-multiple-bash-session/launch-sipp.sh $i &
done
