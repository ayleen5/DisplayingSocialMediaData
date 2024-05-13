FROM openjdk:17-jdk-alpine

VOLUME /tmp
 
ARG JAR_FILE='target/*.jar'

COPY ${JAR_FILE} LoadersService.jar
 
ENTRYPOINT ["java","-jar","/LoadersService.jar"]