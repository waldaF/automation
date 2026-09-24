FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY . .

ENTRYPOINT ["mvn", "clean", "verify"]
CMD ["-P", "dev", "-Dfailsafe.suites=src/test/resources/suites/Rest.xml"]
