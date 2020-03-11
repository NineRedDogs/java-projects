#!/bin/bash

echo;
echo Copying adaptor jar to i6xms ....
echo;
echo;

scp -i ~/Downloads/ovm_93.pem ~/projects/src/mrb/ms-adaptor/target/jars/ms-adaptor-unobs.jar i6xms:ms-adaptor.jar

echo;
echo;
echo Done
echo;
echo

