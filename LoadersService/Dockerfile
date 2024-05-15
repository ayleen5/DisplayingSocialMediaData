FROM openjdk:17-jdk-alpine

VOLUME /tmp
 
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} LoadersService-0.0.1-SNAPSHOT.jar
 
ENTRYPOINT ["java","-jar","/LoadersService-0.0.1-SNAPSHOT.jar"]