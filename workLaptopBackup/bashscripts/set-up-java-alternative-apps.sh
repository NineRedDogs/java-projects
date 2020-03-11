#!/bin/bash

###########################################################
#
#  AJG - added unlink, ln and update-alternatives to sudoers for my account - to allow script to be run without password for the 2 x sudo commands later in this script
#
###########################################################



if [ -z "$1" ]
  then
    echo "No argument supplied, expecting single argument [ 7 | 8 ]     --> exitting ..."
    exit -1
elif [ $1 = "7" ]; then
   javaBase="$JAVA_7_HOME"
   source /home/agrahame/scripts/j7jh.sh
elif [ $1 = "8" ]; then
   javaBase="$JAVA_8_HOME"
   source /home/agrahame/scripts/j8jh.sh
else 
   echo "Unexpected java version requested - was looking for 7 or 8, but got [$1]"
   exit -1
fi

#echo =1==================
#ls -la /opt/java/
#echo =1==================

sudo unlink /opt/java/java

#echo =2==================
#ls -la /opt/java/
#echo =2==================

sudo ln -s $javaBase /opt/java/java

#source /home/agrahame/scripts/updatePathVar.sh

#echo =3==================
#ls -la /opt/java/
#echo =3==================

export javaDir=$javaBase/bin

declare -a javaApps=(appletviewer apt ControlPanel extcheck idlj jar jarsigner java javac javadoc javafxpackager javah javap java-rmi.cgi javaws jcmd jconsole jcontrol jdb jhat jinfo jmap jmc jmc.ini jps jrunscript jsadebugd jstack jstat jstatd jvisualvm keytool native2ascii orbd pack200 policytool rmic rmid rmiregistry schemagen serialver servertool tnameserv unpack200 wsgen wsimport xjc)

for javaApp in ${javaApps[@]}
do
   echo; echo; echo;
   echo Processing java app : $javaApp ....
   
   sudo update-alternatives --install "/usr/bin/$javaApp" "$javaApp" "$javaDir/$javaApp" 1
   sudo update-alternatives --set $javaApp $javaDir/$javaApp

   echo
   #echo ---------------------
   #echo ---------------------

done

echo
echo
echo " Java version : "
echo
java -version
echo " "
echo " JAVA_HOME : $JAVA_HOME"
echo "------------------------------------------ "
echo " "
echo " PATH : $PATH"
echo "------------------------------------------ "
echo
