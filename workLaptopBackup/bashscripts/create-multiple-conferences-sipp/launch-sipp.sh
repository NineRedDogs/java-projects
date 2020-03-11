#!/bin/bash

echo "creating a conference : id : $1"

while [ 1 ]
do
    /home/agrahame/Downloads/sipp/sipp-3.3/sipp 192.168.2.117:5070 -sf /home/agrahame/projects/mrb-27/msml_create_conf_via_input_param.xml -t t1 -m 1 -p 5000 -set confid $1
    echo " "
    sleep 30
done
