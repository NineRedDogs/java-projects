#!/bin/bash

echo " "; echo " -- --  -"
docker rm -f nginxplus

echo " "; echo " -- --  -"
docker build --no-cache -t ajg-nginxplus-centos .

echo " "; echo " -- --  -"
docker run --name nginxplus -p 8000:80 -p 8010:8010 -d ajg-nginxplus-centos


echo " "; echo " -- --  -"
echo "to connect :           $ docker exec -it nginxplus /bin/bash"
echo " "; echo " -- --  -"
