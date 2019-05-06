'''
Created on 4 May 2019

@author: andrewgrahame
'''
import unittest,csv

from preprocessor import Preprocessor


class Test(unittest.TestCase):


    def setUp(self):
        #numRows="5000"
        numRows="5000000"
        self.sourceData = "/Users/andrewgrahame/dev/git-area/java-projects/python-projects/admiral-id/data/generated_" + numRows
        inFile = self.sourceData + ".csv"
        self.neoP = Preprocessor(inFile)
        self.neoP.processCsv()
        pass
    
    def getPersonRecordHash(self, rowId):
        personRecordHash=None
        with open(self.neoP.getPersonFname(), 'r') as source:
            personReader = csv.reader(source, delimiter=',', quotechar='"')

            for personRecord in personReader:
                if (personRecord[1] == rowId):
                    personRecordHash = personRecord[0]
                    
        source.close()
        return personRecordHash
    
    def getPersonRowId(self, personRecordHash):
        rowId=None
        with open(self.neoP.getPersonFname(), 'r') as source:
            personReader = csv.reader(source, delimiter=',', quotechar='"')

            for personRecord in personReader:
                if (personRecord[0] == personRecordHash):
                    rowId = personRecord[1]
                    
        source.close()
        return rowId    

    def getMatches(self, personRecordHash):
        matches = []

        with open(self.neoP.getMatchFname(), 'r') as source:
            matchReader = csv.reader(source, delimiter=',', quotechar='"')

            # parse the actual data of the csv file
            for match in matchReader:                
                if (match[0] == personRecordHash) or (match[1] == personRecordHash):
                    # our person hash part of this match, save match
                    matches.append(match)
                  
        source.close()
        return matches
    
    def getStrippedMatches(self, personRecordHash, matchedHashes):
        #strippedMatchesHash = set()
        strippedMatchesRowId = set()

        with open(self.neoP.getTempMatchFname(), 'r') as source:
            matchReader = csv.reader(source, delimiter=',', quotechar='"')

            for match in matchReader:
                otherHash = None
                              
                if (match[0] == personRecordHash):
                    otherHash = match[1]
                elif (match[1] == personRecordHash):
                    otherHash = match[0]
                    
                if (otherHash):
                    if (otherHash not in matchedHashes):
                        #strippedMatchesHash.add(otherHash)
                        strippedMatchesRowId.add(self.getPersonRowId(otherHash))
                  
        source.close()
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
        assert (numMatches == len(expectedMatches))
    
        # 3. get other nodes (hash list *and* rowId list) of match relationships
        matchedHashes, rowIds = self.getMatchingRowIds(personHash, matches)
        #for rid in rowIds:
        #    print("got match : rowid: ", rowId, " match: ", rid)
        assert(sorted(rowIds) == sorted(expectedMatches))
        
        # 4. determine number of matches that were cut during score stage
        strippedMatches = self.getStrippedMatches(personHash, matchedHashes)
        
        #for strippedMatch in strippedMatches:
        #    print("got cut match : rowid: ", rowId, " cut: ", strippedMatch)
        assert(sorted(strippedMatches) == sorted(expectedCutMatches))
                 

    def testPreproc(self):
        self.setUp()
        assert self.neoP.getMatchFname() == self.sourceData + "/matches.csv"

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
    

        
      
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()