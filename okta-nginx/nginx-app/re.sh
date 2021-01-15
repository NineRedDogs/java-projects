#!/bin/bash

# script to start up nginx-oidc environment

# constants
APP_DIR=$(pwd)

sh app.sh
sh nginx.sh

# make surw we end up back in the start dir
cd $APP_DIR

echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo "nginx:"
echo "   to connect :           $ docker exec -it nginxplus /bin/bash"
echo "   for logs   :           $ docker logs -f nginxplus"
echo " "; echo " -- --  -"
echo "spring app:"
echo "   to connect :           $ docker exec -it nginx-spring-app /bin/bash"
echo "   for logs   :           $ docker logs -f nginx-spring-app"
echo " "; echo " -- --  -"
