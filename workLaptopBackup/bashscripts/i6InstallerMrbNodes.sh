#!/bin/bash

echo;
echo Copying mrb installer to i6mrb1 ....
echo;
echo;

scp -i ~/Downloads/ovm_93.pem /home/agrahame/projects/src/installers/mrb-installer/target/installer/dialogic-mrb-installer-*.jar i6mrb1:

echo;
echo Invoking script to copy installer to i6mrb2 ....
echo;

ssh -i ~/Downloads/ovm_93.pem i6mrb1 "sh i6i.sh"

echo;
echo;
echo Done
echo;
echo

