#!/bin/bash
echo "JAVA_HOME before : $JAVA_HOME"
echo "PATH before : $PATH"
export JAVA_HOME=$JAVA_7_HOME
export PATH=$PATH:$JAVA_HOME/bin
echo "JAVA_HOME after : $JAVA_HOME"
echo "PATH after : $PATH"
echo "------------"
echo
