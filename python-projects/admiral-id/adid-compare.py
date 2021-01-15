import pandas as pd

# headerAndData = pd.read_csv("../data/sampleData.csv")
header = pd.read_csv("../data/bigdata_header_raw.csv")
data = pd.read_csv("../data/bigdata_1k.csv", header=None)
data.columns = header.columns
headerAndData = pd.concat([header, data])
# headerAndData=pd.DataFrame(data.values, columns=header.columns)

print("file contains " + str(headerAndData.shape[0]))
print(headerAndData)
columnName = input("Enter Column name filter [Postcode]: ") or "Postcode"
columnValue = input(
    "Enter Column value filter [CF14 6EE]: ") or "CF14 6EE"

filtered = headerAndData[headerAndData[columnName] == columnValue]

print(filtered.T)
