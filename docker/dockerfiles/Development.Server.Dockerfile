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

ENV JAVA_OPTS="\
-Xms3g \
-Xmx3g \
-XX:+UseG1GC \
-XX:MaxGCPauseMillis=50 \
-XX:G1HeapRegionSize=4m \
-XX:ParallelGCThreads=2 \
-XX:ConcGCThreads=2 \
-XX:InitiatingHeapOccupancyPercent=20 \
-XX:G1ReservePercent=10 \
-XX:+AlwaysPreTouch \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseDynamicNumberOfGCThreads \
-XX:+DisableExplicitGC \
-XX:+TieredCompilation \
-XX:+OptimizeStringConcat \
-XX:+UseStringDeduplication \
-XX:+PerfDisableSharedMem \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/tmp/heapdump.hprof \
-Djdk.virtualThreadScheduler.parallelism=16 \
-Djdk.virtualThreadScheduler.minRunnable=32 \
-Djdk.virtualThreadScheduler.maxPoolSize=256 \
-Djdk.tracePinnedThreads=short"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
