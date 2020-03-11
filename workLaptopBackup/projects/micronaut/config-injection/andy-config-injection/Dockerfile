FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/andy-config-injection*.jar andy-config-injection.jar
CMD java ${JAVA_OPTS} -jar andy-config-injection.jar