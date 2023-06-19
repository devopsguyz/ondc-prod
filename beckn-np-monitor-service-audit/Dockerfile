FROM java:11
# copy the packaged jar file into our docker image
COPY beckn-np-monitor-1.0.jar /app.jar
# set the startup command to execute the jar
CMD ["java", "-jar", "/app.jar"]