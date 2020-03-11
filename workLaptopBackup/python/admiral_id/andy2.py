import csv, sys, argparse, os, hashlib, time

from datetime import datetime
from Levenshtein._levenshtein import jaro

class andy2(object):

def getArgs():
    argParser = argparse.ArgumentParser(description='Admiral ID CSV parser')
    argParser.add_argument('-i','--input', help='Input file name', required=True)
    argParser.add_argument('-o','--output', help='Output file name', required=False)
    args = argParser.parse_args()
    return args

def openWriter(folder, prefix):
    csvFile = open(folder + '/' + prefix +'.csv', 'w')
    writer = csv.writer(csvFile , lineterminator='\n')
    # print("open " + prefix + " writer")
    openFiles.append(csvFile);
    return writer

## main script
##

# use args to determine in/out files
args = getArgs()
startTime = time.time()
inFile = getInFile()
outFolder = inFile.split(".csv")[0]
# outFile = getOutFile()

if not os.path.exists(outFolder):
    os.makedirs(outFolder)
print("-----------------------------------------------------------------")
print(" Normalise data [" + inFile + "] output [" + outFolder + "] ...")
print("-----------------------------------------------------------------")

groupSuperFieldsInit()
numSuperfields = determineNumSuperFields()
numScorefields = determineNumScoreFields()
totalScoreWeighting = determineTotalScoreWeighting()

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

print ("Opened the following files: " + str(map(lambda aFile: aFile.name,openFiles)))

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

    personRow = ['MD5_PLACEHOLDER',row[col_rowID],row[col_forename],row[col_surname],convertDate(row[col_dateOfBirth]),
                 row[col_mobile],row[col_email],row[col_dln]]
    personHash = hashlib.md5(','.join(map(str, personRow))).hexdigest()             
    personRow[0] = personHash

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

