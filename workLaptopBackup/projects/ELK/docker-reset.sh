#!/bin/bash

# clean down everything
sh ./docker-remove-resources.sh

# build pre-configged beats images 
sh ./build-beats-docker-images.sh

# create backend network
echo "Create network ..."
docker network create claims-backend

# create shared volume
echo "Create log file volume ..."
docker volume create --name ClaimsLogVolume


# start up mongo
echo "Start up mongo ..."
docker run -d --name claims-mongo --network claims-backend -p 27017:27017 mongo

# start up kafka
echo "Start up kafka ..."
docker run -d --name claims-kafka --network claims-backend -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=claims-kafka --env ADVERTISED_PORT=9092 spotify/kafka

# start up elastic search
echo "Start up elastic search ..."
docker run -d --name claims-es --network claims-backend -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.4.0

# start up kibana
echo "Start up kibana ..."
docker run -d --name claims-kibana --network claims-backend --link claims-es:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:7.4.0


echo "Wait for 40 secs for kibana/elastic-search to start up ..."
sleep 40

# set up metricbeats
echo "Set up metricbeat to kibana/elastic-search  ..."
docker run --name claims-metricbeat-setup --network claims-backend docker.elastic.co/beats/metricbeat:7.4.0 setup -E setup.kibana.host=claims-kibana:5601 -E output.elasticsearch.hosts=["claims-es:9200"]

# start up metricbeats
#     to rebuild metricbeat image (with predefined config) 
#        $
#        $ cd /home/agrahame/dev/sandbox/ELK/metric-beat-config
#        $ edit & save metricbeat.yml
#        $ docker build -t andy-metricbeat .
#        $ docker images (to confirm build)
echo "Start up metricbeat ..."
#docker run -d --name claims-metricbeat --network claims-backend docker.elastic.co/beats/metricbeat:7.4.0 -E setup.kibana.host=claims-kibana:5601 -E output.elasticsearch.hosts=["claims-es:9200"]
docker run -d --name claims-metricbeat --network claims-backend andy-metricbeat:latest metricbeat -E setup.kibana.host=claims-kibana:5601 

# set up filebeats
echo "Set up filebeat to kibana/elastic-search  ..."
docker run --name claims-filebeat-setup --network claims-backend docker.elastic.co/beats/filebeat:7.4.0 setup -E setup.kibana.host=claims-kibana:5601 -E output.elasticsearch.hosts=["claims-es:9200"]

# start up filebeats
#     to rebuild filebeat image (with predefined config) 
#        $
#        $ cd /home/agrahame/dev/sandbox/ELK/file-beat-config
#        $ edit & save filebeat.yml
#        $ docker build -t andy-filebeat .
#        $ docker images (to confirm build)
echo "Start up filebeat (andy image) ..."
docker run -d --name=claims-filebeat -v ClaimsLogVolume:/logvol1 --network claims-backend andy-filebeat:latest filebeat -E setup.kibana.host=claims-kibana:5601 


# set up packetbeats
echo "Set up packetbeat to kibana/elastic-search  ..."
docker run --name claims-packetbeat-setup --network claims-backend --cap-add=NET_ADMIN docker.elastic.co/beats/packetbeat:7.4.0 setup -E setup.kibana.host=claims-kibana:5601 -E output.elasticsearch.hosts=["claims-es:9200"]

# start up packetbeats
#     to rebuild packetbeat image (with predefined config) 
#        $
#        $ cd /home/agrahame/dev/sandbox/ELK/packetbbeat-config
#        $ edit & save packetbeat.yml
#        $ docker build -t andy-packetbeat .
#        $ docker images (to confirm build)
echo "Start up packetbeat (andy image) ..."
docker run -d --name claims-packetbeat --network claims-backend --cap-add=NET_ADMIN andy-packetbeat:latest packetbeat -E setup.kibana.host=claims-kibana:5601 

# start up claims-backend-service
#     to rebuild docker image
#        $
#        $ cd /home/agrahame/dev/git-repos/ARD-WHITESANDS/backend/microservice-fnol
#        $ mvn clean package
#        $ docker images (to confirm build)
echo "Start up claims-backend service ..."
docker run -d --name claims-orch -v ClaimsLogVolume:/logvol1 --network claims-backend -p 8080:8080 claims_backend/microservice-fnol:0.1

echo "All done !!"
echo 
