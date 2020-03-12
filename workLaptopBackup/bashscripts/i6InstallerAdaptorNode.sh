#!/bin/bash

echo;
echo Copying mrb installer to i6xms ....
echo;
echo;

scp -i ~/Downloads/ovm_93.pem /home/agrahame/projects/src/installers/mrb-installer/target/installer/dialogic-mrb-installer-*.jar i6xms:

echo;
echo;
echo Done
echo;
echo

