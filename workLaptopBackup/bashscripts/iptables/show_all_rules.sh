#!/bin/bash

echo -e  "\n-------------------- IPTABLES rules -----------------\n"
iptables -L -v -n
echo -e  "\n-------------------- Nat Table ----------------------\n"
iptables -t nat -L -v -n
echo -e  "\n-------------------- Raw Table ----------------------\n"
iptables -t raw -L -v -n
echo -e  "\n-------------------- Filter Table -------------------\n"
iptables -t filter -L -v -n
echo -e  "\n-------------------- Mangle Table -------------------\n"
iptables -t mangle -L -v -n
echo -e  "\n-------------------- Security Table -----------------\n"
iptables -t security -L -v -n
echo -e  "\n-----------------------------------------------------\n"
