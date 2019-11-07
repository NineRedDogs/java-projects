#!/bin/bash

# script to build from scratch the nginx-oidc container

# constants
APP_DIR=$(pwd)
NGINX_DIR="$APP_DIR/nginx"
echo "NGINX directory : $NGINX_DIR"

# clean up old containers/resources
docker container rm -f nginxplus-oidc

# set up the nginx image
cd $NGINX_DIR

# build the nginx image (with trial creds)
docker build --no-cache -t andy-nginxplus-from-scratch .
echo " "; echo " -- Built nginx+ OIDC  image : andy-nginxplus-from-scratch"; echo " "; echo " -- --  -"

# start nginx + oidc
docker run -d --name nginxplus-oidc -p 8311:8311 -p 8000:80 -p 8010:8010 andy-nginxplus-from-scratch

echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo "nginx:"
echo "   to connect :           $ docker exec -it nginxplus-oidc /bin/bash"
echo "   for logs   :           $ docker logs -f nginxplus-oidc"
echo " "; echo " -- --  -"