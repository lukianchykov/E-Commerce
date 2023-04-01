FROM openjdk:11-jdk

# Set the working directory
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Start the application
CMD ["java", "-jar", "app.jar"]
