#! /bin/bash

# script to list current rules

# show rules
echo
echo
iptables -L -v -n
echo
iptables -t nat -L -v -n
echo
echo

