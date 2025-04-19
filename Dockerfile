FROM openjdk:17

WORKDIR /app

# JAR 파일 복사
COPY build/libs/ottShare-0.0.1-SNAPSHOT.jar app.jar

# wait-for-it.sh 복사 및 실행 권한 부여
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# MySQL 실행 대기 후 Spring Boot 실행
CMD ["/wait-for-it.sh", "mysql-container:3306", "--timeout=30", "--strict", "--", "java", "-jar", "app.jar"]