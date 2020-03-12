FROM java:8
COPY src/PingPong.java /
RUN javac PingPong.java
EXPOSE 8765
ENTRYPOINT ["java"]
CMD ["PingPong"]
