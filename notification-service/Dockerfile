FROM eclipse-temurin:17-jdk
ADD target/notification-service.jar notification-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
