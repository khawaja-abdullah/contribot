# Contribot ✨

Contribot is a lightweight Spring Boot utility that helps you discover fresh open-source issues suitable for first-time or quick contributions. It periodically queries GitHub using customizable search qualifiers and tracks the last run so you only see new items.

## Highlights 🔎
- 🔍 Discover new GitHub issues using rich, composable search qualifiers
- ⚖️ Configurable page size and initial lookback window
- ♻️ Persistent last-run tracking on the filesystem to avoid duplicates
- 🕒 Run locally, via cron-like GitHub Actions, or as a container
- 🪶 Minimal footprint with a single Spring Boot application

## Tech Stack 🧰
- ☕ Java 21
- 🌱 Spring Boot 4.0.1
- 🔧 Maven
- 🐙 GitHub API (org.kohsuke:github-api)

## Architecture overview 🧱
- Entry points:
  - ContribotApplication: bootstraps Spring Boot.
  - JobRunner: CommandLineRunner that executes jobs at startup.
- Jobs:
  - GithubIssueSearchJob: queries GitHub with qualifiers, pages results, and emits issue data.
- Persistence:
  - JobExecutionFSRepository: stores the last successful run timestamp in a JSON file at data/job/issuesearch/state.json.
- Configuration:
  - application.yml with github.* and job properties, bound via GithubProperties and clients configured in GithubClientConfig and ObjectMapperConfig.

## Requirements ✅
- ☕ Java 21+
- 🔐 A GitHub Personal Access Token (PAT). Prefer a fine-grained token with:
  - Repository access: All repositories (read-only)
  - Permissions: Issues (read-only) and Metadata (read-only)

## Configuration ⚙️
Configuration lives in src/main/resources/application.yml. Relevant properties and defaults:

```yaml
spring:
  application:
    name: contribot
github:
  token: ${GITHUB_TOKEN}                      # GitHub token; prefer setting via env var
  issue-search:
    page-size: 50                             # Results per page (1-100 supported by GitHub)
    query:
      qualifiers:                             # GitHub search qualifiers
        - is:issue
        - is:open
        - label:"good first issue"
        - language:Java
    job:
      execution-file: data/job/issuesearch/state.json   # Last-run timestamp tracking file
      initial-lookback-hours: 4                         # When no state exists, look back this many hours
```

Supply the token one of two ways (never commit secrets):
- 🌎 Environment variable (recommended)
  ```bash
  export GITHUB_TOKEN=<YOUR_GITHUB_TOKEN>
  ```
- 🧪 Application YAML (local/dev only)
  ```yaml
  github:
    token: <YOUR_GITHUB_TOKEN>
  ```

Adjust search by editing github.issue-search.query.qualifiers to use any valid GitHub search qualifiers, for example:
- language:Kotlin
- label:"help wanted"
- org:your-org

Tip: The builder responsible for query construction is GithubQueryBuilder 💡.

## Running locally ▶️
Clone and start the application:

```bash
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot
./mvnw clean verify
./mvnw spring-boot:run
```

On startup, the job runs and writes last-run metadata to data/job/issuesearch/state.json. Deleting this file forces an initial run using the configured lookback window.

To pass the token inline for a one-off run:
```bash
GITHUB_TOKEN=ghp_xxx ./mvnw spring-boot:run
```

## Containerization 📦
No Dockerfile is included. Build the jar and run it directly, or package your own image if needed:
- Build: ./mvnw -DskipTests package
- Run: java -jar target/contribot-*.jar

## GitHub Actions ⏰
Two workflows are included:
- 🧪 .github/workflows/ci.yml: build and test on pushes/PRs.
- ⏱️ .github/workflows/background-jobs.yml: scheduled run (cron).

Set GH_TOKEN as a repository secret (Settings → Secrets and variables → Actions → New repository secret). The scheduled workflow reads the token, overrides the execution file path to state.json, and caches it between runs to persist last-run tracking.

## Output 🧾
- 🗂️ State: data/job/issuesearch/state.json stores the last successful execution timestamp.
- 📜 Logs: Spring Boot logs include the number of issues found and paging progress.

## Testing 🧪
Run unit and integration tests:
```bash
./mvnw test
```

Useful test classes:
- GithubQueryBuilderTest: validates search query construction.
- GithubServiceTest and IGitProviderServiceTest: mock GitHub client usage.
- JobExecutionFSRepositoryIntTest: ensures filesystem state behavior.
- ObjectMapperConfigTest: verifies JSON mapping setup.

## Troubleshooting 🛠️
- 401/403 from GitHub API: ensure your PAT has Issues (read-only) and Metadata (read-only) and that it hasn’t expired.
- No results returned: widen or adjust github.issue-search.query.qualifiers in application.yml.
- Duplicates after config change: remove data/job/issuesearch/state.json to reset last-run tracking.
- Build tool can’t find Java: confirm java -version reports 21.
- Rate limits: reduce page-size or add qualifiers to narrow results.

## Contributing 🤝
- 🍴 Fork the repository and create a feature branch from main.
- ✅ Run ./mvnw clean verify locally and ensure tests pass.
- 🧪 Add tests for new behavior.
- 📬 Open a PR with a clear description of the change and configuration impact.

## Roadmap (high-level) 🗺️
- Notifications (Email/Slack)
- Support additional providers (GitLab, Bitbucket)
- Web UI for results and configuration

## License 📄
Licensed under the Apache License, Version 2.0. See LICENSE for details.
