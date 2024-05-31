# Use a base image
FROM openjdk:17

RUN mkdir -p /app

COPY build/libs/mobiauto-backend-interview-0.1-all.jar mobiauto-backend-interview-0.1-all.jar


# Run the application
CMD ["java", "-jar", "mobiauto-backend-interview-0.1-all.jar"]