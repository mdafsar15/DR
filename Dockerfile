FROM openjdk:8
EXPOSE 8091
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} blockchainservice-0.0.1.jar
ENTRYPOINT ["java","-jar","/blockchainservice-0.0.1.jar"]
