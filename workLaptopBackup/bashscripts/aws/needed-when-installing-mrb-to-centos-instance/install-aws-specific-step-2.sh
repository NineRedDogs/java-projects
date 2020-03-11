#!/bin/sh
#
# Install steps unique to the AWS environment

echo "Installing AWS CLI Tools"
if [[ ! -r /etc/yum.repos.d/epel.repo ]]
then
	sudo yum -y install epel-release
fi
sed -i 's/enabled=./enabled=1/g' /etc/yum.repos.d/epel.repo
yum -y install python2-pip
pip install --upgrade pip
pip install --upgrade awscli
cd /usr/local/sbin
curl -O http://s3.amazonaws.com/ec2metadata/ec2-metadata
chmod 755 ec2-metadata
sed -i 's/enabled=./enabled=0/g' /etc/yum.repos.d/epel.repo


