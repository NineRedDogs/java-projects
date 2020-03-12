#! /bin/bash

##################
##
iptables -I INPUT -i eth0 -p tcp --dport 1081 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 5060 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 5070 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p udp --dport 5070 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 5100 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 8000 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 8181 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 8888 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 8989 -m state --state NEW,ESTABLISHED -j ACCEPT

iptables -I INPUT -i eth0 -p tcp --sport 5070  -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p udp --sport 5070  -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 10000 -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 10001 -m state --state ESTABLISHED -j ACCEPT

iptables -I INPUT -i eth0 -p tcp --dport 54500 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 54500  -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 54501 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 54501  -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 54502 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 54502  -m state --state ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --dport 54503 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 54503  -m state --state ESTABLISHED -j ACCEPT

# eclipse remote debugging
iptables -I INPUT -i eth0 -p tcp --dport 8998 -m state --state NEW,ESTABLISHED -j ACCEPT
iptables -I INPUT -i eth0 -p tcp --sport 8998  -m state --state ESTABLISHED -j ACCEPT

## Need to switch on access for loopback
iptables -I INPUT -i lo -j ACCEPT

## Show the tables after the changes
./show.sh

echo 
echo Completed Set up of MRB rules !
echo
echo

