FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ./stats-server/statistics.jar
ENTRYPOINT ["java","-jar","/stats-server/statistics.jar"]