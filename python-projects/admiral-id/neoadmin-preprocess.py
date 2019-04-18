import csv, sys, argparse, os, hashlib, time

from datetime import datetime

startTime = time.time()

matchMaps = {}
openFiles = []


# name, fieldIndex tuple
superFieldTuples = [
	('dln',13),
	('mobile',10),
	('email',11),
	('alfKey',16),
	('deviceID',14),
	('productReference',2),
	('fullnameDOB',21),
	('surnamePostcode',22),
	('regNum',12),
	]

def convertDate(sourceDate):
    convertedDate = ""
    if sourceDate != "":
        convertedDate = datetime.strptime(sourceDate, '%d/%m/%y').date()
        # 
        # TODO: pass in pivotDate (instead of hardcoding here) for types of date, e.g. policy dates vs birth dates
        if convertedDate.year > 2025:
            convertedDate = convertedDate.replace(year=convertedDate.year-100)
        sourceDate = convertedDate.strftime("%Y-%m-%d")
    return convertedDate

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

def openWriter(folder, prefix):
    file = open(folder + '/' + prefix +'.csv', 'w')
    writer = csv.writer(file, lineterminator='\n')
    # print("open " + prefix + " writer")
    openFiles.append(file);
    return writer

def closeWriters():
    for file in openFiles:
        file.close()
    
def removeDuplicateRows(file):
    # we wan't to preserve header i.e. temporarily remove from sort
    print("removing duplicates for " + file);
    os.system("head -n 1 " + file + " > tmp;tail -n +2 " + file + " | sort -u >> tmp; mv tmp " + file)

def groupSuperFieldsInit():
    for superFieldTuple in superFieldTuples:
        matchMaps[superFieldTuple[0]] = {}

def matchSuperFields(row):
    for superFieldTuple in superFieldTuples:
    	matchSuperField(superFieldTuple[0],superFieldTuple[1],row)


def matchSuperField(fieldName, fieldIndex, row):
    superValue = row[fieldIndex]
    rowId = row[0]
    if superValue == None or superValue == "":
        return
    matchMap = matchMaps[fieldName]
    rowArray = matchMap[superValue] if superValue in matchMap else None
    if rowArray == None:
        rowArray = [rowId]
        matchMap[superValue]=rowArray
        # print (fieldName + ':' + superValue, rowArray)
    else:
        if not rowId in rowArray:
            rowArray.append(rowId)
        #    print (fieldName + ':' + superValue, rowArray)

def writeMatches(matchWriter):

    headerRow = [None]*(2+len(superFieldTuples))
    headerRow[0] = ":START_ID"
    headerRow[1] = ":END_ID"
    

    matches = []
    #1 iterate outer map(superfieldname), 2) iterate inner map (superfield
    for superfieldname in matchMaps.keys():
        sys.stdout.write("collecting matches for superfield {}".format(superfieldname))
        sys.stdout.flush()
        matchMap = matchMaps[superfieldname]
        matchCount = 0
        for superfieldvalue in matchMap:
           rowIDArray = matchMap[superfieldvalue]
           if len(rowIDArray) > 1:
           
               for outerIndex in range(len(rowIDArray)):
                   if outerIndex < (len(rowIDArray) -1): 
                       for innerIndex in range(outerIndex + 1, len(rowIDArray)):
                           matchRow = [rowIDArray[outerIndex],rowIDArray[innerIndex],superfieldname]
                           matchCount += 1
                           matches.append(matchRow)
        sys.stdout.write(" [{}]\n".format(matchCount))
        sys.stdout.flush()
        matchMaps.pop(superfieldname)
    matches.sort();
    #print  matches;

    lookupSuperFieldIndex = {}
    tuplePosition = 0
    for superFieldTuple in superFieldTuples:
        lookupSuperFieldIndex[superFieldTuple[0]] = tuplePosition
        headerRow[tuplePosition +2] = superFieldTuple[0]
        tuplePosition += 1

    matchWriter.writerow(headerRow)
    print("flattening " + str(len(matches)) + " matches")
    
    currentMatchRow = [None]*(2+len(superFieldTuples))
    index = 0
    for match in matches:
       if index % 10000 == 0:
           sys.stdout.write("\r{}".format(index))
           sys.stdout.flush()
       index += 1
       # skip the first two fields which contain start and end row id
       superfieldname = match[2]
       superfieldindex = lookupSuperFieldIndex[superfieldname]
       if match[0] == currentMatchRow[0] and match[1] == currentMatchRow[1]:
           # this row looks similar to last, update last
           currentMatchRow[superfieldindex + 2] = True
       else: 
           # this row looks new, write last and update new
           if not currentMatchRow[0] == None:
               ### score here ????
               matchWriter.writerow(currentMatchRow)
               currentMatchRow = [None]*(2+len(superFieldTuples))
           currentMatchRow[0] = match[0]
           currentMatchRow[1] = match[1]
           currentMatchRow[superfieldindex + 2] = True
    
    if not currentMatchRow[0] == None:
        ### also score
        matchWriter.writerow(currentMatchRow)

## main script
##

# use args to determine in/out files
args = getArgs()
inFile = getInFile()
outFolder = inFile.split(".csv")[0]
# outFile = getOutFile()

