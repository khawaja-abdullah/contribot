# Contribot

Contribot is a lightweight Spring Boot utility that periodically checks GitHub for new open issues matching a configurable search string and notifies you.  
Great for developers who want to discover “good first issues” or other contribution opportunities quickly.

## ✨ Features (initial version)
- Polls GitHub’s public REST API for issues matching a keyword.
- Logs matching issues to the console (email/Slack/webhook support planned).
- Configurable search query and polling interval.

## 🚀 Tech Stack
- Java 25
- Spring Boot 3.5.6
- Maven

## 🏁 Getting Started

### Run Locally
```bash

# clone the repository
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot

# build and start the app
mvn clean install
mvn spring-boot:run
```

## 📜 License
Licensed under the Apache License, Version 2.0