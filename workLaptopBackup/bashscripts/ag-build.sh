#!/bin/bash

declare -a buildTypes=(lball lballx mrball lbui mrbui lbi mrbi mrb lb lbbs lbmgcp lvipm mvipm adaptor)
declare -a expectedVMs=(vm1 vm2 vmx vmsipp vm61 vm62 vm71 vm72 vm73 vmd vmh vmh2 t7 t34 t50 t62 t86 t102 t103 t105 t49 t145 t159 t165 xms59 xms70 i6mrb1 i6mrb2 i6xms lb1 lb2 c6 bamboo1 bamboo2)
declare -a installerProjects=(installers/mrb-installer installers/lb-installer)


declare -a lbui=(common/admin-web-gui-shared lb/lb-admin-web-gui)
declare -a mrbui=(common/admin-web-gui-shared mrb/mrb-admin-web-gui)
declare -a lbi=(installers/installer-common installers/lb-installer)
declare -a mrbi=(installers/installer-common installers/mrb-installer)
declare -a mrb=(common/nst-jain-sip-api common/jain-sip-mod mrb/nio-http-proxy common/nst-logging common/nst-common mrb/mrb-common mrb/mrb-jmx-shared mrb/mrb)
declare -a lbbs=(lb/lb-jmx-shared lb/lb-licensing lb/nst-bootstrap)
declare -a lbmgcp=(lb/mgcp-stack lb/mgcp-gateway)
declare -a lb=(lb/lb-jmx-shared common/jain-sip-mod lb/nst-bootstrap common/sip-loadbalancer-common common/sip-loadbalancer-stateless lb/lb-sip lb/proxy lb/nst-lb)
declare -a vipm=(common/vip-manager)
declare -a adaptor=(mrb/ms-common common/jain-sip-mod mrb/ms-adaptor)


trunkDir=/home/agrahame/projects/src
lb11Dir=/home/agrahame/projects/branches/lb-branch-1.1.9.10
lb13Dir=/home/agrahame/projects/branches/lb-1.3-branch
lb14Dir=/home/agrahame/projects/branches/lb-1.4-branch
lb15Dir=/home/agrahame/projects/branches/lb-1.5-branch
lb16Dir=/home/agrahame/projects/branches/lb-1.6-branch
mrb12Dir=/home/agrahame/projects/branches/mrb-1.2-branch
mrb14Dir=/home/agrahame/projects/branches/mrb-1.4-branch
mrb15Dir=/home/agrahame/projects/branches/mrb-1.5-branch
mrb32Dir=/home/agrahame/projects/branches/mrb-3.2-branch
mrb33Dir=/home/agrahame/projects/branches/mrb-3.3-branch
mrb34Dir=/home/agrahame/projects/branches/mrb-3.4.0-branch
mrb35Dir=/home/agrahame/projects/branches/mrb-3.5-branch
webRtcDir=/home/agrahame/projects/branches/webrtc_calls_api_impl
lbNettyDir=/home/agrahame/projects/branches/lb-235-netty-4.1-migration
lic2Dir=/home/agrahame/projects/branches/lic2
#
#
#------------ Pick a branch, any branch !!!!!
#
#
# Trunk
#
baseDir=$trunkDir
#baseDir=$agtemplb
#
#
# LB branches
#
#baseDir=$lb11Dir
#baseDir=$lb13Dir
#baseDir=$lb14Dir
#baseDir=$lb15Dir
#baseDir=$lb16Dir
#baseDir=$lbNettyDir
#
#
# MRB branches
#
#baseDir=$mrb12Dir
#baseDir=$mrb14Dir
#baseDir=$mrb15Dir
#baseDir=$mrb32Dir
#baseDir=$mrb33Dir
#baseDir=$mrb34Dir
#baseDir=$mrb35Dir
#
#
# Other branches
#
#baseDir=$webRtcDir
#baseDir=$lic2Dir
#
#------------------------------------------------------
#


###
# set up some directory vars
#
lbInstallDir="/opt/nst-loadbalancer"
mrbInstallDir="/opt/mrb"
jettyDir="jetty-distribution-8.1.10.v20130312"
jettyAppsDir="$jettyDir/webapps"
adaptorInstallDir="/opt/adaptor"

