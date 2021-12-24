FROM openjdk:8-alpine

COPY target/uberjar/login-app.jar /login-app/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/login-app/app.jar"]
