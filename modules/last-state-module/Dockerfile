FROM openjdk:18-bullseye
ARG JAR_FILE=build/libs/last-state-module-*.jar
COPY ${JAR_FILE} last-state-module-application.jar

COPY mongo-ca-certificate.crt mongo-ca-certificate.crt
COPY import-mongo-ca-certificate.sh import-mongo-ca-certificate.sh
RUN ["/bin/bash", "-c", "./import-mongo-ca-certificate.sh"]

ENTRYPOINT ["java","-jar","/last-state-module-application.jar"]