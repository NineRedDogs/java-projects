FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/my-kafka-app*.jar my-kafka-app.jar
CMD java ${JAVA_OPTS} -jar my-kafka-app.jar