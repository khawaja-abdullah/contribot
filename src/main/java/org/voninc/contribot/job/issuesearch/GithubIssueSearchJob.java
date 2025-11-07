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
import org.voninc.contribot.dao.IJobExecutionRepository;
import org.voninc.contribot.dto.JobExecution;
import org.voninc.contribot.exception.ContribotRuntimeException;
import org.voninc.contribot.job.IJob;
import org.voninc.contribot.service.IGitProviderService;
import org.voninc.contribot.config.GithubProperties;
import org.voninc.contribot.util.GithubQueryBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class GithubIssueSearchJob implements IJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueSearchJob.class);

  private final GithubProperties githubProperties;
  private final IJobExecutionRepository jobExecutionRepository;
  private final IGitProviderService gitProviderService;

  @Autowired
  public GithubIssueSearchJob(GithubProperties githubProperties,
                              IJobExecutionRepository jobExecutionRepository,
                              IGitProviderService gitProviderService) {
    this.githubProperties = githubProperties;
    this.jobExecutionRepository = jobExecutionRepository;
    this.gitProviderService = gitProviderService;
  }

  @Override
  public void run() {
    LocalDateTime currentRunStartTime = LocalDateTime.now(ZoneOffset.UTC);
    try {
      JobExecution lastJobExecution = jobExecutionRepository.retrieveLast();
      LocalDateTime previousRunStartTime = lastJobExecution == null ?
          currentRunStartTime.minusHours(githubProperties.getIssueSearch().getJob().getInitialLookbackHours()) :
          lastJobExecution.startTime();
      String searchQuery = GithubQueryBuilder.buildQuery(githubProperties, previousRunStartTime);
      LOGGER.info("Github Search Query: {}", searchQuery);
      // TODO: notification via email with extended API response as message body
      LOGGER.info("Issues found count: {}", gitProviderService.findIssues(searchQuery).size());
      LocalDateTime currentRunEndTime = LocalDateTime.now(ZoneOffset.UTC);
      JobExecution currentJobExecution = new JobExecution(
          UUID.randomUUID(), currentRunStartTime, currentRunEndTime, Duration.between(currentRunStartTime, currentRunEndTime).getNano() / 100_000
      );
      jobExecutionRepository.persist(currentJobExecution);
    } catch (ContribotRuntimeException e) {
      LOGGER.error("GithubIssueSearchJob execution failed!", e);
    }
  }

}
