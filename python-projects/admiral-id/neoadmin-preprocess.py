import csv, sys, argparse, os, hashlib, time, Levenshtein

from datetime import datetime
from Levenshtein._levenshtein import distance, jaro, jaro_winkler

startTime = time.time()

goodMatchScore = 0.5
#goodMatchScore = 0.00

origCsvRowScoringData = {}
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

#--- composite columns
col_fullnameDob=21
col_surnamePostcode=22

# 
columnId=0
columnText=1
columnIsSuperField=2
columnIsScoreField=3
columnWeighting=4
columnLevThreshold=5

csvFieldDefinitions = [
#--- field                 header             super   score   weight  levenshtein
#---  ID                    text              field   field   score   Threshold
   [ col_rowID,            'RowID',           False,  False                  ],
   [ col_productName,      'PolType',         False,  False                  ],
   [ col_productReference, 'PolicyNo',        True,   False                  ],
   [ col_forename,         'ForeName',        False,  True,     2,    0.75   ],
   [ col_surname,          'Surname',         False,  True,     2,    0.75   ],
   [ col_address1,         'Addr1',           False,  False                  ],
   [ col_address2,         'Addr2',           False,  False                  ],
   [ col_address3,         'Addr3',           False,  False                  ],
   [ col_postcode,         'Postcode',        False,  False                  ],
   [ col_dateOfBirth,      'DOB',             False,  True,     3,    0.85   ],
   [ col_mobile,           'mobile',          True,   True,     2,    0.9    ],
   [ col_email,            'eMail',           True,   True,     1,    0.9    ],
   [ col_regNum,           'RegNo',           True,   False                  ],
   [ col_dln,              'DrvLicNo',        True,   True,     4,    0.9    ],
   [ col_deviceId,         'DeviceID',        True,   False                  ],
   [ col_abiCode,          'ABICode',         False,  False                  ],
   [ col_alfKey,           'ALFKey',          True,   False                  ],
   [ col_dateInception,    'Date_Inc',        False,  False                  ],
   [ col_dateExpiry,       'Date_Exp',        False,  False                  ],
   [ col_dateOrigin,       'OrinDate',        False,  False                  ],
   [ col_dateCancellation, 'CanDate',         False,  False                  ],
#--- composite columns 
   [ col_fullnameDob,      'FullnameDob',     True,   False                  ],
   [ col_surnamePostcode,  'SurnamePostcode', True,   False                  ]
]

# score = (lev_total * field_weighting) summed for ALL matched fields / total weighting for matched fields    
# confidence = similarityWeighting of matchedFields / similarityWeighting of ALL fields 

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
    
def oldRemoveDuplicateRows(file):
    # we wan't to preserve header i.e. temporarily remove from sort
    print("removing duplicates for " + file)
    os.system("head -n 1 " + file + " > tmp;tail -n +2 " + file + " | sort -u >> tmp; mv tmp " + file)
    
def removeDuplicateRows(file, headerRowPresent=True, uniqueOnColumnZero=False):
    with open(file, "r") as inFile:
        if (headerRowPresent == True):
            headers = inFile.readline()
        sortedLines = sorted(set(inFile))
    inFile.close()
    
    with open(file, "w") as outFile:
        if (headerRowPresent == True):
            outFile.write(headers)
            
        if (uniqueOnColumnZero == True):
            prevColZeroValue = ""
            for outLine in sortedLines:
                currColZero = outLine.split(",")[0]
                if (currColZero != prevColZeroValue):
                    outFile.write(outLine)
                prevColZeroValue = currColZero
        else:
            outFile.writelines(sortedLines)
    outFile.close()
    

def groupSuperFieldsInit():
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsSuperField] == True):
            matchMaps[csvField[columnText]] = {}

def matchSuperFields(row, index):
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsSuperField] == True):
            matchSuperField(csvField[columnText],csvField[columnId],row, index)
        
def getNumSuperFields():
    numSuperfields=0
    print("Superfields:")
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsSuperField] == True):
            numSuperfields += 1
            print("[" + str(csvField[columnId]) + "] " + csvField[columnText])
    return numSuperfields
            
def getNumScoreFields():
    numScorefields=0
    print("Scorefields:")
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsScoreField] == True):
            numScorefields += 1
            print("[" + str(csvField[columnId]) + "] " + csvField[columnText] + " w(" + str(csvField[columnWeighting]) + ")")
    return numScorefields

