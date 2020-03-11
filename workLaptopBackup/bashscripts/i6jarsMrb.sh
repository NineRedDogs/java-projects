#!/bin/bash

echo;
echo Copying mrb jars to i6mrb1 ....
echo;
echo;

scp -i ~/Downloads/ovm_93.pem ~/projects/src/mrb/mrb/target/jars/mrb-dialogic-unobs.jar i6mrb1:mrb.jar

scp -i ~/Downloads/ovm_93.pem ~/projects/src/common/vip-manager/target/jars/vip-manager-unobs.jar i6mrb1:vip-manager.jar

scp -i ~/Downloads/ovm_93.pem ~/projects/src/mrb/mrb-admin-web-gui/target/jars/mrb-admin-web-gui-unobs-dialogic.war i6mrb1:mrb-admin-web-gui.war

echo;
echo Invoking script to copy jars to i6mrb2 ....
echo;

ssh -i ~/Downloads/ovm_93.pem i6mrb1 "sh i6j.sh"

echo;
echo;
echo Done
echo;
echo

