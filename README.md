# Contribot ✨

Contribot is a Spring Boot utility that helps you discover fresh open-source issues suitable for first-time or quick contributions. It periodically queries GitHub using customizable search qualifiers and tracks the last run so you only see new items.

## Highlights 🔎
- 🔍 Discover new GitHub issues using rich, composable search qualifiers
- ⚖️ Configurable page size and initial lookback window
- ♻️ Persistent last-run tracking on the filesystem to avoid duplicates
- 📧 Email notifications via SMTP when new issues are found
- 🕒 Run locally, via cron-like GitHub Actions, or as a container
- 🪶 Minimal footprint with a single Spring Boot application

## Tech Stack 🧰
- ☕ Java 21
- 🌱 Spring Boot 4.0.1 (with Spring Mail for SMTP)
- 🔧 Maven
- 🐙 GitHub API (org.kohsuke:github-api)
- 📮 JavaMail API (for email notifications)
- 📝 Jackson (for JSON serialization)

## Architecture overview 🧱
- **Entry points**: ContribotApplication, JobRunner
- **Jobs**: GithubIssueSearchJob (queries GitHub, pages results, emits data)
- **Notifications**: GitIssueSearchNotificationOrchestrator, INotificationStrategy, IGitIssueFormatterStrategy
- **Persistence**: JobExecutionFSRepository (JSON file state tracking)
- **Configuration**: GithubProperties, GithubClientConfig, ObjectMapperConfig

## Requirements ✅
- ☕ Java 21+
- 🔐 A GitHub Personal Access Token (PAT). Prefer a fine-grained token with:
  - Repository access: All repositories (read-only)
  - Permissions: Issues (read-only) and Metadata (read-only)
- 📧 (Optional) SMTP server configuration for email notifications:
  - Access to an SMTP server (Gmail, Outlook, SendGrid, or custom)
  - SMTP credentials (username/password or app-specific password)
  - Valid sender and recipient email addresses

## Configuration ⚙️
Configuration lives in src/main/resources/application.yml. Relevant properties and defaults:

```yaml
spring:
  application:
    name: contribot
  mail:
    host: smtp.gmail.com                      # SMTP server host
    port: 587                                 # SMTP server port (typically 587 for TLS, 465 for SSL)
    username: ${SPRING_MAIL_USERNAME}         # SMTP authentication username
    password: ${SPRING_MAIL_PASSWORD}         # SMTP password
    properties:
      mail.smtp.auth: true                    # Enable SMTP authentication
      mail.smtp.starttls.enable: true         # Enable TLS encryption
      mail.smtp.starttls.required: true       # Require TLS
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
      notification:                           # Email notification settings
        email:
          sender: noreply@contribot.org       # Sender email address (must match SMTP user)
          recipient: your-email@gmail.com     # Recipient email address
          subject: New GitHub Issues          # Email subject line
```

### GitHub Configuration

Supply the GitHub token one of two ways (never commit secrets):
- 🌎 Environment variable (recommended)
  ```bash
  export GITHUB_TOKEN=<YOUR_GITHUB_TOKEN>
  ```
- 🧪 Application YAML (local/dev only)
  ```yaml
  github:
    token: <YOUR_GITHUB_TOKEN>
  ```

Adjust search qualifiers using any valid GitHub search syntax (e.g., `language:Kotlin`, `label:"help wanted"`, `org:your-org`).

Tip: See GithubQueryBuilder for query construction logic.

### Email/SMTP Configuration

To enable email notifications:

