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
package org.voninc.contribot.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.voninc.contribot.config.GithubProperties;
import org.voninc.contribot.config.ObjectMapperConfig;
import org.voninc.contribot.dto.JobExecution;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {GithubProperties.class, ObjectMapperConfig.class, JobExecutionFSRepository.class})
@EnableConfigurationProperties(GithubProperties.class)
@TestPropertySource(
    properties = {"github.issue-search.job.execution-file=${java.io.tmpdir}/job-execution-${random.uuid}.json"}
)
class JobExecutionFSRepositoryIntTest {

  @Autowired
  private JobExecutionFSRepository jobExecutionFSRepository;

  @Test
  void shouldPersistAndRetrieveJobExecution() {
    JobExecution jobExecutionRetrievedOnEmptyFile = jobExecutionFSRepository.retrieveLast();
    assertNull(jobExecutionRetrievedOnEmptyFile, "Retrieved job execution should be null");

    JobExecution jobExecutionToPersist =
        new JobExecution(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), 60000);

    jobExecutionFSRepository.persist(jobExecutionToPersist);
    JobExecution jobExecutionRetrievedAfterPersist = jobExecutionFSRepository.retrieveLast();

    assertNotNull(jobExecutionRetrievedAfterPersist, "Retrieved job execution should not be null");
    assertEquals(jobExecutionToPersist.jobId(), jobExecutionRetrievedAfterPersist.jobId(), "Job IDs should match");
    assertEquals(jobExecutionToPersist.duration(), jobExecutionRetrievedAfterPersist.duration(), "Durations should match");
    assertEquals(jobExecutionToPersist.startTime(), jobExecutionRetrievedAfterPersist.startTime(), "Start times should match");
    assertEquals(jobExecutionToPersist.endTime(), jobExecutionRetrievedAfterPersist.endTime(), "End times should match");
  }

}
