#!/bin/bash

/home/agrahame/scripts/t1.sh

cd /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/daemon
make clean; make

if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine DAEMON, exiting ...."; echo
         exit 1;
      fi

cd /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/iptables-extension
make clean; make

if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine iptables-extension, exiting ...."; echo
         exit 1;
      fi

cd /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/kernel-module
make clean; make
if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build : rtpengine KERNEL module, exiting ...."; echo
         exit 1;
      fi

# no copying over locally built components to vm, as I've been having issues with incompatible component versions, e.g.:
#
# iptables: target "MEDIAPROXY" has version "libxtables.so.10", but "libxtables.so.4" is required.
#

cd /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine

make clean

cd /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/

tar cvzf rtpengine-ajg.tgz rtpengine

if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to create rtpengine tar ball, exiting ...."; echo
         exit 1;
      fi

scp /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine-ajg.tgz vm1:rtpengine/mods/

## now build the mrb installer
cd /home/agrahame/projects/src/installers/mrb-installer
ant clean; ant create-local-installer

if [[ $? -ne 0 ]] 
      then 
         echo; echo; echo "Failed to build MRB installer ...."; echo
         exit 1;
      fi


scp /home/agrahame/projects/src/installers/mrb-installer/target/installer/dialogic-mrb-installer-1.5.9-pre.jar vm1:rtpengine/mods/

echo; echo; echo "RTPEngine built : "; echo
ls -la /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/daemon/rtpengine
ls -la /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/iptables-extension/libxt_MEDIAPROXY.so
ls -la /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine/kernel-module/xt_MEDIAPROXY.ko

echo; echo; echo "RTPEngine tarball created : "; echo
ls -la /home/agrahame/projects/3rd-party-src/rtpengine/curr-mrb-version/rtpengine-1.0.2.tgz

echo; echo; echo "MRB installer created : "; echo
ls -la /home/agrahame/projects/src/installers/mrb-installer/target/installer/dialogic-mrb-installer-1.5.9-pre.jar

echo; echo; echo "MRB installer copied to vm1 : "; echo
ssh vm1 "ls -la /root/rtpengine/mods"

ssh vm1 "sh /root/rtpengine/mods/update-rtp-engine.sh"


#now do the bit of work to copy over the individual rtpengine files, and the appropriate script invocation over there ....

/home/agrahame/scripts/t2.sh

echo; echo; echo;