lbInstallerJarNameD="dialogic-lb-installer-*.jar"
mrbInstallerJarNameD="dialogic-mrb-installer-*.jar"
#lbInstallerJarNameBT="bt-lb-installer-*.jar"
#mrbInstallerJarNameBT="bt-mrb-installer-*.jar"

vipManagerJarName="vip-manager.jar"
vipManagerUnobsJarName="vip-manager-unobs.jar"

mrbJarName="mrb.jar"
mrbUnobsJarName="mrb-unobs.jar"

lbJarName="nst-lb.jar"
lbUnobsJarName="nst-lb-unobs.jar"

lbMgcpJarName="mgcp-gateway.jar"
lbMgcpUnobsJarName="mgcp-gateway-unobs.jar"

lbbsUnobsJarName="nst-bootstrap-unobs.jar"
lbbsJarName="nst-bootstrap.jar"

mrbVipManagerJar="$mrbInstallDir/$vipManagerJarName"
lbVipManagerJar="$lbInstallDir/$vipManagerJarName"

adJarName="ms-adaptor.jar"
adUnobsJarName="ms-adaptor-unobs.jar"
#
# service names
lbService="nst-loadbalancer"
jettyService="jetty"
mrbService="mrb"
vipManagerService="nst-vip-manager"
adaptorService="adaptor"
#
#

usage() {
    echo " "
    echo "$@"
    echo " "
    echo "Usage : bld <build-type> <remote-machine-to-copy-to>"
    echo "        build-type     : ${buildTypes[@]}"
    echo "        remote machine : ${expectedVMs[@]}"
    echo " "
    exit -1
}

showBaseDir() {
    echo " "
    echo " "
    echo " ====================================================================="
    echo " "
    echo "       agb - baseDir : $baseDir"
    echo " "
    echo " ====================================================================="
    echo " "
    cont "Happy with the projects directory being built ?"
}

checkArg() {
   goodArg=false

    declare -a argAry1=("${!2}")
    #echo "${argAry1[@]}"

   for checkType in ${argAry1[@]}
   do
      #echo " testing : $checkType"
      if [[ $3 == $checkType* ]] 
      then
         goodArg=true
         break
      fi
   done

   if [ "$goodArg" = false ] ; then
       usage "Unexpected " $1 " provided [" $3 "] expected " ${argAry1[@]}
   fi
}

function contains() {
    local n=$#
    local value=${!n}
    for ((i=1;i < $#;i++)) {
        if [ "${!i}" == "${value}" ]; then
            return 1
        fi
    }
    return 0
}

function cont {
    echo " "
    echo $1
    select yn in "Yes" "No"; do
        case $yn in
            Yes ) echo "continuing ..."; echo; break;;
            No ) echo "quitting ..."; echo; exit;;
        esac
    done
}


buildProjects() {
   goodBuild=false

   declare -a argAry1=("${!1}")
   echo "${argAry1[@]}"

   cont "Happy with the projects being build ?"

   for project in ${argAry1[@]}
   do
      projDir="$baseDir/$project"
      echo " building : $projDir"

      cd $projDir

      ## for installer projects we need to run 'ant create-local-installer' instead of just 'ant'
      ant clean

      ## Check if current project is an installer project
      contains "${installerProjects[@]}" "$project"
      if [ $? -eq 1 ]; then
         echo "INSTALLER Project (${project}) so building local installer ....."
         if [ "$baseDir" == "$lb11Dir" ] || [ "$baseDir" == "$lb15Dir" ]  ; then
            ant create-installer |& tee /tmp/bld.out
         elif [ "$useNewProjectDirStructure" = false ] ; then
            ant create-local-installer |& tee /tmp/bld.out
         else
            ant |& tee /tmp/bld.out
         fi
      else 
         echo "NOT an installer project so just building ant ....."
         ant |& tee /tmp/bld.out
      fi
      tail /tmp/bld.out  | grep "BUILD SUCCESSFUL"
      if [[ $? -ne 0 ]] 
      then 
         echo "Failed to build : $projDir, exiting ...."
         exit 1;
      fi
   done
}

buildAllMrb() {
   goodBuild=false

   cd $baseDir
   
   if [ "$useNewProjectDirStructure" = false ] ; then
      cd $baseDir/mrb
      $buildScriptDir/buildall |& tee /tmp/bld.out
   else
      cd $baseDir
      ant clean mrb-build |& tee /tmp/bld.out
   fi

   tail /tmp/bld.out  | grep "BUILD SUCCESSFUL"
   if [[ $? -ne 0 ]] 
   then 
      echo "Failed to build-all, exiting ...."
      exit 1;
   fi
}

