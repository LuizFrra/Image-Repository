FROM openjdk:11.0-jdk-slim as build

WORKDIR /imageRepo-build

COPY ./ /imageRepo-build

RUN ls -al

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

RUN ./mvnw package

WORKDIR /imageRepo

RUN cp /imageRepo-build/target/*.jar /imageRepo/app.jar

RUN rm -rf /imageRepo-build

RUN ls -al

ENTRYPOINT [ "java", "-jar", "/imageRepo/app.jar" ]