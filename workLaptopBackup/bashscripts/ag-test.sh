#!/bin/bash

echo
echo "------------------"
echo "recycling LB/VIP-Manager and JETTY ..."
echo
echo
service jetty stop
service nst-vip-manager stop
service nst-loadbalancer stop
echo
echo
find /opt/nst-loadbalancer/ -name "*.log*" | xargs rm -fv
echo
service jetty start
service nst-vip-manager start
service nst-loadbalancer start
echo
echo
echo "... all done"
echo "------------------------ "
echo " " 

sleep 15

egrep -i Exception\|Vendor\|"JmxClientConnection\.set" /opt/nst-loadbalancer/p1/nst-lb.log |& tee -a /root/ag.out


