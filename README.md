# README

## Project Details

This project is a Java-based application developed using the Micronaut framework and built with Gradle. It uses Liquibase for database schema version control.

The application includes user management features, with the ability to insert different vehicles' data into a database. The database changes are managed using Liquibase changesets, which are XML files located in the `db/changelog` directory.

## How to Run the Project

This application is containerized using Docker, which means you can run it in any environment that supports Docker.

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Build the Docker image by running the command `docker build -t my-app .`.
4. Run the Docker container by running the command `docker run -p 8080:8080 my-app`.

To run the application using Docker Compose, ensure you have a `docker-compose.yml` file in your project directory. Then, you can start your application with the command `docker-compose up`.

## Accessing Swagger UI

Swagger UI provides a visual interface for interacting with the API's resources. It allows you to try out the API methods directly from your browser.

To access Swagger UI:

1. Start the application.
2. Open a web browser and navigate to `http://localhost:8080/swagger-ui/`.

## API Documentation

The API documentation is available through Swagger UI. Here you can find detailed information about the API's endpoints, request parameters, and response formats.

## Database Changes

Database changes are managed using Liquibase. The changesets are located in the `db/changelog` directory. Each changeset represents a set of changes to the database schema.

To apply the changesets, Liquibase must be run. This is typically done automatically when the application starts.

## Contributing

Contributions are welcome. Please make sure to update tests as appropriate and adhere to the coding standards used throughout the project.
