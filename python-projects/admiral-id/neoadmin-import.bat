echo off
cls
IF "%1"=="" GOTO Usage

GOTO GotParam


:Usage
   echo USAGE: neoadmin.bat 'folder' - where folder containers normalised data files in neo-admin format
   goto end


:GotParam
neo4j-admin.bat import --nodes:Person="%1\person.csv" --nodes:Product="%1\product.csv" --relationships:IS_COVERED_BY="%1\person_product.csv" --nodes:Vehicle="%1\vehicle.csv" --relationships:DRIVES="%1\product_vehicle.csv" --nodes:Device="%1\device.csv" --relationships:USED="%1\product_device.csv" --nodes:Address="%1\address.csv" --relationships:RESIDES_AT="%1\person_address.csv" --relationships:MATCHES="%1\matches.csv"



:end
