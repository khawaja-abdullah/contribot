/*
 * Copyright 2025 Von Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.voninc.contribot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration properties for GitHub integration.
 *
 * <p>This class binds values from the application's configuration file (e.g. {@code application.yml}
 * or {@code application.properties}) under the {@code github.*} namespace.</p>
 *
 * <p>It defines authentication settings (such as the personal access token) and parameters
 * used for issue search and background job execution.</p>
 *
 * <p>Example configuration:
 * <pre>{@code
 * github:
 *   token: ghp_example123456789
 *   issue-search:
 *     page-size: 50
 *     query:
 *       qualifiers:
 *         - is:open
 *         - label:bug
 *         - repo:voninc/contribot
 *     job:
 *       execution-file: jobs/search_issues.json
 *       initial-lookback-hours: 12
 * }</pre>
 * </p>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

  /**
   * The GitHub authentication token used for API access.
   * <p>This may be a Fine-grained Personal Access Token (PAT), Classic PAT,
   * or a GitHub App installation access token.</p>
   */
  private String token;
  /**
   * Nested configuration for GitHub issue search parameters.
   */
  private IssueSearch issueSearch;

  /**
   * Configuration options related to GitHub issue search functionality.
   */
  @Getter
  @Setter
  public static class IssueSearch {

    /**
     * The maximum number of issues to retrieve per page from the GitHub API.
     */
    private int pageSize;
    /**
     * Configuration for query filters applied to GitHub issue searches.
     */
    private Query query;
    /**
     * Configuration for scheduled or background jobs that perform issue search operations.
     */
    private Job job;

    /**
     * Defines search qualifiers used to build GitHub issue search queries.
     * <p>Each qualifier represents a GitHub search filter such as {@code repo:}, {@code is:open}, or {@code label:bug}.</p>
     */
    @Getter
    @Setter
    public static class Query {

      /**
       * A list of qualifiers (filters) used when constructing GitHub issue search queries.
       */
      private List<String> qualifiers;

    }

    /**
     * Defines job-level configuration for scheduled tasks or background processes
     * that interact with the GitHub issue search API.
     */
    @Getter
    @Setter
    public static class Job {

      /**
       * File path used to load and store the last {@link org.voninc.contribot.dto.JobExecution}.
       */
      private String executionFile;
      /**
       * The initial time window (in hours) to look back when performing
       * the first issue search or job execution.
       */
      private long initialLookbackHours;

      private Notification notification;

      @Getter
      @Setter
      public static class Notification {

        private Email email;

        @Getter
        @Setter
        public static class Email {

          private String sender;
          private String recipient;
          private String subject;

        }

      }

    }

  }

}
