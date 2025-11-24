FROM eclipse-temurin:17-jdk
ADD target/transaction-service.jar transaction-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "transaction-service.jar"]
