import argparse

from preprocessor import Preprocessor

def getArgs():
    argParser = argparse.ArgumentParser(description='Admiral ID CSV parser')
    argParser.add_argument('-i','--input', help='Input file name', required=True)
    argParser.add_argument('-o','--output', help='Output file name', required=False)
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

neoP = Preprocessor(args.input, args.output)
neoP.processCsv()


# outFile = getOutFile()


