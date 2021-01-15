'''
Created on 4 May 2019

@author: andrewgrahame
'''
import unittest,csv,os,sys

from preprocessor import Preprocessor


class Test(unittest.TestCase):
    
    def initData(self, numRows=5000, rebuildNeoFiles=True):
        self.sourceData = "/Users/andrewgrahame/dev/git-area/java-projects/python-projects/admiral-id/data/generated_" + str(numRows)
        inFile = self.sourceData + ".csv"     
        self.neoP = Preprocessor(inFile)
  
        if (rebuildNeoFiles):
            self.neoP.processCsv()
            
        self.readInPersonFile()
        self.readInMatchesFile()
        self.readInTempMatchesFile()        

    def setUp(self):
        pass
    
    def readInPersonFile(self):
        self.personRecords = []
        with open(self.neoP.getPersonFname(), 'r') as source:
            personReader = csv.reader(source, delimiter=',', quotechar='"')
            for personRecord in personReader:
                self.personRecords.append(personRecord)                    
        source.close()
        
    def readInMatchesFile(self):
        self.matches = []
        with open(self.neoP.getMatchFname(), 'r') as source:
            matchReader = csv.reader(source, delimiter=',', quotechar='"')
            for match in matchReader:                
                self.matches.append(match)
        source.close()
  
    def readInTempMatchesFile(self):
        self.tempMatches = []
        with open(self.neoP.getTempMatchFname(), 'r') as source:
            matchReader = csv.reader(source, delimiter=',', quotechar='"')
            for match in matchReader:                
                self.tempMatches.append(match)
        source.close()        
        
    def getPersonRecordHash(self, rowId):
        personRecordHash=None
        for personRecord in self.personRecords:
            if (personRecord[1] == rowId):
                personRecordHash = personRecord[0]             
        return personRecordHash
    
    def getPersonRowId(self, personRecordHash):
        rowId=None
        for personRecord in self.personRecords:
            if (personRecord[0] == personRecordHash):
                rowId = personRecord[1]   
        return rowId    

    def getMatches(self, personRecordHash):
        matches = []
        for match in self.matches:                
            if (match[0] == personRecordHash) or (match[1] == personRecordHash):
                # our person hash part of this match, save match
                matches.append(match)
        return matches
    
    def getStrippedMatches(self, personRecordHash, matchedHashes):
        strippedMatchesRowId = set()
        for match in self.tempMatches:
            otherHash = None    
            if (match[0] == personRecordHash):
                otherHash = match[1]
            elif (match[1] == personRecordHash):
                otherHash = match[0]        
            if (otherHash):
                if (otherHash not in matchedHashes):
                    strippedMatchesRowId.add(self.getPersonRowId(otherHash))   
        return sorted(strippedMatchesRowId)
    
    def getMatchingRowIds(self, currPersonHash, matches):
        rowIds = []
        matchedHashes = []
        for match in matches:
            #print("checking match : ", match)
            if (match[0] == currPersonHash):
                otherHash = match[1]
            elif (match[1] == currPersonHash): 
                otherHash = match[0]
                
            matchedHashes.append(otherHash)

            rowId = self.getPersonRowId(otherHash)
            if (rowId):
                rowIds.append(rowId)
        return matchedHashes, rowIds


    def tearDown(self):
        pass


    def testName(self):
        pass
    
    def testMatches(self):
        pass
    
    def checkRowId(self, rowId, expectedMatches, expectedCutMatches):
        # 1. convert rowId to person hash
        personHash = self.getPersonRecordHash(rowId)
        
        # 2. get all match relationships for this person
        matches = self.getMatches(personHash)
        numMatches = len(matches)
        #print("num matches : ", numMatches)
        assert (numMatches == len(expectedMatches)), "Expected " + str(len(expectedMatches)) + " but got " + str(numMatches)
    
        # 3. get other nodes (hash list *and* rowId list) of match relationships
        matchedHashes, rowIds = self.getMatchingRowIds(personHash, matches)
        #for rid in rowIds:
        #    print("got match : rowid: ", rowId, " match: ", rid)
        assert(sorted(rowIds) == sorted(expectedMatches)), "expected " + str(sorted(expectedMatches)) + " --but got-- " + str(sorted(rowIds))
        
        # 4. determine number of matches that were cut during score stage
        strippedMatches = self.getStrippedMatches(personHash, matchedHashes)
        numStrippedMatches = len(strippedMatches)
        
        assert (numStrippedMatches == len(expectedCutMatches)), "Expected " + str(len(expectedCutMatches)) + " but got " + str(numStrippedMatches)

        
        #for strippedMatch in strippedMatches:
        #    print("got cut match : rowid: ", rowId, " cut: ", strippedMatch)
        assert(sorted(strippedMatches) == sorted(expectedCutMatches)), "expected " + str(sorted(expectedCutMatches)) + " --but got-- " + str(sorted(strippedMatches))
                 

    def testPreproc5000(self):
        testRows=5000
        self.initData(numRows=testRows, rebuildNeoFiles=False)
        assert self.neoP.getMatchFname() == self.sourceData + "/matches.csv"
        print("Testing " + str(testRows) + " rows ....")

        rowId = "3817"
        expectedMatches = ["3818", "3816"]
        expectedCutMatches = []
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        
        rowId = "1700"
        expectedMatches = []
        expectedCutMatches = ["1694", "1695", "1696", "1697", "1698", "1699"]
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        
        rowId = "2803"
        expectedMatches = []
        expectedCutMatches = ["2802", "2804", "2805"]
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        pass
    
    def testPreproc5000000(self):
        testRows = 5000000
        self.initData(numRows=testRows, rebuildNeoFiles=False)
        assert self.neoP.getMatchFname() == self.sourceData + "/matches.csv"
        print("Testing " + str(testRows) + " rows ....")        

        rowId = "1000317"
        expectedMatches = ['1000314', '1000315', '1000316']
        expectedCutMatches = ['2710000', '4784195']
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        
        rowId = "2257817"
        expectedMatches = ['2257815', '2257816']
        expectedCutMatches = ['2425784', '2619289']
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        
        rowId = "4209175"
        expectedMatches = ['4209173', '4209174', '4209176']
        expectedCutMatches = ['1531426', '2261375']
        self.checkRowId(rowId, expectedMatches, expectedCutMatches)
        pass
    
    # this method can be used to find rows with interesting numbers of matches/cut matches
    def processRowId(self, rowId):
        # 1. convert rowId to person hash
        personHash = self.getPersonRecordHash(rowId)
        
        # 2. get all match relationships for this person
        matches = self.getMatches(personHash)
        numMatches = len(matches)
    
        # 3. get other nodes (hash list *and* rowId list) of match relationships
        matchedHashes, rowIds = self.getMatchingRowIds(personHash, matches)
        
        # 4. determine number of matches that were cut during score stage
        strippedMatches = self.getStrippedMatches(personHash, matchedHashes)
        numStrippedMatches = len(strippedMatches)
        maxNum = 1
        if (numMatches > maxNum) and (numStrippedMatches > maxNum):
            print("rowid: ", rowId, " num matches : ", numMatches)        
            print("rowid: ", rowId, " num stripped matches : ", numStrippedMatches)

        
        #for strippedMatch in strippedMatches:
        #    print("got cut match : rowid: ", rowId, " cut: ", strippedMatch)
        
    # this method can be used to find rows with interesting numbers of matches/cut matches
    def dontTestGetData(self):
        print("Testing data ...")
        self.initData(rebuildNeoFiles=False)

        for idx in range(4208800, 5000000):
            if (idx %100 == 0):
                #sys.stdout.write("\r{}".format(idx))
                #sys.stdout.flush()
                print("Processing ID : ", idx)
                            
            self.processRowId(str(idx))
    
        pass
                
        
      
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()