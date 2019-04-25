import csv, sys, argparse, os, hashlib, time, Levenshtein

from datetime import datetime
from Levenshtein._levenshtein import distance, jaro, jaro_winkler

startTime = time.time()

goodMatchScore = 0.8
levenshteinThreshold = 0.66
#goodMatchScore = 0.00

origRowData = {}
matchMaps = {}
openFiles = []

# columns
col_rowID=0
col_productName=1
col_productReference=2
col_forename=3 #score - 2 : 
col_surname=4 #score - 2  : 
col_address1=5
col_address2=6
col_address3=7
col_postcode=8
col_dateOfBirth=9 #score - 2 
col_mobile=10 #score - 2
col_email=11 #score - 1
col_regNum=12
col_dln=13 #score - 3
col_deviceId=14
col_abiCode=15
col_alfKey=16
col_dateInception=17
col_dateExpiry=18
col_dateOrigin=19
col_dateCancellation=20

# score = (lev_total * field_weighting) summed for ALL matched fields / total weighting for matched fields    
# confidence = similarityWeighting of matchedFields / similarityWeighting of ALL fields 

#--- composite columns
col_fullnameDob=21
col_surnamePostcode=22


csvFieldDefinitions = [
	[ col_rowID, 'RowID', True, 2 ], 
[ col_productName, 'PolType', True, 2 ], 
[ col_productReference, 'PolicyNo', True, 2 ], 
[ col_forename, 'ForeName', True, 2 ], 
[ col_surname, 'Surname', True, 2 ], 
[ col_address1, 'Addr1', True, 2 ], 
[ col_address2, 'Addr2', True, 2 ], 
[ col_address3, 'Addr3', True, 2 ], 
[ col_postcode, 'Postcode', True, 2 ], 
[ col_dateOfBirth, 'DOB', True, 2 ], 
[ col_mobile, 'mobile', True, 2 ], 
[ col_email, 'eMail', True, 2 ], 
[ col_regNum, 'RegNo', True, 2 ], 
[ col_dln, 'DrvLicNo', True, 2 ], 
[ col_deviceId, 'DeviceID', True, 2 ], 
[ col_abiCode, 'ABICode', True, 2 ], 
[ col_alfKey, 'ALFKey', True, 2 ], 
[ col_dateInception, 'Date_Inc', True, 2 ], 
[ col_dateExpiry, 'Date_Exp', True, 2 ], 
[ col_dateOrigin, 'OrinDate', True, 2 ], 
[ col_dateCancellation, 'CanDate', True, 2 ], 

# score = (lev_total * field_weighting) summed for ALL matched fields / total weighting for matched fields    
# confidence = similarityWeighting of matchedFields / similarityWeighting of ALL fields 

#--- composite columns
col_fullnameDob
col_surnamePostcode

	]


