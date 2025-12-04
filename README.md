# Contribot

Contribot is a lightweight Spring Boot utility that helps you discover fresh open-source issues suitable for first-time or quick contributions. It periodically queries GitHub using customizable search qualifiers and tracks the last run so you only see new items.

## Features
- GitHub issue discovery with rich search qualifiers
- Configurable page size and initial lookback window
- Persistent last-run tracking on the filesystem to avoid duplicates
- Run locally or on a schedule via GitHub Actions
- Minimal footprint with a single Spring Boot application

## Tech Stack
- Java 21
- Spring Boot 4.0.0
- Maven
- GitHub API (org.kohsuke:github-api)

## Requirements
- Java 21+
- A GitHub Personal Access Token (PAT). Prefer a fine-grained token with:
  - Repository access: All repositories (read-only)
  - Permissions: Issues (read-only) and Metadata (read-only)

## Configuration
Configuration lives in `src/main/resources/application.yml`. These are the relevant properties and defaults:

```yaml
spring:
  application:
    name: contribot
github:
  token: ${GITHUB_TOKEN}                      # GitHub token; prefer setting via env var
  issue-search:
    page-size: 50                             # Results per page
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

You can supply the token in one of two ways (never commit secrets):

- Environment variable (recommended)
  ```bash
  export GITHUB_TOKEN=<YOUR_GITHUB_TOKEN>
  ```
- Application YAML (local/dev only)
  ```yaml
  github:
    token: <YOUR_GITHUB_TOKEN>
  ```

To tweak what Contribot searches for, edit the `github.issue-search.query.qualifiers` list in `application.yml` to use any valid GitHub search qualifiers (e.g., `language:Kotlin`, `label:"help wanted"`, `org:your-org`).

## Running Locally
Clone and start the application:

```bash
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot
./mvnw clean install
./mvnw spring-boot:run
```

When the job runs, it writes last-run metadata to `data/job/issuesearch/state.json`. Deleting this file forces an initial run using the configured lookback window.

## Running on a Schedule (GitHub Actions)
A workflow is included to run background jobs on a schedule:
- `.github/workflows/background-jobs.yml`

Set the `GITHUB_TOKEN` as a repository secret (e.g., `Settings > Secrets and variables > Actions > New repository secret`). The workflow reads the token from the environment and executes the job on the defined cron.

CI is also configured for basic checks:
- `.github/workflows/ci.yml`

## Testing
Run unit and integration tests:
```bash
./mvnw test
```

## Troubleshooting
- 401/403 from GitHub API: ensure your PAT has Issues (read-only) and Metadata (read-only) and that it hasn’t expired.
- No results returned: widen or adjust `github.issue-search.query.qualifiers` in `application.yml`.
- Duplicate issues after config change: remove the state file at `data/job/issuesearch/state.json` to reset last-run tracking.
- Build tool can’t find Java: confirm `java -version` reports 21.

## Roadmap (high-level)
- Support additional providers (GitLab, Bitbucket)
- Email/Slack notifications
- Web UI for results and configuration

## License
Licensed under the Apache License, Version 2.0. See `LICENSE` for details.
