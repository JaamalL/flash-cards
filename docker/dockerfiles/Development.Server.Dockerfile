# Builder
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY server/pom.xml .
COPY server/mvnw .
COPY server/.mvn .mvn

RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B

COPY server/src/ ./src

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean package -DskipTests


# Runner
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar application.jar

EXPOSE 7777
EXPOSE 9090

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
