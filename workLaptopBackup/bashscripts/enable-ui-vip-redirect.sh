#!/bin/bash

echo
echo Enabling UI VIP redirect ....
echo
echo

sed -i s/true/false/g /opt/mrb/jetty-distribution-8.1.10.v20130312/nst-mrb-web-admin-config.xml

echo; echo "======================================"
grep false /opt/mrb/jetty-distribution-8.1.10.v20130312/nst-mrb-web-admin-config.xml
echo "======================================"; echo

systemctl restart jetty

echo now updating on mrb : 192.168.2.145 .....


ssh root@192.168.2.145 "sh /root/andy/enable-ui-vip-redirect.sh"


echo
echo Done
echo

