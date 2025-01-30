FROM openjdk:23
EXPOSE 8080
ADD target/newmarket-api.jar newmarket-api.jar
ENTRYPOINT ["java", "-jar", "/newmarket-api.jar"]