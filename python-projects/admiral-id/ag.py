import csv, sys, argparse, os, re, subprocess, timeit

from datetime import datetime
from itertools import islice
from os.path import abspath

def getArgs():
    argParser = argparse.ArgumentParser(description='Admiral ID CSV parser')
    argParser.add_argument('-c','--csv-rows', help='Output file name', required=True)
    argParser.add_argument('-b','--batch-size', help='Batch file size', default=1000)
    argParser.add_argument('-s','--source-data-file', help='Source CSV file', default="/root/dev/sample-data/data_archive/ADMIRALID_DATASETV2.csv_PARSED")
    argParser.add_argument('-t','--cypher-template-file', help='Cypher template file', default="/root/dev/ARD-ADID/scripts/cyp.template")
    args = argParser.parse_args()
    return args

def getBaseCsvFile():
    if os.path.isfile(args.source_data_file):
        baseCsvFile = args.source_data_file
    else:
        print("source CSV file ", args.source_data_file, " DOES NOT exist, exitting")
        quit()
    return baseCsvFile

def getCypherTemplateFile():
    if os.path.isfile(args.cypher_template_file):
        cypherTemplateFile = args.cypher_template_file
    else:
        print("Cypher template file ", args.cypher_template_file, " DOES NOT exist, exitting")
        quit()
    return cypherTemplateFile

def generateCsvFileName(numCsvRows):
    csvFName = "csvFile_" + str(numCsvRows) + ".csv"
    return csvFName


def generateCsvData(baseCsvFName, numCsvRows):
    csvFile = generateCsvFileName(numCsvRows)
    with open(baseCsvFile) as sourceFile:
        with open(csvFile, 'w') as targetFile:
            for line in islice(sourceFile, numCsvRows):
                targetFile.write(line)

    sourceFile.close()
    targetFile.close()
    return csvFile

def getFullPath(fname):
   path = abspath(fname)
   #print path
   return path

def replaceCypherPlaceholders(cypherTemplateFileName, cypherFileName, csvFile, batchSize):
    with open(cypherTemplateFileName, "r") as templateFile:
        s = templateFile.read()
    with open(cypherFileName, 'w') as cypherFile:
        s = re.sub("_BATCH_SIZE_", str(batchSize), s)
        s = re.sub("_CSV_FILE_", getFullPath(csvFile), s)
        cypherFile.write(s)
    templateFile.close()
    cypherFile.close()

def generateCypherFileName():
    # may want this to be more complex in the future ....
    cypherFName = "adid.cyp"
    return cypherFName

def createCypherFile(templateFile, csvFile, batchSize):
   cypherFile = generateCypherFileName()
   replaceCypherPlaceholders(templateFile, cypherFile, csvFile, batchSize)
   return cypherFile



def runBashCommand(bashCmd):
    result = subprocess.call(bashCmd, shell=True)

    if result != 0:
        print("Error executing command [", bashCmd, "]")
        quit()
    #else:
    #    print("command OK")



##
##
## main script
##

# use args to determine in/out files
args = getArgs()
baseCsvFile = getBaseCsvFile()
cypherTemplateFile = getCypherTemplateFile()
batchSize = args.batch_size
csvRows = int(args.csv_rows)

print("-------------------------------------------------------------------------------------------------------------------------------------")
print(" Admiral ID PoC: Data Importer:  (1) Generating source CSV file: rows [" + str(csvRows) + "] baseFile [" + baseCsvFile + "] ...")
print("-------------------------------------------------------------------------------------------------------------------------------------\n\n")

# create the CSV file (based on required number of rows)
csvFile = generateCsvData(baseCsvFile, csvRows)

# create the cypher file with actual values replacing placeholders
cypherImportFile = createCypherFile(cypherTemplateFile, csvFile, batchSize)

# now execute the cypher commands 

# 1 - empty the DB
runBashCommand("cypher-shell 'match (n) detach delete n'")

# 2 - perform the import
start_time = timeit.default_timer()
runBashCommand("cat " + cypherImportFile + " | cypher-shell")
elapsed = timeit.default_timer() - start_time
print "elapsed time : ", elapsed

# 3 - check we've read in expected data
runBashCommand("cypher-shell 'match (p) return count(p)'")


# housekeeping ....
#os.remove(csvFile)
#os.remove(cypherImportFile)

print(" Done !! \n\n")

