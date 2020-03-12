#!/bin/bash
# redirects incoming tcp connections to one of 2 destination addresses round robin between the addresses
# 192.168.2.171 is a vip alias - setup by running ifconfig eth0:0 192.168.2.171
# 192.168.2.4 is one destination address
# 192.168.2.1 is another destination address
# Uses port 80 for incoming port and the port on the 2 destination addresses
#
# Obtained from question here...
# https://bbs.archlinux.org/viewtopic.php?id=123653

ALIAS_ADDRESS=192.168.2.171

NODE_ADDRESS=192.168.2.7

# create ethernet alias
ifconfig eth0:0 $ALIAS_ADDRESS

# Clear any existing rules
iptables -t nat --flush

# allow IP forwarding
sysctl net.ipv4.ip_forward=1

# Change the source IP addresses on the way out - so the return messages (inc acks) work
iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE

# add a rule for each node to load balance to:-
iptables -t nat -A PREROUTING -p tcp --dport 80 -d $ALIAS_ADDRESS -j DNAT -m statistic --mode nth --every 4 --packet 0 --to-destination $NODE_ADDRESS:8004
iptables -t nat -A PREROUTING -p tcp --dport 80 -d $ALIAS_ADDRESS -j DNAT -m statistic --mode nth --every 3 --packet 0 --to-destination $NODE_ADDRESS:8003
iptables -t nat -A PREROUTING -p tcp --dport 80 -d $ALIAS_ADDRESS -j DNAT -m statistic --mode nth --every 2 --packet 0 --to-destination $NODE_ADDRESS:8002
iptables -t nat -A PREROUTING -p tcp --dport 80 -d $ALIAS_ADDRESS -j DNAT -m statistic --mode nth --every 1 --packet 0 --to-destination $NODE_ADDRESS:8001


# Run this script on a machine
# run a listener nc -l 8001 -k on machine $NODE_ADDRESS
# run anther listener nc -l 8002 -k on machine $NODE_ADDRESS
# run something to send a message to the VIP - printf "Hello\n\n"|  nc $ALIAS_ADDRESS 80
# should see the message Hello alternating between the 2 different nc listening instances

#printf "Hello\n\n"|  nc 192.168.2.171 80