def getTotalScoreWeighting():
    totalScoreWeighting=0
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsScoreField] == True):
            totalScoreWeighting += csvField[columnWeighting]
    return totalScoreWeighting


def adjustConfidenceIfFullnameDobOnlyMatches(matchRow):
    adjust=0.0
    
    fullnameDobColumn=7
    fullnameDob=False
    offset=2 # skip the start/end node fields
    numMatches = 0
    
    for superField in range (0, numSuperfields):
        if (matchRow[superField+offset] == True):
            numMatches += 1
            if (superField == fullnameDobColumn):
                fullnameDob = True

    if ((fullnameDob == True) and (numMatches == 1)):
        adjust = 1.0
    
    return adjust
    
    
def score(matchRow, lookupSuperFieldIndex):
    startRow=origCsvRowScoringData[matchRow[0]]
    endRow=origCsvRowScoringData[matchRow[1]]
    numCommonFields=0.0
    similarityScore=0.0
    numMatchingFields=0.0
    totalWeightingMatchedFields=0.0
    scoreField=0
	
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsScoreField] == True):

            # initialise score for this field
            compareFieldScore = 0.0
            
            # are both fields present in each of the original nodes
            if startRow[scoreField] and endRow[scoreField]:
            	
            	# yep, so compare the contents
                compareFieldScore = round(jaro(startRow[scoreField], endRow[scoreField]), 2)

                # only record a comparison score if its relatively close
                if (compareFieldScore > csvField[columnLevThreshold]): # only record a comparison score if its relatively close
                    compareFieldScore *= csvField[columnWeighting]
                else:
                    compareFieldScore = 0.0 # not enough of a match, but as both fields present, record a low score to dilute the total
                    
                numMatchingFields += 1
                totalWeightingMatchedFields += csvField[columnWeighting]
                similarityScore += compareFieldScore
    
                matchRow.append(compareFieldScore)
            else:
                # both fields not present, no comparison score to be recorded
                matchRow.append(None)
    
            # move on to next score field
            scoreField += 1       

    similarity = round((similarityScore / totalWeightingMatchedFields), 2)
    maxConfidence = (totalScoreWeighting + adjustConfidenceIfFullnameDobOnlyMatches(matchRow))
    confidence = round((totalWeightingMatchedFields / maxConfidence), 2)
    
    matchRow.append(similarity)
    matchRow.append(confidence)
    
    return similarity
	

def matchSuperField(fieldName, fieldIndex, row, hashIndex):
    superValue = row[fieldIndex]
    #rowId = row[hash]
    rowId = hashIndex
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
    headerRow = [None]*(2+numSuperfields) # adding 2 to allow room for start and end node ID
    print('Num Header row columns : ', len(headerRow))
    print('Header row : ', headerRow)
    headerRow[0] = ":START_ID"
    headerRow[1] = ":END_ID"

    tempMatchFilename = outFolder + "/temp_matches.csv"
    tempMatchFile = open(tempMatchFilename, 'w')
    tempMatchWriter = csv.writer(tempMatchFile, lineterminator='\n')

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
                           tempMatchWriter.writerow(matchRow)
                           # matches.append(matchRow)
        sys.stdout.write(" [{}]\n".format(matchCount))
        sys.stdout.flush()
        matchMaps.pop(superfieldname)
    # matches.sort();
    #print  matches;

    tempMatchFile.close()

    removeDuplicateRows(tempMatchFilename, headerRowPresent=False)
    
    print "opening temp match file reader",
    
    tempMatchFile = open(tempMatchFilename, 'r')
    tempReader = csv.reader(tempMatchFile, delimiter=',', quotechar='"')
    print (": DONE")

    lookupSuperFieldIndex = {}
    tuplePosition = 0
    
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsSuperField] == True):
            lookupSuperFieldIndex[csvField[columnText]] = tuplePosition
            headerRow[tuplePosition +2] = csvField[columnText]
            tuplePosition += 1

    for csvField in csvFieldDefinitions:
        if (csvField[columnIsScoreField] == True):
            headerRow.append("score_" + csvField[columnText] + ":float")
        
    headerRow.append("similarity:float")
    headerRow.append("confidence:float")
    print("Headers [", headerRow, "]")
    
    matchWriter.writerow(headerRow)

    print("flattening temp matches")
    currentMatchRow = [None]*(2+numSuperfields)
    index = 0
    for match in tempReader:
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
               	    	
               currentMatchRow = [None]*(2+numSuperfields)
           currentMatchRow[0] = match[0]
           currentMatchRow[1] = match[1]
           currentMatchRow[superfieldindex + 2] = True
    
    if not currentMatchRow[0] == None:
        ### also score
        mScore = score(currentMatchRow, lookupSuperFieldIndex)
        if (mScore > goodMatchScore):
        	matchWriter.writerow(currentMatchRow)
        	

