#!/bin/bash

if [ -z "$1" ]
  then
    echo "Usage: fixTypechange.sh <fileToFix>"
    exit 1
fi

if [ ! -f $1 ]; then
    echo "file does not exist : $1"
    exit 1
fi

echo "git status for $1 is " :
git status $1

echo "now reverting $1 ..."
git reset HEAD $1
git checkout -- $1

echo "new git status for $1 ....."
git status $1

echo " "
echo "all done....."
echo " "


