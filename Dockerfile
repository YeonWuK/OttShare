FROM openjdk:17

WORKDIR /app

# Spring Boot JAR 파일 복사
COPY build/libs/ottShare-0.0.1-SNAPSHOT.jar app.jar

# wait-for-it.sh 복사 및 실행 권한 부여
COPY wait-for-it.sh /wait-for-it.sh

# MySQL이 실행될 때까지 대기 후 Spring Boot 실행
CMD ["mysql:3306", "--", "java", "-jar", "app.jar"]