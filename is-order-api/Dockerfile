FROM java:8-jdk-alpine

COPY ./target/is-order-api-1.0-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch is-order-api-1.0-SNAPSHOT.jar'

ENTRYPOINT ["java", "-jar", "is-order-api-1.0-SNAPSHOT.jar"]