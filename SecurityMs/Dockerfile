FROM openjdk:17-jdk-alpine

VOLUME /tmp
 
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} SecurityMs-0.0.1-SNAPSHOT.jar
 
ENTRYPOINT ["java","-jar","/SecurityMs-0.0.1-SNAPSHOT.jar"]
 