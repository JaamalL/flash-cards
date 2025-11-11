FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY server/target/application.jar application.jar

EXPOSE 7777
EXPOSE 9090

ENV SPRING_PROFILES_ACTIVE=development
ENV APPLICATION_PORT=7777
ENV JAVA_OPTS="-Xms1536m -Xmx2048m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
