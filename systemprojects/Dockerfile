# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar from target directory
COPY target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
