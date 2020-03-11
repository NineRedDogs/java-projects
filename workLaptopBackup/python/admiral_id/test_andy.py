import unittest

from andy_neo import determineNumScoreFields

class TestSum(unittest.TestCase):
    
    def testSum(self):
        exp=6
        assert determineNumScoreFields() == 4, "Should be " + exp


# start
if __name__ == "__main__":
    unittest.main(argv=["-i","fred"])
    print("Everything passed")
    
    