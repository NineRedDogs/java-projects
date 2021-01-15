#!/bin/bash

#
# This script cycles runs the cypher import python script with various combinations of 
# values for num_records and cypher_batch_size.
#
# useful for checking the time cost for small incremental changes to the data model, and how it's imported to neo4j
#
# each combination is executed 3 times, and the avge of the 3 runs is calculated.
# the summary of the results is found in importTimes.log and the full set of console output is kept in allOutput.log
# 
#

rm -f allOutput.log
rm -f importTimes.log

for rows in  100 200 500 1000 2000 5000 10000 20000 50000; do
#for rows in  2000 500; do

    for batchSize in 100 1000 5000; do
#    for batchSize in 100 1000; do

       echo "   rows: $rows, batchSize: $batchSize .... "
       for loop in 1 2 3; do
          echo "      loop : $loop ...."

          python cypherImport.py -c $rows -b $batchSize > c1.txt
          cat c1.txt >> allOutput.log
          grep Recs c1.txt >> currRun.log
          rm -f c1.txt
      done

      echo "--------#######################" |& tee -a importTimes.log
      avgeT=$(awk '{total += $9} END {print total / 3}' currRun.log)
      echo |& tee -a importTimes.log
      echo |& tee -a importTimes.log
      
      echo "   ==> rows: $rows, batchSize: $batchSize, AVGE : $avgeT" |& tee -a importTimes.log
      echo |& tee -a importTimes.log
      cat currRun.log  |& tee -a importTimes.log
      echo |& tee -a importTimes.log
      echo |& tee -a importTimes.log
      echo "--------#######################" |& tee -a importTimes.log
      rm -f currRun.log

   done
done

