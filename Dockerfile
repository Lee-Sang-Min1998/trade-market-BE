FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /workspace/app

COPY gradle gradle

COPY gradlew .

COPY gradlew.bat .

COPY build.gradle build.gradle

COPY settings.gradle .

COPY src src

RUN dos2unix ./gradlew

RUN chmod +x ./gradlew

RUN ./gradlew clean build --warning-mode all

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /workspace/app

COPY --from=builder ./workspace/app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]