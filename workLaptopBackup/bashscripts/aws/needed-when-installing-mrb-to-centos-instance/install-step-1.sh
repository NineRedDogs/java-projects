#!/bin/bash
sudo setenforce 0
sudo sed -i --follow-symlinks 's/^SELINUX=.*/SELINUX=disabled/g' /etc/sysconfig/selinux
sudo yum -y update
echo "Collecting base install package list.."
sudo yum list installed | sed '0,/^Installed Packages$/d' | xargs -n3 | awk '{printf %s\\t%s\\t%s\\n, $1, $2, $3}' | sort > /tmp/packages_before.txt
echo "Installing nodejs.."
sudo yum -y install epel-release
#****Temp workaround for http-parser being removed from epel for RHEL 7.4 relese****
#****Can be removed when CentOS 7.4 is released*****
sudo yum -y install https://kojipkgs.fedoraproject.org//packages/http-parser/2.7.1/3.el7/x86_64/http-parser-2.7.1-3.el7.x86_64.rpm
#***************************************************
sudo yum -y install nodejs bzip2
sudo sed -i 's/enabled=./enabled=0/g' /etc/yum.repos.d/epel.repo
sudo sed -i 's/enabled=./enabled=0/g' /etc/yum.repos.d/epel-testing.repo
#TODO - Install lsb as part of generic MRB install?
sudo yum -y install redhat-lsb
LAST_KERNEL=$(sudo rpm -q --last kernel | awk '{print substr($1, 8); exit}')
CURRENT_KERNEL=$(uname -r)
test $LAST_KERNEL = $CURRENT_KERNEL && { echo "No reboot required"; } || { echo "Reboot required due to kernel update"; sudo shutdown -r now; }


