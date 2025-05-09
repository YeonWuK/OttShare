# 1단계: 빌드 스테이지
FROM --platform=linux/amd64 gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar

# 2단계: 실행 스테이지
FROM --platform=linux/amd64 openjdk:17-jdk-slim
WORKDIR /app

# 앱 복사
COPY --from=build /app/build/libs/ottShare-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources/application.yml ./application.yml

# wait-for-it.sh 복사
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
CMD ["java", "-jar", "/app/app.jar"]