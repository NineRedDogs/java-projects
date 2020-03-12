FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/claims-server*.jar claims-server.jar
CMD java ${JAVA_OPTS} -jar claims-server.jar