'''
Created on 3 May 2019

@author: agrahame
'''


# 
columnId=0
columnText=1
columnIsSuperField=2
columnIsScoreField=3
columnWeighting=4
columnLevThreshold=5

# columns
col_rowID=0
col_productName=1
col_productReference=2
col_forename=3 
col_surname=4 
col_address1=5
col_address2=6
col_address3=7
col_postcode=8
col_dateOfBirth=9 
col_mobile=10
col_email=11
col_regNum=12
col_dln=13
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


