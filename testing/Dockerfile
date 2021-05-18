FROM amazoncorretto:latest
COPY parent-be/target/*.jar /usr/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=testing","/usr/app/app.jar"]