buildAllLb() {
   goodBuild=false
   
   if [ "$useNewProjectDirStructure" = false ] ; then
      cd $baseDir/lb
      $buildScriptDir/buildall |& tee /tmp/bld.out
   else
      cd $baseDir
      ant clean lb-build |& tee /tmp/bld.out
   fi

   tail /tmp/bld.out  | grep "BUILD SUCCESSFUL"
   if [[ $? -ne 0 ]] 
   then 
      echo "Failed to build-all, exiting ...."
      exit 1;
   fi
}

copyBootstrap() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying Bootstrap ... "; echo
   lbbsLocal=$baseDir/lb/nst-bootstrap/target/jars/$lbbsUnobsJarName
   lbbsRemote=$remoteMachine:$lbInstallDir/$lbbsJarName
   echo " "; echo " "; echo " Newly built BOOTSTRAP jar : "
   ls -la $lbbsLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $lbbsLocal $lbbsRemote
   ssh $remoteMachine "ls -la ${lbInstallDir}/${lbbsJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       lbbsRemote2=$remoteMachine2:$lbInstallDir/$lbbsJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2"
       scp $lbbsLocal $lbbsRemote2
       ssh $remoteMachine2 "ls -la ${lbInstallDir}/${lbbsJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyLbJar() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying LB jar ... "; echo
   lbLocal=$baseDir/lb/nst-lb/target/jars/$lbUnobsJarName
   lbRemote=$remoteMachine:$lbInstallDir/$lbJarName
   echo " "; echo " "; echo " Newly built LB jar : "
   ls -la $lbLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $lbLocal $lbRemote
   ssh $remoteMachine "ls -la ${lbInstallDir}/${lbJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       lbRemote2=$remoteMachine2:$lbInstallDir/$lbJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2"
       scp $lbLocal $lbRemote2
       ssh $remoteMachine2 "ls -la ${lbInstallDir}/${lbJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyLbMgcp() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying MGCP Gateway ... "; echo
   lbMgcpLocal=$baseDir/lb/mgcp-gateway/target/jars/$lbMgcpUnobsJarName
   lbMgcpRemote=$remoteMachine:$lbInstallDir/$lbMgcpJarName
   echo " "; echo " "; echo " Newly built MGCP jar : "
   ls -la $lbMgcpLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $lbMgcpLocal $lbMgcpRemote
   ssh $remoteMachine "ls -la ${lbInstallDir}/${lbMgcpJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       lbMgcpRemote2=$remoteMachine2:$lbInstallDir/$lbMgcpJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2"
       scp $lbMgcpLocal $lbMgcpRemote2
       ssh $remoteMachine2 "ls -la ${lbInstallDir}/${lbMgcpJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyMrbJar() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying MRB jar ... "; echo
   mrbLocal=$baseDir/mrb/mrb/target/jars/$mrbUnobsJarName
   mrbRemote=$remoteMachine:$mrbInstallDir/$mrbJarName
   echo " "; echo " "; echo " Newly built MRB jar : "
   ls -la $mrbLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $mrbLocal $mrbRemote
   ssh $remoteMachine "ls -la ${mrbInstallDir}/${mrbJarName}"
   
   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       mrbRemote2=$remoteMachine2:$mrbInstallDir/$mrbJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2"
       scp $mrbLocal $mrbRemote2
       ssh $remoteMachine2 "ls -la ${mrbInstallDir}/${mrbJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyVipManagerJarLB() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying VIP Manager (LB) ... "; echo
   vipmLocal=$baseDir/common/vip-manager/target/jars/$vipManagerUnobsJarName
   vipmRemote=$remoteMachine:$lbInstallDir/$vipManagerJarName
   echo " "; echo " "; echo " Newly built vip-manager jar : "
   ls -la $vipmLocal
   echo " "; echo " "; echo " Copying to $remoteMachine LB installation"
   scp $vipmLocal $vipmRemote
   ssh $remoteMachine "ls -la ${lbInstallDir}/${vipManagerJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       vipmRemote2=$remoteMachine2:$lbInstallDir/$vipManagerJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2 LB installation"
       scp $vipmLocal $vipmRemote2
       ssh $remoteMachine2 "ls -la ${lbInstallDir}/${vipManagerJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyVipManagerJarMRB() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying VIP Manager (MRB) ... "; echo
   vipmLocal=$baseDir/common/vip-manager/target/jars/$vipManagerUnobsJarName
   vipmRemote=$remoteMachine:$mrbInstallDir/$vipManagerJarName
   echo " "; echo " "; echo " Newly built vip-manager jar : "
   ls -la $vipmLocal
   echo " "; echo " "; echo " Copying to $remoteMachine MRB installation"
   scp $vipmLocal $vipmRemote
   ssh $remoteMachine "ls -la ${mrbInstallDir}/${vipManagerJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       vipmRemote2=$remoteMachine2:$mrbInstallDir/$vipManagerJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2 MRB installation"
       scp $vipmLocal $vipmRemote2
       ssh $remoteMachine2 "ls -la ${mrbInstallDir}/${vipManagerJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyLbUi() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying LB-UI ... "; echo
   lbuiLocal=$baseDir/lb/lb-admin-web-gui/target/jars/lb-admin-web-gui-unobs-dialogic.war
   lbuiRemote=$remoteMachine:$lbInstallDir/$jettyAppsDir/
   echo " "; echo " "; echo " Newly built jar : "
   ls -la $lbuiLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $lbuiLocal $lbuiRemote
   ssh $remoteMachine "ls -la ${lbInstallDir}/${jettyAppsDir}/lb*.war"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
      lbuiRemote2=$remoteMachine2:$lbInstallDir/$jettyAppsDir/
      echo " "; echo " "; echo " Copying to $remoteMachine2"
      scp $lbuiLocal $lbuiRemote2
      ssh $remoteMachine2 "ls -la ${lbInstallDir}/${jettyAppsDir}/lb*.war"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyMrbUi() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying MRB-UI ... "; echo
   mrbuiLocal=$baseDir/mrb/mrb-admin-web-gui/target/jars/mrb-admin-web-gui-unobs-dialogic.war
   mrbuiRemote=$remoteMachine:$mrbInstallDir/$jettyAppsDir/
   echo " "; echo " "; echo " Newly built jar : "
   ls -la $mrbuiLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $mrbuiLocal $mrbuiRemote
   ssh $remoteMachine "ls -la ${mrbInstallDir}/${jettyAppsDir}/mrb*.war"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
      mrbuiRemote2=$remoteMachine2:$mrbInstallDir/$jettyAppsDir/
      echo " "; echo " "; echo " Copying to $remoteMachine2"
      scp $mrbuiLocal $mrbuiRemote2
      ssh $remoteMachine2 "ls -la ${mrbInstallDir}/${jettyAppsDir}/mrb*.war"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

