#!/bin/bash

echo
echo "------------------"
echo "recycling MRB ..."
echo
echo
service mrb stop
echo
echo
find /opt/mrb/ -name "*.log*" | xargs rm -fv
echo
service mrb start
echo
echo
echo "... all done"
echo "------------------------ "
echo " " 

sleep 30

egrep -i Exception\|Vendor\|"JmxClientConnection\.set" /opt/mrb/nst-mrb.log |& tee -a /root/ag/ag.out


