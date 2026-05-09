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
package org.voninc.contribot.job.issuesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.voninc.contribot.config.GithubProperties;
import org.voninc.contribot.dao.IJobExecutionRepository;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.dto.JobExecution;
import org.voninc.contribot.exception.ContribotRuntimeException;
import org.voninc.contribot.job.IJob;
import org.voninc.contribot.service.IGitProviderService;
import org.voninc.contribot.service.GitIssueSearchNotificationOrchestrator;
import org.voninc.contribot.util.GithubQueryBuilder;
import org.voninc.contribot.util.NotificationChannelType;
import org.voninc.contribot.util.NotificationFormatType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * Job responsible for searching GitHub issues based on configured query parameters.
 *
 * <p>This job implements the {@link IJob} interface and performs the following workflow:</p>
 * <ol>
 *   <li>Retrieves the last recorded job execution timestamp from {@link IJobExecutionRepository}
 *       to determine the search window.</li>
 *   <li>If no previous execution exists, uses the initial lookback hours configured in
 *       {@link GithubProperties} to establish a search start time.</li>
 *   <li>Constructs a GitHub search query using {@link GithubQueryBuilder} with the
 *       configured search qualifiers and creation date filter.</li>
 *   <li>Executes the search via {@link IGitProviderService#findIssues(String)}.</li>
 *   <li>If issues are found, sends an email notification via
 *       {@link GitIssueSearchNotificationOrchestrator#sendNotification(NotificationFormatType, NotificationChannelType, String, String, String, List)}.</li>
 *   <li>Records the execution metadata (start time, end time, duration) and persists it
 *       using {@link IJobExecutionRepository#persist(JobExecution)}.</li>
 * </ol>
 *
 * <p>All timestamps are in UTC. Execution errors are logged but do not propagate further,
 * allowing the application to continue running even if the job fails.</p>
 *
 * <p>This job is automatically triggered at application startup via {@link org.voninc.contribot.JobRunner}.</p>
 */
@Component
public class GithubIssueSearchJob implements IJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueSearchJob.class);

  private final GithubProperties githubProperties;
  private final IJobExecutionRepository jobExecutionRepository;
  private final IGitProviderService gitProviderService;
  private final GitIssueSearchNotificationOrchestrator gitIssueSearchNotificationOrchestrator;

  /**
   * Constructs a new GitHub issue search job with required dependencies.
   *
   * @param githubProperties configuration properties for GitHub integration
   * @param jobExecutionRepository repository for persisting job execution metadata
   * @param gitProviderService service for querying GitHub issues
   * @param gitIssueSearchNotificationOrchestrator orchestrator for sending notifications
   */
  @Autowired
  public GithubIssueSearchJob(GithubProperties githubProperties,
                              IJobExecutionRepository jobExecutionRepository,
                              IGitProviderService gitProviderService,
                              GitIssueSearchNotificationOrchestrator gitIssueSearchNotificationOrchestrator) {
    this.githubProperties = githubProperties;
    this.jobExecutionRepository = jobExecutionRepository;
    this.gitProviderService = gitProviderService;
    this.gitIssueSearchNotificationOrchestrator = gitIssueSearchNotificationOrchestrator;
  }

  /**
   * Executes the GitHub issue search job.
   *
   * <p>Steps performed:</p>
   * <ol>
   *   <li>Record the current run start time in UTC.</li>
   *   <li>Retrieve the last {@link JobExecution} to determine the search window.</li>
   *   <li>If no previous execution exists, use the initial lookback window from configuration.</li>
   *   <li>Build a GitHub search query using {@link GithubQueryBuilder#buildIssueSearchQuery(GithubProperties, LocalDateTime)}.</li>
   *   <li>Execute the search via {@link IGitProviderService#findIssues(String)}.</li>
   *   <li>Log the number of issues found.</li>
   *   <li>If issues are found, send an email notification with the results.</li>
   *   <li>Record the current execution end time and calculate duration in milliseconds.</li>
   *   <li>Persist the {@link JobExecution} record for the next run.</li>
   *   <li>Log any errors encountered without propagating exceptions.</li>
   * </ol>
   *
   * <p><b>Note:</b> This method is non-blocking and handles all exceptions internally by logging.
   * Exceptions do not prevent other application components from functioning.</p>
   */
  @Override
  public void run() {
    LocalDateTime currentRunStartTime = LocalDateTime.now(ZoneOffset.UTC);
    try {
      JobExecution lastJobExecution = jobExecutionRepository.retrieveLast();
      LocalDateTime previousRunStartTime = lastJobExecution == null ?
          currentRunStartTime.minusHours(githubProperties.getIssueSearch().getJob().getInitialLookbackHours()) :
          lastJobExecution.startTime();
      String searchQuery = GithubQueryBuilder.buildIssueSearchQuery(githubProperties, previousRunStartTime);
      LOGGER.info("Github Search Query: {}", searchQuery);
      List<GitIssue> gitIssues = gitProviderService.findIssues(searchQuery);
      LOGGER.info("Issues found count: {}", gitIssues.size());
      if (!gitIssues.isEmpty()) {
        gitIssueSearchNotificationOrchestrator.sendNotification(
            NotificationFormatType.PLAINTEXT,
            NotificationChannelType.EMAIL_SMTP,
            githubProperties.getIssueSearch().getJob().getNotification().getEmail().getSender(),
            githubProperties.getIssueSearch().getJob().getNotification().getEmail().getRecipient(),
            githubProperties.getIssueSearch().getJob().getNotification().getEmail().getSubject(),
            gitIssues
        );
      }
      LocalDateTime currentRunEndTime = LocalDateTime.now(ZoneOffset.UTC);
      JobExecution currentJobExecution = new JobExecution(
          UUID.randomUUID(), currentRunStartTime, currentRunEndTime, Duration.between(currentRunStartTime, currentRunEndTime).toMillis()
      );
      jobExecutionRepository.persist(currentJobExecution);
    } catch (ContribotRuntimeException e) {
      LOGGER.error("GithubIssueSearchJob execution failed!", e);
    }
  }

}
