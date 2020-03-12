#!/bin/bash

# build file beat image
echo "Building file-beat image ....."
cd /home/agrahame/dev/sandbox/ELK/file-beat-config
docker build -t andy-filebeat .

# build metric beat image
echo "Building metric-beat image ....."
cd /home/agrahame/dev/sandbox/ELK/metric-beat-config
docker build -t andy-metricbeat .

# build packet beat image
echo "Building packet-beat image ....."
cd /home/agrahame/dev/sandbox/ELK/packet-beat-config
docker build -t andy-packetbeat .

echo; echo "All done ....."; echo;echo
