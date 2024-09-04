# 1. 베이스 이미지 지정
FROM eclipse-temurin:21-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar /app/user-service.jar

# 4. 도커는 application-dev.yml 사용
ENV SPRING_PROFILES_ACTIVE=dev

# 5. JAR 파일 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]