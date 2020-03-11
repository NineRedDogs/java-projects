#!/bin/bash

if [ -z "$1" ]
  then
    echo "No argument supplied, expecting single argument [ 7 | 8 ]     --> exitting ..."
    exit -1
elif [ $1 = "7" ]; then
   javaDir="/opt/java/64bit/jdk1.7.0_67/bin"
elif [ $1 = "8" ]; then
   javaDir="/opt/java/64bit/jdk1.8.0_66/bin"
else 
   echo "Unexpected java version requested - was looking for 7 or 8, but got [$1]"
   exit -1
fi

declare -a javaApps=(appletviewer apt ControlPanel extcheck idlj jar jarsigner java javac javadoc javafxpackager javah javap java-rmi.cgi javaws jcmd jconsole jcontrol jdb jhat jinfo jmap jmc jmc.ini jps jrunscript jsadebugd jstack jstat jstatd jvisualvm keytool native2ascii orbd pack200 policytool rmic rmid rmiregistry schemagen serialver servertool tnameserv unpack200 wsgen wsimport xjc)

for javaApp in ${javaApps[@]}
do
   echo; echo; echo;
   echo Processing java app : $javaApp ....
   
   sudo update-alternatives --install "/usr/bin/$javaApp" "$javaApp" "$javaDir/$javaApp" 1
   sudo update-alternatives --set $javaApp $javaDir/$javaApp

   echo ---------------------
   echo ---------------------

done

echo
echo
echo " Java version : "
echo
java -version
echo " "
echo "------------------------------------------ "
echo
