FROM docker.elastic.co/beats/metricbeat:7.4.0
USER root
RUN chmod -w /usr/share/metricbeat/metricbeat.yml
COPY metricbeat.yml /usr/share/metricbeat/metricbeat.yml
RUN chown root:metricbeat /usr/share/metricbeat/metricbeat.yml
USER metricbeat