copyAdaptorJar() {
   echo " "; echo "--------------------------------------------"; echo " "
   echo "  Copying Adaptor jar ... "; echo
   adLocal=$baseDir/mrb/ms-adaptor/target/jars/$adUnobsJarName
   adRemote=$remoteMachine:$adaptorInstallDir/$adJarName
   echo " "; echo " "; echo " Newly built Adaptor jar : "
   ls -la $adLocal
   echo " "; echo " "; echo " Copying to $remoteMachine"
   scp $adLocal $adRemote
   ssh $remoteMachine "ls -la ${adaptorInstallDir}/${adJarName}"

   # did user provide a second remote machine to copy to ?
   if [ -n "$remoteMachine2" ]
   then
       adRemote2=$remoteMachine2:$adaptorInstallDir/$adJarName
       echo " "; echo " "; echo " Copying to $remoteMachine2"
       scp $adLocal $adRemote2
       ssh $remoteMachine2 "ls -la ${adaptorInstallDir}/${adJarName}"
   fi
   echo " "; echo "--------------------------------------------"; echo " "
}

doBranchSpecific() {
   echo " "; echo "--------------------------------------------"; echo " "
   case "$baseDir" in
      $lb11Dir | $lb13Dir | $lb14Dir | $lb15Dir | $lb16Dir | $mrb12Dir | $mrb14Dir | $mrb15Dir | $mrb32Dir | $mrb33Dir | $mrb34Dir | $mrb35Dir | $webRtcDir | $lbNettyDir) echo "Using old project dir structure ..."
         useNewProjectDirStructure=false
           ;;

      *) echo "New project dir structure ..."
         useNewProjectDirStructure=true
         ;;
   esac
   echo " "; echo "--------------------------------------------"; echo " "
}

