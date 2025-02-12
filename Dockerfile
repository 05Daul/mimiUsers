FROM gradle:8.11.1-jdk17 AS build
# 소스코드를 복사할 작업디렉토리를 생성
WORKDIR /myapp
# 호스트머신에 소스코드를 이미지작업 디렉토리로 복사
COPY . /myapp

# gradlew를 실행할 수 있는 권한을 추가
RUN chmod +x /myapp/gradlew

#COPY src /myapp/src
RUN /myapp/gradlew clean build --no-daemon -x test

# 자바를 실행하기 위한 작업
FROM openjdk:17-alpine
WORKDIR /myapp
# 프로젝트빌드로 생성된 jar파일을 런타임이미지로 복사
COPY --from=build /myapp/build/libs/*SNAPSHOT.jar /myapp/mimiuser.jar
EXPOSE 5679
ENTRYPOINT ["java", "-jar", "/myapp/mimiuser.jar"]