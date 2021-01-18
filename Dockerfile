FROM adoptopenjdk:11-jre-hotspot

ARG JAR_FILE='target/*boot.jar'
ADD ${JAR_FILE} weather-service.jar

# Run the jar
ENTRYPOINT ["java", "-Dspring.profiles.active=pr","-jar","/weather-service.jar"]