if not os.path.exists(outFolder):
	os.makedirs(outFolder)
print("-----------------------------------------------------------------")
print(" Normalise data [" + inFile + "] output [" + outFolder + "] ...")
print("-----------------------------------------------------------------")

groupSuperFieldsInit()

print "opening reader",
sourceFile = open(inFile, 'r')
reader = csv.reader(sourceFile, delimiter=',', quotechar='"')
print (": DONE")


personWriter = openWriter(outFolder,'person')
addressWriter = openWriter(outFolder,'address')
personAddressWriter = openWriter(outFolder,'person_address')
productWriter = openWriter(outFolder, 'product')
personProductWriter = openWriter(outFolder, 'person_product')
vehicleWriter = openWriter(outFolder, 'vehicle')
productVehicleWriter = openWriter(outFolder, 'product_vehicle')
deviceWriter = openWriter(outFolder, 'device')
productDeviceWriter = openWriter(outFolder, 'product_device')
matchWriter = openWriter(outFolder, 'matches')

print ("Opened the following files: " + str(map(lambda file: file.name,openFiles)))

personWriter.writerow([":ID","rowID:string","forename:string","surname:string","dateOfBirth:date","mobile:string","email:string","dln"])
addressWriter.writerow([":ID","address1:string","address2:string","address3:string","postcode:string","alfKey:string"])
personAddressWriter.writerow([":START_ID",":END_ID"])
productWriter.writerow([":ID","productReference:string","productName:string","dateInception:date","dateExpiry:date","dateOrigin:date","dateCancellation:date"])
personProductWriter.writerow([":START_ID",":END_ID"])
vehicleWriter.writerow([":ID","regNum", "abiCode"])
productVehicleWriter.writerow([":START_ID",":END_ID"])
deviceWriter.writerow([":ID","deviceID"])
productDeviceWriter.writerow([":START_ID",":END_ID"])


lastPersonRow=[-1]
lastProductRow=[-1]
index = 0
print "Starting write loop"
for row in reader:
    index += 1

    if (index %10000 == 0):
        sys.stdout.write("\r{}".format(index))
        sys.stdout.flush()
    # the first field is mapped to :ID which is used for relationships but is not actually imported - hence row[0] is typically listed twice
    if row[0] == "RowID":
        continue

    # CLEAN UP PROBLEMS WITH DATA 

    if row[2] == "Loans":
        # The raw data is missing a decent product id for loans :(
        row[2] = "Loans_" + personRow[0]
    if row[2] == '':
       row[2] = 'MISSING_DATA_' + row[0]
    row[11] = row[11].lower()
    row[13] = row[13].lower()
    row[8] = row[8].replace(' ','').lower()
    # there is a leading space in at least one regNum
    row[12] = row[12].strip()

    personRow = [row[0],row[0],row[3],row[4],convertDate(row[9]),row[10],row[11],row[13]]
    addressRow = ['MD5_PLACEHOLDER',row[5],row[6],row[7],row[8],row[16]]
    addressRow[0] = hashlib.md5(','.join(addressRow)).hexdigest()
    productRow = [row[2],row[2],row[1],convertDate(row[17]),convertDate(row[18]),convertDate(row[19]),convertDate(row[20])]
    # annoyingly regnum can't be considered unique as there are examples with same reg and different abiCode
    vehicleRow = [row[12] + "_" + row[15], row[12],row[15]]
    deviceRow = [row[14],row[14]]
    # row[21] = fullname_dateOfBirth
    row.append(row[3].lower() + "_" + row[4].lower() + "_" + row[9])
    # row[22] = surname_postcode
    row.append(row[4].lower() + "_" + row[8])
    
    if personRow[0]!=lastPersonRow[0]:
    	personWriter.writerow(personRow)
        addressWriter.writerow(addressRow)
        personAddressWriter.writerow([personRow[0],addressRow[0]])
    
    if productRow[0]!=lastProductRow[0]:
    	productWriter.writerow(productRow)
        personProductWriter.writerow([personRow[0],productRow[0]])
    if vehicleRow[1] != "":
        vehicleWriter.writerow(vehicleRow)
        #productVehicleWriter.writerow( ["***********"])
        #productVehicleWriter.writerow(vehicleRow)
        #productVehicleWriter.writerow(productRow)
	productVehicleWriter.writerow([productRow[0],vehicleRow[0]])
    if deviceRow[0] != "":
	deviceWriter.writerow(deviceRow)
        productDeviceWriter.writerow([productRow[0],deviceRow[0]])
    
    lastPersonRow = personRow
    lastProductRow = productRow

    matchSuperFields(row)
    
print "\nEND WRITE LOOP"
writeMatches(matchWriter)

closeWriters()

print ""
print("files closed")



removeDuplicateRows(outFolder + "/vehicle.csv")
removeDuplicateRows(outFolder + "/device.csv")
removeDuplicateRows(outFolder + "/product.csv")
removeDuplicateRows(outFolder + "/address.csv")
removeDuplicateRows(outFolder + "/product_vehicle.csv")

endTime = time.time()

elapsed = endTime - startTime

print(" Done in " + str(elapsed) + " seconds!! \n\n")

