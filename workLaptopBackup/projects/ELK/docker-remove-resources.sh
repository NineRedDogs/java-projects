#!/bin/bash

echo "Starting docker reset ..."

# clean down everything
echo "Removing containers ..."
docker rm -f claims-mongo
docker rm -f claims-kafka
docker rm -f claims-orch
docker rm -f claims-es
docker rm -f claims-kibana
docker rm -f claims-filebeat-setup
docker rm -f claims-metricbeat-setup
docker rm -f claims-packetbeat-setup
docker rm -f claims-metricbeat
docker rm -f claims-filebeat
docker rm -f claims-packetbeat


# create backend network
echo "Removing network ..."
docker network rm claims-backend

# create shared volume
echo "Removing volume ..."
docker volume rm ClaimsLogVolume


echo "All done !!"
echo 
