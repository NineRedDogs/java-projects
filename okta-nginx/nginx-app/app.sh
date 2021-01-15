#!/bin/bash

# script to rebuild from scratch the nginx spring boot app

# before we start ripping down docker containers, make sure the spring project builds
mvn install dockerfile:build
echo " "; echo " -- Built spring-boot-app docker image : andy-nginx/spring-nginx-oidc-app"; echo " "; echo " -- --  -"

# clean up old containers/resources
docker container rm -f nginx-spring-app

# start spring boot app
docker run -d --name nginx-spring-app -p 7777:7777 andy-nginx/spring-nginx-oidc-app:latest

echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo " "; echo " -- --  -"
echo "spring app:"
echo "   to connect :           $ docker exec -it nginx-spring-app /bin/bash"
echo "   for logs   :           $ docker logs -f nginx-spring-app"
echo " "; echo " -- --  -"

