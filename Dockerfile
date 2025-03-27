# Use the official OpenJDK 17 runtime
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/Ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with the correct entry point
ENTRYPOINT ["java", "-jar", "app.jar"]