def extractScoringData(origCsvRowData):
    scoreColumn=0
    scoringFields = [None] * numScorefields
    for csvField in csvFieldDefinitions:
        if (csvField[columnIsScoreField] == True):

            # this is a scoring field save, the contents for use in the score phase
            scoringFields[scoreColumn] = origCsvRowData[csvField[columnId]]
            scoreColumn += 1

    return scoringFields


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
numSuperfields = getNumSuperFields()
numScorefields = getNumScoreFields()
totalScoreWeighting = getTotalScoreWeighting()

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
#index = 0
print "Starting write loop"
for index, row in enumerate(reader):
    #index += 1

    if (index %10000 == 0):
        sys.stdout.write("\r{}".format(index))
        sys.stdout.flush()
    # the first field is mapped to :ID which is used for relationships but is not actually imported - hence row[0] is typically listed twice
    if row[col_rowID] == "RowID":
        continue
       
    

    # CLEAN UP PROBLEMS WITH DATA 

    if row[col_productReference] == "Loans":
        # The raw data is missing a decent product id for loans :(
        row[col_productReference] = "Loans_" + personRow[0]
    if row[col_productReference] == '':
       row[col_productReference] = 'MISSING_DATA_' + row[col_rowID]
    row[col_forename] = row[col_forename].lower()
    row[col_surname] = row[col_surname].lower()
    row[col_email] = row[col_email].lower()
    row[col_dln] = row[col_dln].lower()
    row[col_postcode] = row[col_postcode].replace(' ','').lower()
    # there is a leading space in at least one regNum
    row[col_regNum] = row[col_regNum].lower()
    row[col_regNum] = row[col_regNum].strip()

    personRow = ['MD5_PLACEHOLDER',row[col_rowID],row[col_forename],row[col_surname],convertDate(row[col_dateOfBirth]),
				 row[col_mobile],row[col_email],row[col_dln]]
    personHash = hashlib.md5(','.join(map(str, personRow))).hexdigest()             
    personRow[0] = personHash
    addressRow = ['MD5_PLACEHOLDER',row[col_address1],row[col_address2],row[col_address3],row[col_postcode],row[col_alfKey]]
    addressHashData = [row[col_address1],row[col_postcode]]

    # origCsvRowScoringData[int(row[col_rowID])] = row
    #origCsvRowScoringData[personHash] = row
    origCsvRowScoringData[personHash] = extractScoringData(row)

    addressRow[0] = hashlib.md5(','.join(addressHashData)).hexdigest()
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
    # else:
    #     if not lastPersonRow[7] and personRow[7]:
    #         lastPersonRow[7] = personRow[7]
    #     if not personRow[7] and lastPersonRow[7]:
    #         personRow[7] = lastPersonRow[7]
    #     if (not (hashlib.md5(','.join(map(str,personRow))).hexdigest() == hashlib.md5(','.join(map(str,lastPersonRow))).hexdigest())):
    #         print personRow
    #         print lastPersonRow
    #         sys.exit("*********ERROR Non matching person sharing rowid******")
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

    matchSuperFields(row,personHash)
    
print "\nEND WRITE LOOP"
writeMatches(matchWriter)

closeWriters()

print ""
print("files closed")

removeDuplicateRows(outFolder + "/vehicle.csv")
removeDuplicateRows(outFolder + "/device.csv")
removeDuplicateRows(outFolder + "/product.csv")
removeDuplicateRows(outFolder + "/address.csv", uniqueOnColumnZero=True)
removeDuplicateRows(outFolder + "/product_vehicle.csv")
removeDuplicateRows(outFolder + "/product_device.csv")
removeDuplicateRows(outFolder + "/person.csv")
removeDuplicateRows(outFolder + "/person_address.csv")

endTime = time.time()

elapsed = endTime - startTime

print(" Done in " + str(elapsed) + " seconds!! \n\n")

