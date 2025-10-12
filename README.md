# Contribot

Contribot is a lightweight Spring Boot utility designed to help developers find open-source contribution opportunities. 
It works by running a scheduled poll against public Git repositories (starting with GitHub) using your advanced search criteria, ensuring you get alerted to the freshest issues as soon as they are posted.

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.5.6
- Maven

## 🏁 Getting Started

### Run Locally
Clone the repository
```bash
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot
```

Before running, you must configure a GitHub Authentication Token for the app to access the API.
- Navigate to your GitHub settings to create a new Personal Access Token (PAT) preferably a fine-grained token.
- Ensure the token has the necessary scopes for reading public repository data i.e. All repositories (read-only) with 
specific permissions on Issues (read-only) and Metadata (read-only).
- Once generated, configure the token using one of the following methods, but NEVER commit the token to version control:
```yaml
# Option #1 (application.yml)
github:
  token: <YOUR_GITHUB_TOKEN>
```
```bash
# Option #2 (Environment Variables)
export GITHUB_TOKEN=<YOUR_GITHUB_TOKEN>
```

Build and start the app
```bash
./mvnw clean install # -DskipTests (for skipping build with tests)
./mvnw spring-boot:run
```

## 📜 License
Licensed under the Apache License, Version 2.0