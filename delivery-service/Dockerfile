FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/delivery-service.jar ./delivery-service.jar
ENTRYPOINT ["java","-jar","./delivery-service.jar"]
EXPOSE 6003
