#!/bin/bash

if [ -z "$1" ]; then
   echo "HELP:"
   echo "$0 <folder> - where folder containers normalised data files in neo-admin format";
   exit 1;

fi
FOLDER=$1

neo4j-admin import --nodes:Person="$FOLDER/person.csv" --nodes:Product="$FOLDER/product.csv" --relationships:IS_COVERED_BY="$FOLDER/person_product.csv" --nodes:Vehicle="$FOLDER/vehicle.csv" --relationships:DRIVES="$FOLDER/product_vehicle.csv" --nodes:Device="$FOLDER/device.csv" --relationships:USED="$FOLDER/product_device.csv" --nodes:Address="$FOLDER/address.csv" --relationships:RESIDES_AT="$FOLDER/person_address.csv" --relationships:MATCHES="$FOLDER/matches.csv"


