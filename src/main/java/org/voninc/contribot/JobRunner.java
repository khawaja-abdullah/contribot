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
 * Executes the GitHub issue search job at application startup.
 *
 * <p>This component implements Spring's {@link CommandLineRunner} interface and is automatically
 * invoked by Spring Boot after the application context is fully initialized. It delegates
 * job execution to the configured {@link GithubIssueSearchJob} instance.</p>
 *
 * <p>The job discovery and notification workflow begins immediately after startup, allowing
 * the application to discover fresh GitHub issues and send notifications without requiring
 * external triggers or scheduling.</p>
 */
@Component
public class JobRunner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

  private final GithubIssueSearchJob githubIssueSearchJob;

  /**
   * Constructs a new JobRunner with the GitHub issue search job dependency.
   *
   * @param githubIssueSearchJob the job responsible for discovering and notifying about GitHub issues;
   *                            must not be null
   */
  @Autowired
  public JobRunner(GithubIssueSearchJob githubIssueSearchJob) {
    this.githubIssueSearchJob = githubIssueSearchJob;
  }

  /**
   * Runs the GitHub issue search job.
   *
   * <p>This method is invoked automatically by Spring Boot at application startup.
   * It logs the job start and completion, then delegates to the configured
   * {@link GithubIssueSearchJob#run()} for the actual issue search and notification logic.</p>
   *
   * @param args command-line arguments passed to the application (not used by this job)
   * @throws Exception if job execution fails unexpectedly (though errors are typically handled
   *                   internally by the job and logged)
   */
  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("Starting Github Issue Search Job ...");
    githubIssueSearchJob.run();
    LOGGER.info("Completed Github Issue Search Job ...");
  }

}
