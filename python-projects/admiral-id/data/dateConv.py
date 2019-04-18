import csv, sys, argparse, os

from datetime import datetime

def convertDate(sourceDate):
#    print("Converting date : ", sourceDate)
    convertedDate = ""
    if sourceDate != "":
        convertedDate = datetime.strptime(sourceDate, "%Y-%m-%d").date()
#        print("Converted date (1)  : ", convertedDate)
        # 
        # TODO: pass in pivotDate (instead of hardcoding here) for types of date, e.g. policy dates vs birth dates
        if convertedDate.year > 2025:
            convertedDate = convertedDate.replace(year=convertedDate.year-100)
        sourceDate = convertedDate.strftime('%d/%m/%y')
#    print("Converted date (2)  : ", convertedDate)
#    print("Converted date (3)  : ", sourceDate)
    return sourceDate

def getArgs():
    argParser = argparse.ArgumentParser(description='Admiral ID CSV parser')
    argParser.add_argument('-i','--input', help='Input file name', required=True)
    argParser.add_argument('-o','--output', help='Output file name', required=False)
    args = argParser.parse_args()
    return args

def fileExists(fName):
   exists = os.path.isfile(fName)
   if not exists:
      print(fName, " does not exist")
   else:
      print(fName, " does exist")

def getInFile():
#    print("Input file:  [", args.input, "]")
    if os.path.isfile(args.input):
        inFile = args.input
    else:
        print("input file ", args.input, " DOES NOT exist, exitting")
        quit()
    return inFile

def getOutFile():
    if args.output:
       outFile = args.output
    else: 
       # Output file NOT provided, using input file as base
       outFile = args.input + "_PARSED"
    return outFile


##
##
## main script
##

# use args to determine in/out files
args = getArgs()
inFile = getInFile()
outFile = getOutFile()

print("------------------------------------------------------------------------------------------------------------")
print(" Admiral ID PoC: CSV parser:  parsing: input [" + inFile + "] output [" + outFile + "] ...")
print("------------------------------------------------------------------------------------------------------------\n\n")

with open(inFile, 'r') as source:
    with open(outFile, 'w') as result:
        writer = csv.writer(result, lineterminator='\n')
        reader = csv.reader(source, delimiter=',', quotechar='"')

        # just copy over headers row
        result.write(source.readline())

        # parse the actual data of the csv file
        for row in reader:

            # convert the date columns
            #
            #  - DoB (column 9)
            row[9] = convertDate(row[9]) 
            #
            #  - DateInc (column 17)
            row[17] = convertDate(row[17]) 
            #
            #  - DateExp (column 18)
            row[18] = convertDate(row[18]) 
            #
            #  - OrinDate (column 19)
            row[19] = convertDate(row[19]) 
            #
            #  - CanDate (column 20)
            row[20] = convertDate(row[20]) 

            #
            # all conversions done...
            #
            # write the adjusted data to the output file
            writer.writerow(row)

source.close()
result.close()

print(" Done !! \n\n")

