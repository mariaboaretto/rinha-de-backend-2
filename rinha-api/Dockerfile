# Base image
FROM eclipse-temurin:17-jdk-focal

# Setting working directory
WORKDIR /app

# Copying Maven wrappers and pom file
COPY .mvn/ ./.mvn/
COPY mvnw pom.xml ./

# Resolving all project dependencies
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]