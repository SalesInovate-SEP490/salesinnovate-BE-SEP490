FROM openjdk:17

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} iLead-service.jar

ENTRYPOINT ["java","-jar","iLead-service.jar"]

EXPOSE 8082