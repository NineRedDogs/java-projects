#!/bin/bash

# script to start up kong-oidc environment

# assumptions - followed instructions from: https://docs.konghq.com/enterprise/0.36-x/deployment/installation/free-trial/
#               where the docker kong/postgres images were set up and tagged/named accordingly 

echo " --- "

# before we start ripping down docker containers, make sure the spring project builds
mvn install dockerfile:build

# clean up old containers/resources
docker container rm -f kong-ee
docker container rm -f kong-ee-database
docker container rm -f kong-spring-app
docker network rm kong-ee-net
echo " --- "

# set up trial license env var - sent via email from kong-ee folks
export KONG_LICENSE_DATA='{"license":{"signature":"23b9bb74d855cc81ca6e88575b57b1229a058e4daafe7d699835a18f222ca73da5e3974b6fcb102f9ccbb7885cb71b5292a4035d9e99a34cb97ed7987c552a26","payload":{"customer":"AJG","license_creation_date":"2019-10-16","product_subscription":"Kong Enterprise Edition","admin_seats":"5","support_plan":"None","license_expiration_date":"2019-10-31","license_key":"00Q1K0000187inPUAQ_a1V1K000007KdaEUAS"},"version":1}}'

# show that we are clean
docker container ls

# start to build our environment

# 1. create network
docker network create kong-ee-net


# 2. start postgres
docker run -d --name kong-ee-database \
     --network=kong-ee-net \
     -p 5432:5432 \
     -e "POSTGRES_USER=kong" \
     -e "POSTGRES_DB=kong" \
     postgres:9.6

echo "wait 15 seconds for postgres container to warm up ...."
sleep 15

echo "---"
echo "kong license : $KONG_LICENSE_DATA"
echo "---"

# 3. run kong migrations
docker run --rm --network=kong-ee-net \
   -e "KONG_DATABASE=postgres" \
   -e "KONG_PG_HOST=kong-ee-database" \
   -e "KONG_LICENSE_DATA=$KONG_LICENSE_DATA" \
   kong-ee kong migrations bootstrap

# 4. configure & start kong
docker run -d --name kong-ee --network=kong-ee-net \
     -e "KONG_DATABASE=postgres" \
     -e "KONG_PG_HOST=kong-ee-database" \
     -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
     -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
     -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
     -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
     -e "KONG_ADMIN_LISTEN=0.0.0.0:8001" \
     -e "KONG_PORTAL=on" \
     -e "KONG_LICENSE_DATA=$KONG_LICENSE_DATA" \
     -p 8000:8000 \
     -p 8443:8443 \
     -p 8001:8001 \
     -p 8444:8444 \
     -p 8002:8002 \
     -p 8445:8445 \
     -p 8003:8003 \
     -p 8004:8004 \
     -p 8080:8080 \
     kong-ee

# 5. start up spring docker image
docker run -d --name kong-spring-app \
    --network=kong-ee-net \
    andy-kong/spring-kong-header:latest

# 6. checkpoint - 
echo " --- "
echo " --- "
docker container ls
echo " --- "
echo "Visit the Kong Manager at http://localhost:8002"
echo " --- "
echo " --- "

echo "a quick 10 sec sleep to let everything settled down before we configure kong ...."
sleep 10
echo " --- "; echo " ---"

# 7. enable & configure OIDC kong plugin
curl -i -X POST --url localhost:8001/plugins \
     --data 'name=openid-connect' \
     --data 'config.issuer=https://dev-424995.okta.com/oauth2/default/.well-known/openid-configuration' \
     --data 'config.client_id=0oa1mjs256xq3tKmN357' \
     --data 'config.client_secret=XaewflSG_nlmMm07971BHd69Qj-fTrqdQXtN2xwd' \
     --data 'config.scopes=openid' \
     --data 'config.scopes=email' \
     --data 'config.redirect_uri=http://localhost:8000/'
echo " --- "; echo " ---"
#     --data 'config.upstream_access_token_header=x-userinfo' \
#     --data 'config.upstream_user_info_header=x-userinfo' \

# 8. create upstream & target
curl -i -X POST --url localhost:8001/upstreams/  --data 'name=andy-protected-resources'
echo " --- "; echo " ---"
curl -s -X POST --url localhost:8001/upstreams/andy-protected-resources/targets --data 'target=kong-spring-app:8080'
echo " --- "; echo " ---"

# 9. create service/route
curl -i -X POST --url localhost:8001/services/ --data 'name=andy-protected-service' --data 'url=http://kong-spring-app:8080/'
echo " --- "; echo " ---"
curl -i -X POST --url localhost:8001/services/andy-protected-service/routes --data 'name=andy-mac-route' --data 'hosts[]=localhost'
echo " --- "; echo " ---"

# 10. done ???
echo " "
echo " get off to localhost:8002/default/dashboard to see the kong config has been successfully applied ...."
echo " "
