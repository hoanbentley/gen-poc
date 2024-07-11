FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY ./target/gen-poc-0.0.1-SNAPSHOT.jar /app/gen-poc.jar

# Expose the application port
EXPOSE 8088

# Default command to run the application
CMD ["java", "-jar", "gen-poc.jar"]