1. **Choose your SMTP provider**:
   - **Gmail**: `smtp.gmail.com:587` (requires [App Password](https://support.google.com/accounts/answer/185833))
   - **Outlook**: `smtp-mail.outlook.com:587`
   - **SendGrid**: `smtp.sendgrid.net:587`
   - **Custom**: Your organization's SMTP server

2. **Set environment variables**:
   ```bash
   export SPRING_MAIL_USERNAME=your-app-username
   export SPRING_MAIL_PASSWORD=your-app-password
   ```

3. **Update application.yml with notification settings**:
   ```yaml
   github:
     issue-search:
       job:
         notification:
           email:
             sender: noreply@your-domain.com
             recipient: your-email@example.com
   ```
   (See Configuration section for complete YAML example)

**Note**: Notifications are only sent when issues are found. Debug with `mail.debug: true` in properties.

## Running locally ▶️
Clone and start the application:

```bash
git clone https://github.com/khawaja-abdullah/contribot.git
cd contribot
./mvnw clean verify
./mvnw spring-boot:run
```

On startup, the job runs and persists execution metadata. Deleting the state file forces a fresh run using the configured lookback window.

To pass credentials inline for a one-off run:
```bash
GITHUB_TOKEN=ghp_xxx SPRING_MAIL_PASSWORD=pwd ./mvnw spring-boot:run
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
- 🗂️ State: Execution metadata persisted between runs
- 📜 Logs: Spring Boot logs show issues found and paging progress

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

## Documentation 📚

### JavaDoc Comments

All public and protected classes, interfaces, and methods throughout the codebase are thoroughly documented with comprehensive JavaDoc comments that include:

- **Class-level documentation**: Purpose, responsibilities, and usage examples
- **Method documentation**: Parameters, return values, exceptions, and detailed workflows
- **Nested class documentation**: Configuration properties with real-world examples
- **Design patterns**: Strategy, Repository, Adapter, Facade patterns explained in relevant classes

**Access JavaDoc documentation:**
- **In IDE**: Hover over any class or method for inline JavaDoc tooltips in IntelliJ IDEA
- **Generate HTML docs**: Run `./mvnw javadoc:javadoc` → Open `target/site/apidocs/index.html`

**Key documented components:**
- **Entry Points**: `ContribotApplication`, `JobRunner`
- **Core Jobs**: `GithubIssueSearchJob` (includes detailed workflow steps)
- **Services**: `GithubService`, `GitIssueSearchNotificationOrchestrator` (orchestrator pattern)
- **Strategies**: `IGitIssueFormatterStrategy`, `INotificationStrategy`, `SmtpNotificationStrategy`, `PlainTextGitIssueFormatterStrategy`
- **Data Transfer Objects**: `GitIssue`, `GitRepository`, `JobExecution` (with field-level documentation)
- **Configuration**: `GithubProperties` (nested classes with configuration examples), `GithubClientConfig`, `ObjectMapperConfig`
- **Persistence**: `IJobExecutionRepository`, `JobExecutionFSRepository` (file-system based implementation details)
- **Utilities**: `GithubQueryBuilder` (query construction logic), `Constant` (search query constants), `NotificationFormatType`, `NotificationChannelType`
- **Exception Handling**: `ContribotRuntimeException`

### Code Organization

All public classes are organized into logical packages:
- **org.voninc.contribot**: Application entry points
- **org.voninc.contribot.config**: Spring configuration and properties
- **org.voninc.contribot.service**: Business logic and notification strategies
- **org.voninc.contribot.job**: Job implementations
- **org.voninc.contribot.dao**: Data access and persistence
- **org.voninc.contribot.dto**: Data transfer objects
- **org.voninc.contribot.util**: Utility classes and enums
- **org.voninc.contribot.exception**: Custom exceptions

## Troubleshooting 🛠️
- 401/403 from GitHub API: Ensure your PAT has Issues (read-only) and Metadata (read-only) permissions and hasn't expired.
- No results returned: Widen or adjust github.issue-search.query.qualifiers.
- Duplicates after config change: Delete the state file to reset tracking.
- Build tool can't find Java: Verify java -version reports 21+.
- Rate limits: Reduce page-size or add more specific qualifiers.
- **Email not sending**: Verify environment variables (SPRING_MAIL_USERNAME, SPRING_MAIL_PASSWORD), SMTP settings, TLS enabled, and firewall allows outbound SMTP. Gmail users need [App Password](https://support.google.com/accounts/answer/185833).

## Contributing 🤝
- 🍴 Fork the repository and create a feature branch from main.
- ✅ Run ./mvnw clean verify locally and ensure tests pass.
- 🧪 Add tests for new behavior.
- 📬 Open a PR with a clear description of the change and configuration impact.

## Roadmap (high-level) 🗺️
- Slack notifications
- Support additional providers (GitLab, Bitbucket)
- Web UI for results and configuration

## License 📄
Licensed under the Apache License, Version 2.0. See LICENSE for details.
