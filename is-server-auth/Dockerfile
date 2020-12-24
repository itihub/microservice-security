FROM java:8-jdk-alpine

COPY ./target/is-server-auth-1.0-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch is-server-auth-1.0-SNAPSHOT.jar'

ENTRYPOINT ["java", "-jar", "is-server-auth-1.0-SNAPSHOT.jar"]