#!/bin/bash

#
# script that adds extra iptables rules that will test the remove-stale-rules code - see LB-362 for background
#

### The IP addresses used in this script are influenced by the following set up of my LB cluster:

## Service VIP : 192.168.188.251
# Base IP of LB machine : 192.168.188.211
#
### Set up the following stateless http/generic services:
##
# 
#  h1 - HTTP (Stateless) - round robin
#     - VIP  : 192.168.188.251:8080
#     - nodes: 192.168.188.176:8001
#     -        192.168.188.176:8002
#  
#  h2 - Generic-TCP - round robin
#     - VIP  : 192.168.188.251:8080
#     - nodes: 192.168.188.176:8002
#     -        192.168.188.176:8003
#     -        192.168.188.176:8004
#  
#  h3 - HTTP (Stateless) - priority
#     - VIP  : 192.168.188.251:8080
#     - nodes: 192.168.188.176:8001
#     -        192.168.188.176:8002
#     -        192.168.188.176:8003
#     -        192.168.188.176:8004
#  
#  
#  #
#  # PREROUTING table 
#  #
# VIP (i.e. -d) not in any stateless service (valid IP on LB machine, but not in any service)
iptables -t nat -A PREROUTING -p tcp -d 192.168.188.211 --dport 8080 -j DNAT -m statistic --mode nth --packet 0 --every 2 --to-destination 192.168.188.176:8001

# VIP (i.e. -d) is good, but dport doesn't match a service vip address
iptables -t nat -A PREROUTING -p tcp -d 192.168.188.251 --dport 8282 -j DNAT -m statistic --mode nth --packet 0 --every 2 --to-destination 192.168.188.176:8001

# VIP (i.e. -d) is good, but endpoint node address is not a configured endpoint
iptables -t nat -A PREROUTING -p tcp -d 192.168.188.251 --dport 8080 -j DNAT -m statistic --mode nth --packet 0 --every 2 --to-destination 192.168.188.176:8811
iptables -t nat -A PREROUTING -p tcp -d 192.168.188.251 --dport 8080 -j DNAT -m statistic --mode nth --packet 0 --every 2 --to-destination 192.168.188.233:8001
iptables -t nat -A PREROUTING -p tcp -d 192.168.188.251 --dport 8080 -j DNAT -m statistic --mode nth --packet 0 --every 2 --to-destination 192.168.188.233:8812

#
# OUTPUT table 
#
# VIP (i.e. -d) not in any stateless service (valid IP on LB machine, but not in any service)
iptables -t nat -A OUTPUT -d 192.168.188.211/32 -p tcp -m tcp --dport 8080 -j DNAT --to-destination 192.168.188.176:8002

# VIP (i.e. -d) is good, but dport doesn't match a service vip address
iptables -t nat -A OUTPUT -d 192.168.188.251/32 -p tcp -m tcp --dport 8555 -j DNAT --to-destination 192.168.188.176:8002

# VIP (i.e. -d) is good, but endpoint node address is not a configured endpoint
iptables -t nat -A OUTPUT -d 192.168.188.251/32 -p tcp -m tcp --dport 8080 -j DNAT --to-destination 192.168.188.176:8822
iptables -t nat -A OUTPUT -d 192.168.188.251/32 -p tcp -m tcp --dport 8080 -j DNAT --to-destination 192.168.188.199:8002
iptables -t nat -A OUTPUT -d 192.168.188.251/32 -p tcp -m tcp --dport 8080 -j DNAT --to-destination 192.168.188.199:8823

#
# POSTROUTING table 
#
# the following rules set up masquerade rules for nodes from the final 3 entries added for the PRE-ROUTING rule - test the clean up of the POSTROUTING rule
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.176 --dport 8811 -j MASQUERADE
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.233 --dport 8001 -j MASQUERADE
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.233 --dport 8812 -j MASQUERADE

# the following rules set up masquerade rules for nodes from the final 3 entries added for the OUTPUT rule - test the clean up of the POSTROUTING rule
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.176 --dport 8822 -j MASQUERADE
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.199 --dport 8002 -j MASQUERADE
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.199 --dport 8823 -j MASQUERADE

# the following rules set up masquerade rules which do not match any service vips, so cannot be cleaned up - so will survice the clean up stale rules at restart
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.133 --dport 7777 -j MASQUERADE
iptables -t nat -A POSTROUTING -o eth0 -p tcp --destination 192.168.188.244 --dport 5555 -j MASQUERADE

