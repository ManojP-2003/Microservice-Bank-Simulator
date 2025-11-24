FROM eclipse-temurin:17-jdk
ADD target/eureka-server.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","eureka-server.jar"]
