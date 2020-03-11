FROM docker.elastic.co/beats/filebeat:7.4.0
USER root
RUN chmod -w /usr/share/filebeat/filebeat.yml
COPY filebeat.yml /usr/share/filebeat/filebeat.yml
RUN chown root:filebeat /usr/share/filebeat/filebeat.yml
USER filebeat
