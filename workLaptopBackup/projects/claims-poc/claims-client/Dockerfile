FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/claims-client*.jar claims-client.jar
CMD java ${JAVA_OPTS} -jar claims-client.jar