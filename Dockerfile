# Stage 1: Build
FROM amazoncorretto:17 as builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

RUN ls -la /app/build/libs/  # JAR 파일 확인용 명령어


# Stage 2: Run
FROM amazoncorretto:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENV TZ Asia/Seoul

# ENTRYPOINT ["java","-jar","/app/app.jar"]
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=file:/vault/secrets/application.yml"]
