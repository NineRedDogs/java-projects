import csv, sys, argparse, os, hashlib, time

from datetime import datetime
from Levenshtein._levenshtein import jaro
from CsvDefinitions import *


class Preprocessor(object):
    
    # #####################################################
    # class constants    
    # #####################################################
    goodMatchScore = 0.5
    
    origCsvRowScoringData = {}
    matchMaps = {}
    openFiles = []
    
    @staticmethod
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
    
    @staticmethod
    def removeDuplicateRows(fileName, headerRowPresent=True, uniqueOnColumnZero=False):
        with open(fileName, "r") as inFile:
            if (headerRowPresent == True):
                headers = inFile.readline()
            sortedLines = sorted(set(inFile))
        inFile.close()
        
        with open(fileName, "w") as outFile:
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

    

    
    
    
    def __init__(self, inFile, outFile):
        if os.path.isfile(inFile):
            self.inFile = inFile
        else:
            print("input file ", inFile, " DOES NOT exist, exiting")
            quit()
            
        if outFile:
            self.outFile = outFile
        else: 
            # Output file NOT provided, using input file as base
            self.outFile = inFile + "_PARSED"
            
        self.outFolder = inFile.split(".csv")[0]
        if not os.path.exists(self.outFolder):
            os.makedirs(self.outFolder)

        self.determineNumSuperFields()
        self.determineNumScoreFields()
        self.determineTotalScoreWeighting()

        
    def getInFile(self):
        return self.inFile
    
    def getOutFile(self):
        return self.outFile
    
    def openWriter(self, folder, prefix):
        csvFile = open(folder + '/' + prefix +'.csv', 'w')
        writer = csv.writer(csvFile , lineterminator='\n')
        # print("open " + prefix + " writer")
        self.openFiles.append(csvFile);
        return writer
    
    def closeWriters(self):
        for openFile in self.openFiles:
            openFile.close()
        
    def groupSuperFieldsInit(self):
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsSuperField] == True):
                self.matchMaps[csvField[columnText]] = {}
    
    def matchSuperFields(self, row, index):
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsSuperField] == True):
                self.matchSuperField(csvField[columnText],csvField[columnId],row, index)
            
    def determineNumSuperFields(self):
        self.numSuperFields=0
        print("Super fields:")
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsSuperField] == True):
                self.numSuperFields += 1
                print("[" + str(csvField[columnId]) + "] " + csvField[columnText])
                
    def determineNumScoreFields(self):
        self.numScoreFields=0
        print("Score fields:")
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsScoreField] == True):
                self.numScoreFields += 1
                print("[" + str(csvField[columnId]) + "] " + csvField[columnText] + " w(" + str(csvField[columnWeighting]) + ")")
    
    def determineTotalScoreWeighting(self):
        self.totalScoreWeighting=0
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsScoreField] == True):
                self.totalScoreWeighting += csvField[columnWeighting]
    
    def adjustConfidenceIfFullnameDobOnlyMatches(self, matchRow):
        adjust=0.0
        
        fullnameDobColumn=7
        fullnameDob=False
        offset=2 # skip the start/end node fields
        numMatches = 0
        
        for superField in range (0, self.numSuperFields):
            if (matchRow[superField+offset] == True):
                numMatches += 1
                if (superField == fullnameDobColumn):
                    fullnameDob = True
    
        if ((fullnameDob == True) and (numMatches == 1)):
            adjust = 1.0
        
        return adjust
        
        
    def score(self, matchRow):
        startRow=self.origCsvRowScoringData[matchRow[0]]
        endRow=self.origCsvRowScoringData[matchRow[1]]
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
        maxConfidence = (self.totalScoreWeighting + self.adjustConfidenceIfFullnameDobOnlyMatches(matchRow))
        confidence = round((totalWeightingMatchedFields / maxConfidence), 2)
        
        matchRow.append(similarity)
        matchRow.append(confidence)
        
        return similarity
        
    
    def matchSuperField(self, fieldName, fieldIndex, row, hashIndex):
        superValue = row[fieldIndex]
        #rowId = row[hash]
        rowId = hashIndex
        if superValue == None or superValue == "":
            return
        matchMap = self.matchMaps[fieldName]
        rowArray = matchMap[superValue] if superValue in matchMap else None
        if rowArray == None:
            rowArray = [rowId]
            matchMap[superValue]=rowArray
            # print (fieldName + ':' + superValue, rowArray)
        else:
            if not rowId in rowArray:
                rowArray.append(rowId)
            #    print (fieldName + ':' + superValue, rowArray)
    
    def writeMatches(self, matchWriter):
        headerRow = [None]*(2+self.numSuperFields) # adding 2 to allow room for start and end node ID
        print('Num Header row columns : ', len(headerRow))
        print('Header row : ', headerRow)
        headerRow[0] = ":START_ID"
        headerRow[1] = ":END_ID"
    
        tempMatchFilename = self.outFolder + "/temp_matches.csv"
        tempMatchFile = open(tempMatchFilename, 'w')
        tempMatchWriter = csv.writer(tempMatchFile, lineterminator='\n')
    
        #1 iterate outer map(superfieldname), 2) iterate inner map (superfield
        for superfieldname in self.matchMaps.keys():
            sys.stdout.write("collecting matches for superfield {}".format(superfieldname))
            sys.stdout.flush()
            matchMap = self.matchMaps[superfieldname]
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
            sys.stdout.write(" [{}]\n".format(matchCount))
            sys.stdout.flush()
            self.matchMaps.pop(superfieldname)
        # matches.sort();
        #print  matches;
    
        tempMatchFile.close()
    
        self.removeDuplicateRows(tempMatchFilename, headerRowPresent=False)
        
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
        currentMatchRow = [None]*(2+self.numSuperFields)
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
                    mScore = self.score(currentMatchRow)
                    if (mScore > self.goodMatchScore):
                        matchWriter.writerow(currentMatchRow)
                               
                currentMatchRow = [None]*(2+self.numSuperFields)
                currentMatchRow[0] = match[0]
                currentMatchRow[1] = match[1]
                currentMatchRow[superfieldindex + 2] = True
        
        if not currentMatchRow[0] == None:
            ### also score
            mScore = self.score(currentMatchRow)
            if (mScore > self.goodMatchScore):
                matchWriter.writerow(currentMatchRow)
                
    
    def extractScoringData(self, origCsvRowData):
        scoreColumn=0
        scoringFields = [None] * self.numScoreFields
        for csvField in csvFieldDefinitions:
            if (csvField[columnIsScoreField] == True):
    
                # this is a scoring field save, the contents for use in the score phase
                scoringFields[scoreColumn] = origCsvRowData[csvField[columnId]]
                scoreColumn += 1
    
        return scoringFields


    def processCsv(self):
        
        startTime = time.time()

        print("-----------------------------------------------------------------")
        print(" Normalise data [" + self.inFile + "] output [" + self.outFolder + "] ...")
        print("-----------------------------------------------------------------")

        self.groupSuperFieldsInit()
        #numSuperFields = determineNumSuperFields()
        #numScoreFields = determineNumScoreFields()
        
        print "opening reader",
        sourceFile = open(self.inFile, 'r')
        reader = csv.reader(sourceFile, delimiter=',', quotechar='"')
        print (": DONE")


        personWriter = self.openWriter(self.outFolder,'person')
        addressWriter = self.openWriter(self.outFolder,'address')
        personAddressWriter = self.openWriter(self.outFolder,'person_address')
        productWriter = self.openWriter(self.outFolder, 'product')
        personProductWriter = self.openWriter(self.outFolder, 'person_product')
        vehicleWriter = self.openWriter(self.outFolder, 'vehicle')
        productVehicleWriter = self.openWriter(self.outFolder, 'product_vehicle')
        deviceWriter = self.openWriter(self.outFolder, 'device')
        productDeviceWriter = self.openWriter(self.outFolder, 'product_device')
        matchWriter = self.openWriter(self.outFolder, 'matches')
        
        print ("Opened the following files: " + str(map(lambda aFile: aFile.name,self.openFiles)))

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
            
            personRow = ['MD5_PLACEHOLDER',row[col_rowID],row[col_forename],row[col_surname],self.convertDate(row[col_dateOfBirth]),
                         row[col_mobile],row[col_email],row[col_dln]]
            personHash = hashlib.md5(','.join(map(str, personRow))).hexdigest()             
            personRow[0] = personHash

            if row[col_productReference] == "Loans":
                # The raw data is missing a decent product id for loans :(
                row[col_productReference] = "Loans_" + personRow[0]        
        
            addressRow = ['MD5_PLACEHOLDER',row[col_address1],row[col_address2],row[col_address3],row[col_postcode],row[col_alfKey]]
            addressHashData = [row[col_address1],row[col_postcode]]
        
            # origCsvRowScoringData[int(row[col_rowID])] = row
            #origCsvRowScoringData[personHash] = row
            self.origCsvRowScoringData[personHash] = self.extractScoringData(row)
        
            addressRow[0] = hashlib.md5(','.join(addressHashData)).hexdigest()
            productRow = [row[col_productReference],row[col_productReference],row[col_productName],
                          self.convertDate(row[col_dateInception]),self.convertDate(row[col_dateExpiry]),
                          self.convertDate(row[col_dateOrigin]),self.convertDate(row[col_dateCancellation])]
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
        
            self.matchSuperFields(row,personHash)
            
        print "\nEND WRITE LOOP"
        self.writeMatches(matchWriter)
        
        self.closeWriters()

        print ""
        print("files closed")
        
        self.removeDuplicateRows(self.outFolder + "/vehicle.csv")
        self.removeDuplicateRows(self.outFolder + "/device.csv")
        self.removeDuplicateRows(self.outFolder + "/product.csv")
        self.removeDuplicateRows(self.outFolder + "/address.csv", uniqueOnColumnZero=True)
        self.removeDuplicateRows(self.outFolder + "/product_vehicle.csv")
        self.removeDuplicateRows(self.outFolder + "/product_device.csv")
        self.removeDuplicateRows(self.outFolder + "/person.csv")
        self.removeDuplicateRows(self.outFolder + "/person_address.csv")
        
        endTime = time.time()
        
        elapsed = endTime - startTime
        
        print(" Done in " + str(elapsed) + " seconds!! \n\n")
                
            
