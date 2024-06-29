FROM maven:3.8.7 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

# Compile and package the application to an executable JAR
RUN mvn package

FROM amazoncorretto:17.0.11

ARG JAR_FILE=user-service-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

CMD java -jar user-service-0.0.1-SNAPSHOT.jar