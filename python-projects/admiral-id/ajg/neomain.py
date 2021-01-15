import argparse

from preprocessor import Preprocessor

def getArgs():
    argParser = argparse.ArgumentParser(description='Admiral ID CSV parser')
    argParser.add_argument('-i','--input', help='Input file name', required=True)
    args = argParser.parse_args()
    return args
    



##
##
## main script
##
## main script
##

# use args to determine in/out files
args = getArgs()

neoP = Preprocessor(args.input)
neoP.processCsv()


# outFile = getOutFile()


