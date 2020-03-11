# ~/.profile: executed by the command interpreter for login shells.
# This file is not read by bash(1), if ~/.bash_profile or ~/.bash_login
# exists.
# see /usr/share/doc/bash/examples/startup-files for examples.
# the files are located in the bash-doc package.

# the default umask is set in /etc/profile; for setting the umask
# for ssh logins, install and configure the libpam-umask package.
#umask 022

# if running bash
if [ -n "$BASH_VERSION" ]; then
    # include .bashrc if it exists
    if [ -f "$HOME/.bashrc" ]; then
	. "$HOME/.bashrc"
    fi
fi

# set PATH so it includes user's private bin if it exists
if [ -d "$HOME/bin" ] ; then
    PATH="$HOME/bin:$PATH"
fi

# set PATH so it includes user's private bin if it exists
if [ -d "$HOME/.local/bin" ] ; then
    PATH="$HOME/.local/bin:$PATH"
fi


export http_proxy='http://199.19.250.205:80'
export https_proxy='http://199.19.250.205:80'
export ftp_proxy='http://199.19.250.205:80'
export DOCKER_HOST='tcp://localhost:2375'
export no_proxy="localhost, 127.0.0.1, pr-ghub.admiral.uk, *.pr-ghub.admiral.uk"
#export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
#export JAVA_HOME=/opt/jdk1.8.0_192/
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export JAVA_HOME=/opt/jdk-13.0.1/
export PATH=$PATH:$JAVA_HOME/bin
export JAVA_OPTS="-Dhttp.proxyHost=199.19.250.205 -Dhttp.proxyPort=80 -Dhttps.proxyHost=199.19.250.205 -Dhttps.proxyPort=80 -Djavax.net.ssl.trustStore=$JAVA_HOME/jre/lib/security/cacerts -Djavax.net.ssl.trustStorePassword=changeit"

# set up path to android-sdk
export ANDROID_HOME="/mnt/c/Users/agrahame/android-sdks/"
export PATH="${PATH}:${ANDROID_HOME}tools/:${ANDROID_HOME}platform-tools/"

#export M2_HOME=/usr/share/maven
#export M2=$M2_HOME/bin
#export MAVEN_OPTS="-Xms256m -Xmx512m"
#export PATH=$PATH:$M2


# for X stuff
export DISPLAY=:0
