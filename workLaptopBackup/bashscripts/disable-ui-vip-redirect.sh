#!/bin/bash

echo
echo Disabling UI VIP redirect ....
echo
echo

sed -i s/false/true/g /opt/mrb/jetty-distribution-8.1.10.v20130312/nst-mrb-web-admin-config.xml

echo; echo "======================================"
grep true /opt/mrb/jetty-distribution-8.1.10.v20130312/nst-mrb-web-admin-config.xml
echo "======================================"; echo

systemctl restart jetty

echo now updating on mrb : 192.168.2.145 .....


ssh root@192.168.2.145 "sh /root/andy/disable-ui-vip-redirect.sh"


echo
echo Done
echo

