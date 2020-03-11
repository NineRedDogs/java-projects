#!/bin/bash

NEWLINE=$'\n'

containsElement () {
  local e
  for e in ${@:2}; do [[ "$e" == "$1" ]] && return 0; done
  return 1
}

#startDir="/home/agrahame/projects/src"
#startDir="/home/agrahame/projects/branches/lb-branch-1.1.9.10"
startDir="/home/agrahame/projects/other.src"

for project in $(find $startDir -maxdepth 2 -type d)
do

   checkThisFolder=true
   echo " "
   echo "____________________________________________________________"
   echo " "
   echo processing project : $project
   echo " "
   ivyFile=$project/ivy.xml
   cpFile=$project/.classpath
   
   if [ ! -f $ivyFile ]; then
      checkThisFolder=false
   fi
   
   if [ ! -f $cpFile ]; then
      checkThisFolder=false
   fi
   
   if $checkThisFolder ; 
   then
      # great, both ivy and cp files exist, do some checking ....
   
      # list all nst dependencies from ivy.xml
      ivyDeps=$(grep "dependency org=\"nst\"" $ivyFile | awk -F\" '{ print $4 }')
   
      # list all project dependencies from .classpath
      cpDeps=$(grep "combineaccessrules" $cpFile | awk -F\"\/ '{ print $2 }')
  
      # check ivy deps
      echo "      "
      echo "      ivy deps"
      echo "      ---------"
      newCpEntries=""
      for ivyDep in ${ivyDeps[@]}
      do
          echo "      checking ivy dep : $ivyDep"

          containsElement "$ivyDep" "${cpDeps[@]}"

          if [[ $? -eq 1 ]]; then
             echo "            $ivyDep DOES NOT exist in .classpath file, add following line to .classpath:"
             newCpEntries="$newCpEntries $NEWLINE             <classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/$ivyDep\"/> "
             echo " "
          fi
      done

      if [ -n "$newCpEntries" ]; then
         echo "      Items missing from .classpath - add the following lines to $cpFile"
         echo "$newCpEntries"
      fi
      
      echo "      "
      echo "      classpath deps"
      echo "      ---------------"
      # check cp deps
      for cpDep in ${cpDeps[@]}
      do
          echo "      checking classpath dep : $cpDep"

          containsElement "$cpDep" "${ivyDeps[@]}"

          if [[ $? -eq 1 ]]; then
             echo "            $cpDep DOES NOT exist in ivy.xml file --> Remove from classpath file"
          fi
      done
   
   else
      echo one or both of ivy cp files does not exist skipping this folder
   fi
   
done

echo " "
echo " "
echo " Done !!"
echo " "

