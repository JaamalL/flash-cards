FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY server/target/application.jar application.jar

EXPOSE 7777
EXPOSE 9090

ENV SPRING_PROFILES_ACTIVE=development

ENV APPLICATION_PORT=7777
ENV GRPC_PORT=9090

ENTRYPOINT ["java", "-jar", "application.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
