FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/andy-mn*.jar andy-mn.jar
CMD java ${JAVA_OPTS} -jar andy-mn.jar