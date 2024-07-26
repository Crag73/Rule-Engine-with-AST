# Rule Engine Application

## Overview

This application is a simple 3-tier rule engine to determine user eligibility based on attributes like age, department, income, spend, etc. It uses an Abstract Syntax Tree (AST) to represent conditional rules and allows for dynamic creation, combination, and modification of these rules.

## File Structure

- `src/main/java/com/example/ruleengine/RuleEngineApplication.java`: Main class to run the Spring Boot application.
- `src/main/java/com/example/ruleengine/controller/RuleController.java`: REST controller to handle API requests.
- `src/main/java/com/example/ruleengine/model/Node.java`: Class to represent the AST nodes.
- `src/main/java/com/example/ruleengine/service/RuleEngine.java`: Service class containing the logic for creating, combining, and evaluating rules.
- `src/main/resources/static/index.html`: The frontend HTML file for creating rules.
- `src/main/resources/application.properties`: Configuration file for Spring Boot.
- `src/main/resources/schema.sql`: SQL script to create the database schema.
- `src/test/java/com/example/ruleengine/RuleEngineApplicationTests.java`: Unit tests for the application.
- `.gitignore`: Git ignore file to exclude certain files and directories from version control.
- `mvnw` and `mvnw.cmd`: Maven wrapper scripts.
- `pom.xml`: Maven project file with dependencies and build configuration.
- `README.md`: Documentation for the project, including build and run instructions.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- SQLite

### Running the Application

1. Clone the repository.
2. Navigate to the project directory.
3. Run `./mvnw spring-boot:run` to start the application.
4. Open `src/main/resources/static/index.html` in a web browser to interact with the application.

## API Endpoints

- `POST /rules/create`: Creates a rule and returns the corresponding AST.
- `POST /rules/combine`: Combines multiple rules into a single AST.
- `POST /rules/evaluate`: Evaluates a rule against provided data and returns the result.