##########################
#########
##

if [ $# -lt 2 ]
  then
     usage "Unexpected number of args"
fi

# show user the project directory being built
showBaseDir

/home/agrahame/scripts/t1.sh

checkArg "Build Type" buildTypes[@] $1
checkArg "Remote Machine" expectedVMs[@] $2
if [ $# -eq 3 ]
then
   checkArg "Remote Machine (2)" expectedVMs[@] $3
   remoteMachine2=$3
fi

#if we get here the given args are correct, now lets give them more meaningful names
buildType=$1
remoteMachine=$2

if [ "$baseDir" == "$trunkDir" ]; then
   buildScriptDir="bin"
else
   buildScriptDir="."
fi

doBranchSpecific

# now do the work
case "$buildType" in
   lbi) echo "Building LB Installer ..."
        buildProjects lbi[@]
        lbiLocalD="$baseDir/installers/lb-installer/target/installer/$lbInstallerJarNameD"
        #lbiLocalBT="$baseDir/installers/lb-installer/target/installer/$lbInstallerJarNameBT"
        lbiRemote="$remoteMachine:"
        echo " "; echo " "; echo " Newly built LB installer jars : "
        ls -la $lbiLocalD
        echo " "; echo " "; echo " Copying to $remoteMachine"
        scp $lbiLocalD $lbiRemote
        #scp $lbiLocalBT $lbiRemote
        ssh $remoteMachine "ls -la /root/$lbInstallerJarNameD"

        # did user provide a second remote machine to copy to ?
        if [ -n "$remoteMachine2" ]
        then
            echo " "; echo " "; echo " Copying to $remoteMachine2"
            lbiRemote2="$remoteMachine2:"
            scp $lbiLocalD $lbiRemote2
            #scp $lbiLocalBT $lbiRemote2
            ssh $remoteMachine2 "ls -la /root/$lbInstallerJarNameD"
        fi
        ;;

   mrbi) echo "Building MRB Installer ..."
         buildProjects mrbi[@]
         mrbiLocalD="$baseDir/installers/mrb-installer/target/installer/$mrbInstallerJarNameD"
         #mrbiLocalBT="$baseDir/installers/mrb-installer/target/installer/$mrbInstallerJarNameBT"
         mrbiRemote="$remoteMachine:"
         echo " "; echo " "; echo " Newly built MRB installer jars : "
         ls -la $mrbiLocalD
         echo " "; echo " "; echo " Copying to $remoteMachine"
         scp $mrbiLocalD $mrbiRemote
         #scp $mrbiLocalBT $mrbiRemote
         ssh $remoteMachine "ls -la /root/$mrbInstallerJarNameD"

         # did user provide a second remote machine to copy to ?
         if [ -n "$remoteMachine2" ]
         then
            echo " "; echo " "; echo " Copying to $remoteMachine2"
            mrbiRemote2="$remoteMachine2:"
            scp $mrbiLocalD $mrbiRemote2
            #scp $mrbiLocalBT $mrbiRemote2
            ssh $remoteMachine2 "ls -la /root/$mrbInstallerJarNameD"
         fi
         ;;

   lbui) echo "Building LB UI ..."
         buildProjects lbui[@]
         copyLbUi
         ssh $remoteMachine "service $jettyService stop; echo; echo"
         ssh $remoteMachine "find ${lbInstallDir}/${jettyDir} -name \"*.log*\" | xargs rm -fv; echo"
         ssh $remoteMachine "service $jettyService start"

         # did user provide a second remote machine to copy to ?
         if [ -n "$remoteMachine2" ]
         then
            ssh $remoteMachine2 "service $jettyService stop; echo; echo"
            ssh $remoteMachine2 "find ${lbInstallDir}/${jettyDir} -name \"*.log*\" | xargs rm -fv; echo"
            ssh $remoteMachine2 "service $jettyService start"
         fi
         ;;

   mrbui) echo "Building MRB UI ..."
          buildProjects mrbui[@]
          copyMrbUi
          ssh $remoteMachine "service $jettyService stop; echo; echo"
          ssh $remoteMachine "find ${mrbInstallDir}/${jettyDir} -name \"*.log*\" | xargs rm -fv; echo"
          ssh $remoteMachine "service $jettyService start"

          # did user provide a second remote machine to copy to ?
          if [ -n "$remoteMachine2" ]
          then
             ssh $remoteMachine2 "service $jettyService stop; echo; echo"
             ssh $remoteMachine2 "find ${mrbInstallDir}/${jettyDir} -name \"*.log*\" | xargs rm -fv; echo"
             ssh $remoteMachine2 "service $jettyService start"
          fi
          ;;

   mrb)  echo "Building MRB jar ..."
         buildProjects mrb[@]
         copyMrbJar
         ssh $remoteMachine "service $mrbService stop; echo; echo"
         ssh $remoteMachine "find ${mrbInstallDir}/ -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
         ssh $remoteMachine "service $mrbService start"

         # did user provide a second remote machine to copy to ?
         if [ -n "$remoteMachine2" ]
         then
            ssh $remoteMachine2 "service $mrbService stop; echo; echo"
            ssh $remoteMachine2 "find ${mrbInstallDir}/ -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
            ssh $remoteMachine2 "service $mrbService start"
         fi
         ;;

   lbbs)  echo "Building LB Bootstrap ..."
          buildProjects lbbs[@]
          copyBootstrap
          ssh $remoteMachine "service $lbService stop; echo; echo"
          ssh $remoteMachine "find ${lbInstallDir} -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
          ssh $remoteMachine "service $lbService start"

          # did user provide a second remote machine to copy to ?
          if [ -n "$remoteMachine2" ]
          then
             ssh $remoteMachine2 "service $lbService stop; echo; echo"
             ssh $remoteMachine2 "find ${lbInstallDir} -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
             ssh $remoteMachine2 "service $lbService start"
          fi
          ;;

   lbmgcp)  echo "Building LB MGCP ..."
          buildProjects lbmgcp[@]
          copyLbMgcp
          ssh $remoteMachine "service $lbService stop; echo; echo"
          ssh $remoteMachine "find ${lbInstallDir} -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
          ssh $remoteMachine "service $lbService start"

          # did user provide a second remote machine to copy to ?
          if [ -n "$remoteMachine2" ]
          then
             ssh $remoteMachine2 "service $lbService stop; echo; echo"
             ssh $remoteMachine2 "find ${lbInstallDir} -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
             ssh $remoteMachine2 "service $lbService start"
          fi
          ;;

   lb)  echo "Building LB jar..."
        buildProjects lb[@]
        copyLbJar
        ssh $remoteMachine "service $lbService stop; echo; echo"
        ssh $remoteMachine "find ${lbInstallDir}/ -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
        ssh $remoteMachine "service $lbService start"

        # did user provide a second remote machine to copy to ?
        if [ -n "$remoteMachine2" ]
        then
           ssh $remoteMachine2 "service $lbService stop; echo; echo"
           ssh $remoteMachine2 "find ${lbInstallDir}/ -maxdepth 1 -name \"*.log*\" | xargs rm -fv; echo"
           ssh $remoteMachine2 "service $lbService start"
        fi
        ;;

   lvipm)  echo "Building VIP Manager (for LB) ..."
           buildProjects vipm[@]
           copyVipManagerJarLB
           ssh $remoteMachine "service $vipManagerService stop; echo; echo"
           ssh $remoteMachine "find ${lbInstallDir}/ -maxdepth 1 -name \"nst-vip-manager*.log*\" | xargs rm -fv; echo"
           ssh $remoteMachine "service $vipManagerService start"

           # did user provide a second remote machine to copy to ?
           if [ -n "$remoteMachine2" ]
           then
              ssh $remoteMachine2 "service $vipManagerService stop; echo; echo"
              ssh $remoteMachine2 "find ${lbInstallDir}/ -maxdepth 1 -name \"nst-vip-manager*.log*\" | xargs rm -fv; echo"
              ssh $remoteMachine2 "service $vipManagerService start"
           fi
           ;;

   mvipm)  echo "Building VIP Manager (for MRB) ..."
           buildProjects vipm[@]
           copyVipManagerJarMRB
           ssh $remoteMachine "service $vipManagerService stop; echo; echo"
           ssh $remoteMachine "find ${mrbInstallDir}/ -maxdepth 1 -name \"nst-vip-manager*.log*\" | xargs rm -fv; echo"
           ssh $remoteMachine "service $vipManagerService start"

           # did user provide a second remote machine to copy to ?
           if [ -n "$remoteMachine2" ]
           then
              ssh $remoteMachine2 "service $vipManagerService stop; echo; echo"
              ssh $remoteMachine2 "find ${mrbInstallDir}/ -maxdepth 1 -name \"nst-vip-manager*.log*\" | xargs rm -fv; echo"
              ssh $remoteMachine2 "service $vipManagerService start"
           fi
           ;;

   lball) echo "Building all, copying new versions of unobfuscated jars, cleaning out logs and restarting all services"
          buildAllLb
          copyBootstrap
          copyLbJar
          copyLbMgcp
          copyVipManagerJarLB
          copyLbUi
          ssh $remoteMachine "service $jettyService stop; service $vipManagerService stop; service $lbService stop; echo; echo"
          ssh $remoteMachine "find ${lbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
          ssh $remoteMachine "service $jettyService start; service $vipManagerService start; service $lbService start"

          # did user provide a second remote machine to copy to ?
          if [ -n "$remoteMachine2" ]
          then
             ssh $remoteMachine2 "service $jettyService stop; service $vipManagerService stop; service $lbService stop; echo; echo"
             ssh $remoteMachine2 "find ${lbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
             ssh $remoteMachine2 "service $jettyService start; service $vipManagerService start; service $lbService start"
          fi
          ;;

   lballx) echo "copying unobfuscated jars, cleaning out logs and restarting all services"
          copyBootstrap
          copyLbJar
          copyLbMgcp
          copyVipManagerJarLB
          copyLbUi
          ssh $remoteMachine "service $jettyService stop; service $vipManagerService stop; service $lbService stop; echo; echo"
          ssh $remoteMachine "find ${lbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
          ssh $remoteMachine "service $jettyService start; service $vipManagerService start; service $lbService start"

          # did user provide a second remote machine to copy to ?
          if [ -n "$remoteMachine2" ]
          then
             ssh $remoteMachine2 "service $jettyService stop; service $vipManagerService stop; service $lbService stop; echo; echo"
             ssh $remoteMachine2 "find ${lbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
             ssh $remoteMachine2 "service $jettyService start; service $vipManagerService start; service $lbService start"
          fi
          ;;

   mrball) echo "Building all, copying new versions of unobfuscated jars, cleaning out logs and restarting all services"
           buildAllMrb
           copyMrbJar
           copyVipManagerJarMRB
           copyMrbUi
           ssh $remoteMachine "service $jettyService stop; service $vipManagerService stop; service $mrbService stop; echo; echo"
           ssh $remoteMachine "find ${mrbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
           ssh $remoteMachine "service $jettyService start; service $vipManagerService start; service $mrbService start"

           # did user provide a second remote machine to copy to ?
           if [ -n "$remoteMachine2" ]
           then
              ssh $remoteMachine2 "service $jettyService stop; service $vipManagerService stop; service $mrbService stop; echo; echo"
              ssh $remoteMachine2 "find ${mrbInstallDir}/ -name \"*.log*\" | xargs rm -fv; echo"
              ssh $remoteMachine2 "service $jettyService start; service $vipManagerService start; service $mrbService start"
           fi
           ;;

   adaptor)  echo "Building Adaptor ..."
           buildProjects adaptor[@]
           copyAdaptorJar
           ssh $remoteMachine "service $adaptorService stop; echo; echo"
           ssh $remoteMachine "find ${adaptorInstallDir}/ -maxdepth 2 -name \"nst-ms-adaptor*.log*\" | xargs rm -fv; echo"
           ssh $remoteMachine "service $adaptorService start"

           # did user provide a second remote machine to copy to ?
           if [ -n "$remoteMachine2" ]
           then
              ssh $remoteMachine2 "service $adaptorService stop; echo; echo"
              ssh $remoteMachine2 "find ${adaptorInstallDir}/ -maxdepth 2 -name \"nst-ms-adaptor*.log*\" | xargs rm -fv; echo"
              ssh $remoteMachine2 "service $adaptorService start"
           fi
           ;;

   *) echo "Unexpected build type : $buildType"
      ;;
esac

/home/agrahame/scripts/t2.sh

echo " "; echo " "
