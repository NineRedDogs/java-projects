FROM docker.elastic.co/beats/packetbeat:7.4.0
USER root
RUN chmod -w /usr/share/packetbeat/packetbeat.yml
COPY packetbeat.yml /usr/share/packetbeat/packetbeat.yml
RUN chown root:packetbeat /usr/share/packetbeat/packetbeat.yml
USER packetbeat
