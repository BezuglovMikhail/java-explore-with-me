FROM amazoncorretto:11-alpine-jdk
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9090
#WORKDIR /app
COPY target/*.jar StatsServer.jar
ENTRYPOINT ["java","-jar","/StatsServer.jar"]