# Contribot

Contribot is a lightweight Spring Boot utility designed to help developers find open-source contribution opportunities. 
It works by running a scheduled poll against public Git repositories (starting with GitHub) using your advanced search criteria, ensuring you get alerted to the freshest issues as soon as they are posted.

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.5.6
- Maven

## 🏁 Getting Started

### Run Locally
```bash

# clone the repository
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot

# build and start the app
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

## 📜 License
Licensed under the Apache License, Version 2.0