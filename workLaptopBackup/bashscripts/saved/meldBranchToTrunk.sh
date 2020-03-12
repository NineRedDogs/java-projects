#!/bin/bash

#if [ -z "$1" ]
#  then
#    echo "Usage: compareTrunkBranchLB <fileToCompare>"
#    exit 1
#fi

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

NEWLINE=$'\n'

trunk="/home/agrahame/projects/src"
lbBranch11="/home/agrahame/projects/branches/lb-branch-1.1.9.10"
lbBranch12="/home/agrahame/projects/branches/lb-branch-1.2"
lbBranch13="/home/agrahame/projects/branches/lb-1.3-branch"
mrbBranch12="/home/agrahame/projects/branches/mrb-1.2-branch"
mrbBranch14="/home/agrahame/projects/branches/mrb-1.4-branch"
mrbBranch15="/home/agrahame/projects/branches/mrb-1.5-branch"
webRtcBranch="/home/agrahame/projects/branches/webrtc_calls_api_impl"

#
#sourceBranch="$trunk"
#sourceBranch="$lbBranch11"
#sourceBranch="$lbBranch12"
sourceBranch="$lbBranch13"
#sourceBranch="$mrbBranch12"
#sourceBranch="$mrbBranch14"
#sourceBranch="$mrbBranch15"
#sourceBranch="$webRtcBranch"

destBranch="$trunk"
#destBranch="$lbBranch11"
#destBranch="$lbBranch12"
#destBranch="$lbBranch13"
#destBranch="$mrbBranch12"
#destBranch="$mrbBranch14"
#destBranch="$mrbBranch15"
#destBranch="$webRtcBranch"

echo "about to meld from [ $sourceBranch ]   to    [ $destBranch ]"

cont "Happy with the branches being used ?"


## declare all the files changed on the source branch here

# tip : to get changed files of a commit ->    $ git diff-tree --no-commit-id --name-only -r <SHA>
declare -a filesToCompare=( lb/nst-bootstrap/src/com/nstechnologies/apps/nstlb/service/BootstrapServiceStatus.java lb/nst-bootstrap/src/com/nstechnologies/bootstrap/lvs/LbVipManagementInterface.java lb/nst-bootstrap/src/com/nstechnologies/bootstrap/lvs/LbVipManager.java lb/nst-bootstrap/src/com/nstechnologies/bootstrap/service/BootstrapService.java lb/nst-bootstrap/src/com/nstechnologies/bootstrap/service/ServiceManager.java vip-manager-common/src/com/nstechnologies/apps/vipManager/common/JMXLoadBalancerMXBean.java vip-manager/src/com/nstechnologies/apps/vipManager/app/LbServiceManager.java )

#declare -a filesToCompare=(security-common/src/com/nstechnologies/apps/security/SecurityProfile.java security-common/src/com/nstechnologies/apps/security/Certificate.java security-common/src/com/nstechnologies/apps/security/KeystoreGenerator.java security-common/src/com/nstechnologies/apps/security/ServerCertificate.java)
#declare -a filesToCompare=(lb/proxy/src/com/nstechnologies/proxy/generic/SocketChannelHandler.java lb/proxy/src/com/nstechnologies/proxy/http/HttpInboundHandler.java)

allCopyTips="Here are the commands you will need: $NEWLINE"
filesNotOnDestBranch=""

for file in ${filesToCompare[@]}
do
   echo; echo; echo;
   echo Processing $file ....

   echo =====================================================================
   echo "   File   : " $file
   echo --------------------------------------------------------------------
   echo "   From   : " $sourceBranch
   echo --------------------------------------------------------------------
   echo "   To     : " $destBranch
   echo =====================================================================
   
   destFile="$destBranch/$file"
   sourceFile="$sourceBranch/$file"

   compareThisFile=true

   if [ ! -f $destFile ]; then
       echo "Destination file does not exist : $destFile"
       compareThisFile=false
       
       # give some tips to user to help them do the copy of these files
       copyTip="cp -v -r -a $sourceFile $destFile"
       allCopyTips="$allCopyTips $NEWLINE $copyTip"
       filesNotOnDestBranch="$filesNotOnDestBranch $NEWLINE $destFile"
   fi
   
   if [ ! -f $sourceFile ]; then
       echo "Source file does not exist : $sourceFile"
       compareThisFile=false
   fi
   

   if $compareThisFile ; then

      echo "... Great, both files exist ..... "
      
      echo ---------------------------------------------------------------------
      echo "About to compare the following files :"
      echo "  Source      : $sourceFile"
      echo "  Destination : $destFile"
      echo ---------------------------------------------------------------------
      echo; echo; echo;
      
      if cmp -s "$sourceFile" "$destFile" 
      then
         echo "The files match, so no need to start up MELD"
      else
         echo "The files are different, so will fire up MELD"
         meld $sourceFile $destFile
      fi
      
      echo " "
   fi

done

echo; echo; echo;
echo "All Done!"
echo; echo; echo;
if [ -n "$filesNotOnDestBranch" ]; then
   echo "Remember the following files do NOT exist in the destination branch (i.e. $destBranch) , so you'll need to copy them over :"
   echo "$filesNotOnDestBranch"
   echo; echo; echo;
   echo "$allCopyTips"
   echo; echo; echo;
fi

