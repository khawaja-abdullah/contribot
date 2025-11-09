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
package org.voninc.contribot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.voninc.contribot.job.issuesearch.GithubIssueSearchJob;

/**
 * Spring Boot {@link CommandLineRunner} implementation that triggers the execution
 * of the {@link GithubIssueSearchJob} at application startup.
 *
 * <p>This component ensures that the GitHub issue search job runs automatically
 * when the Spring Boot application context is fully initialized.</p>
 */
@Component
public class JobRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

  private final GithubIssueSearchJob githubIssueSearchJob;

  @Autowired
  public JobRunner(GithubIssueSearchJob githubIssueSearchJob) {
    this.githubIssueSearchJob = githubIssueSearchJob;
  }

  /**
   * Invoked by Spring Boot on application startup to run the GitHub issue search job.
   *
   * @param args Command-line arguments passed to the application (ignored by this implementation).
   * @throws Exception If the job execution fails for any reason.
   */
  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("Starting Github Issue Search Job ...");
    githubIssueSearchJob.run();
    LOGGER.info("Completed Github Issue Search Job ...");
  }

}
