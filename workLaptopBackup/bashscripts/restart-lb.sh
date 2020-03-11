#!/bin/bash

echo
echo "------------------"
echo "recycling LB ..."
echo
echo
service nst-loadbalancer stop
echo
echo
find /opt/nst-loadbalancer/ -name "*.log*" | xargs rm -fv
echo
service nst-loadbalancer start
echo
echo
echo "... all done"
echo "------------------------ "
echo " " 

sleep 30

egrep -i Exception\|Vendor\|"JmxClientConnection\.set" /opt/nst-loadbalancer/p1/nst-lb.log |& tee -a /root/ag/ag.out


