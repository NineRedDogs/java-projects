#! /bin/bash

# script to return the machine to an open no-rules box

# flush all rules
iptables -F
iptables -X

# set default policy to acceopt
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
iptables -P OUTPUT ACCEPT

# show rules
./show.sh