# name, fieldIndex tuple
superFieldTuples = [
	('dln',col_dln),
	('mobile',col_mobile),
	('email',col_email),
	('alfKey',col_alfKey),
	('deviceID',col_deviceId),
	('productReference',col_productReference),
	('fullnameDOB',col_fullnameDob),
	('surnamePostcode',col_surnamePostcode),
	('regNum',col_regNum),
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

def score(matchRow, lookupSuperFieldIndex):
	startRow=origRowData[int(matchRow[0])]
	endRow=origRowData[int(matchRow[1])]
	numCommonFields=0
	totalCommonFields=0
	
	for superFieldTuple in superFieldTuples:
		aIdx = lookupSuperFieldIndex[superFieldTuple[0]]
		if (matchRow[aIdx+2] == True):
			matchRow[aIdx+2] = 1.0
		else:
			if (matchRow[aIdx+2] == None):
				#print('No match for superfield : ', superFieldTuple[0])
				# compare this field between the rows
				if startRow[superFieldTuple[1]] and endRow[superFieldTuple[1]]:
					#print('both fields exist in original row, so comparing [', startRow[superFieldTuple[1]], '] with [', endRow[superFieldTuple[1]], ']')
					#matchRow[aIdx+2] = round(jaro_winkler(startRow[superFieldTuple[1]], endRow[superFieldTuple[1]]), 2)
					compScore = round(jaro(startRow[superFieldTuple[1]], endRow[superFieldTuple[1]]), 2)
					if (compScore > levenshteinThreshold): # only record a comparison score if its relatively close
						matchRow[aIdx+2] = compScore
					else:
						matchRow[aIdx+2] = 0.0 # not enough of a match, but as both fields present, record a low score to dilute the total
    
	for idx in range(2, len(matchRow)):
		if (matchRow[idx]) or (matchRow[idx] == 0.0):
			numCommonFields += 1
			totalCommonFields += matchRow[idx]
			
	scoreVal = round((totalCommonFields / numCommonFields), 2)
	matchRow.append(scoreVal)
	return scoreVal
	

def matchSuperField(fieldName, fieldIndex, row):
    superValue = row[fieldIndex]
    rowId = row[col_rowID]
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
    headerRow.append("score:float")
    print("Headers [", headerRow, "]")
    
        

    matchWriter.writerow(headerRow)
    print("flattening " + str(len(matches)) + " matches")
    print("1")
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
               mScore = score(currentMatchRow, lookupSuperFieldIndex)
               if (mScore > goodMatchScore):
               	    matchWriter.writerow(currentMatchRow)
               #else:
               #	    print("Bad match score, not writing (", mScore, ") nodes:", currentMatchRow[0], ":", currentMatchRow[1])
               	    	
               currentMatchRow = [None]*(2+len(superFieldTuples))
           currentMatchRow[0] = match[0]
           currentMatchRow[1] = match[1]
           currentMatchRow[superfieldindex + 2] = True
    
    if not currentMatchRow[0] == None:
        ### also score
        mScore = score(currentMatchRow, lookupSuperFieldIndex)
        if (mScore > goodMatchScore):
        	matchWriter.writerow(currentMatchRow)
        else:
        	print("Bad match score, not writing (", mScore, ") nodes:", currentMatchRow[0], ":", currentMatchRow[1])
        	

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

personWriter.writerow([":ID","rowID:string","forename:string","surname:string","dateOfBirth:date","mobile:string","email:string","dln:string"])
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
    if row[col_rowID] == "RowID":
        continue
       
    origRowData[int(row[col_rowID])] = row;

    # CLEAN UP PROBLEMS WITH DATA 

    if row[col_productReference] == "Loans":
        # The raw data is missing a decent product id for loans :(
        row[col_productReference] = "Loans_" + personRow[0]
    if row[col_productReference] == '':
       row[col_productReference] = 'MISSING_DATA_' + row[col_rowID]
    row[col_email] = row[col_email].lower()
    row[col_dln] = row[col_dln].lower()
    row[col_postcode] = row[col_postcode].replace(' ','').lower()
    # there is a leading space in at least one regNum
    row[col_regNum] = row[col_regNum].strip()

    personRow = [row[col_rowID],row[col_rowID],row[col_forename],row[col_surname],convertDate(row[col_dateOfBirth]),
				 row[col_mobile],row[col_email],row[col_dln]]
    addressRow = ['MD5_PLACEHOLDER',row[col_address1],row[col_address2],row[col_address3],row[col_postcode],row[col_alfKey]]
    addressRow[0] = hashlib.md5(','.join(addressRow)).hexdigest()
    productRow = [row[col_productReference],row[col_productReference],row[col_productName],
				  convertDate(row[col_dateInception]),convertDate(row[col_dateExpiry]),
				  convertDate(row[col_dateOrigin]),convertDate(row[col_dateCancellation])]
    # annoyingly regnum can't be considered unique as there are examples with same reg and different abiCode
    vehicleRow = [row[col_regNum] + "_" + row[col_abiCode], row[col_regNum],row[col_abiCode]]
    deviceRow = [row[col_deviceId],row[col_deviceId]]
    # row[col_fullnameDob] = fullname_dateOfBirth
    row.append(row[col_forename].lower() + "_" + row[col_surname].lower() + "_" + row[col_dateOfBirth])
    # row[col_surnamePostcode] = surname_postcode
    row.append(row[col_surname].lower() + "_" + row[col_postcode])
    
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
removeDuplicateRows(outFolder + "/product_device.csv")

endTime = time.time()

elapsed = endTime - startTime

print(" Done in " + str(elapsed) + " seconds!! \n\n")

