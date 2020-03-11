#! /bin/bash

echo
echo Blocking all ports apart from SSH ......
echo

## Set default policy to DROP
iptables -P INPUT DROP
iptables -P FORWARD DROP
iptables -P OUTPUT ACCEPT

## Accept all SSH from local network
iptables -I INPUT -i eth0 -p tcp -s 192.168.2.0/24 --dport 22 -m state --state NEW,ESTABLISHED,RELATED -j ACCEPT
#iptables -I OUTPUT -o eth0 -p tcp --sport 22 -m state --state ESTABLISHED -j ACCEPT

# make sure nothing comes or goes out of this box
iptables -A INPUT -j DROP
#iptables -I OUTPUT -j ACCEPT

## Show the tables after the changes
./show.sh

echo
echo Blocked all incoming ports apart from SSH ......
echo
