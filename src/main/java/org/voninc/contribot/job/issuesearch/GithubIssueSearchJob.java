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
 * <p>This job retrieves the last execution timestamp from {@link IJobExecutionRepository} to determine
 * the search window. It constructs a GitHub search query using {@link GithubQueryBuilder} and executes
 * it via {@link IGitProviderService}.</p>
 *
 * <p>After execution, the job persists a {@link org.voninc.contribot.dto.JobExecution} record
 * containing the job's start time, end time, and duration for tracking future executions.</p>
 *
 * <p>Execution errors are logged via {@link org.slf4j.Logger} but do not propagate further.</p>
 */
@Component
public class GithubIssueSearchJob implements IJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueSearchJob.class);

  private final GithubProperties githubProperties;
  private final IJobExecutionRepository jobExecutionRepository;
  private final IGitProviderService gitProviderService;
  private final GitIssueSearchNotificationOrchestrator gitIssueSearchNotificationOrchestrator;

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
   *   <li>Determine the start time of the current run.</li>
   *   <li>Retrieve the last {@link JobExecution} from {@link IJobExecutionRepository}.</li>
   *   <li>Determine the search window based on the last execution or initial lookback hours.</li>
   *   <li>Construct a GitHub issue search query using {@link GithubQueryBuilder}.</li>
   *   <li>Execute the search via {@link IGitProviderService} and log the number of issues found.</li>
   *   <li>Create a new {@link JobExecution} record with start time, end time, and duration.</li>
   *   <li>Persist the job execution using {@link IJobExecutionRepository}.</li>
   *   <li>Log completion and any errors encountered.</li>
   * </ol>
   *
   * <p>All timestamps are in UTC. Duration is calculated in milliseconds.</p>
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
