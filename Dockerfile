FROM openjdk:20

COPY target/employee-app.jar /app.jar

CMD ["java", "-jar", "/app.jar"]