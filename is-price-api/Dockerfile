FROM java:8-jdk-alpine

COPY ./target/is-price-api-1.0-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch is-price-api-1.0-SNAPSHOT.jar'

ENTRYPOINT ["java", "-jar", "is-price-api-1.0-SNAPSHOT.jar"]