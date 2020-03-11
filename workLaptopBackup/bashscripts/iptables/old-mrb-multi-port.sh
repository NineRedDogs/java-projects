#! /bin/bash

##################
##
##    TCP
##
## Accept all MRB TCP ports (e.g. UI, SIP, Rest, JMX)
iptables -I INPUT -i eth0 -p tcp -m multiport --dports 1081,5060,5070,5100,8000,8181,8888,8989 -m state --state NEW,ESTABLISHED -j ACCEPT
#iptables -I OUTPUT -o eth0 -p tcp -m multiport --sports 1081,5060,5070,5100,8000,8181,8888,8989 -m state --state ESTABLISHED -j ACCEPT
##
## Assume outbound traffic is allowed
##
##
##################


##################
##
##    UDP
##
## Accept all MRB UDP ports (e.g. SIP)
iptables -I INPUT -i eth0 -p udp --dport 5070 -m state --state NEW,ESTABLISHED -j ACCEPT
#iptables -I OUTPUT -o eth0 -p udp --sport 5070 -m state --state ESTABLISHED -j ACCEPT
##
## Assume outbound traffic is allowed
##
##
##################

## Need to switch on access for loopback
iptables -I INPUT -i lo -j ACCEPT
#iptables -A OUTPUT -o lo -j ACCEPT

## Show the tables after the changes
./show.sh

echo 
echo Completed Set up of MRB rules !
echo
echo

