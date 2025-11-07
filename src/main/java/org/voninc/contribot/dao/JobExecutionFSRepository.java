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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.voninc.contribot.dto.JobExecution;
import org.voninc.contribot.exception.ContribotRuntimeException;
import org.voninc.contribot.util.GithubProperties;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class JobExecutionFSRepository implements IJobExecutionRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionFSRepository.class);

  private final GithubProperties githubProperties;
  private final ObjectMapper objectMapper;

  @Autowired
  public JobExecutionFSRepository(GithubProperties githubProperties, ObjectMapper objectMapper) {
    this.githubProperties = githubProperties;
    this.objectMapper = objectMapper;
  }

  public JobExecution retrieveLast() {
    try {
      Path filePath = Paths.get(githubProperties.getIssueSearch().getJob().getExecutionFile());
      if (!Files.exists(filePath)) {
        LOGGER.debug("No previous job execution file found...creating one");
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
        LOGGER.debug("Created new file for storing job execution: {}", filePath);
        return null;
      }
      try (InputStream inputStream = Files.newInputStream(filePath)) {
        byte[] readBytes = inputStream.readAllBytes();
        if (readBytes.length == 0) {
          LOGGER.debug("Previous job execution file has no data");
          return null;
        }
        return objectMapper.readValue(readBytes, JobExecution.class);
      }
    } catch (Exception e) {
      throw new ContribotRuntimeException("Failed to retrieve last job execution!", e);
    }
  }

  public void persist(JobExecution jobExecution) {
    try (OutputStream outputStream = Files.newOutputStream(Paths.get(githubProperties.getIssueSearch().getJob().getExecutionFile()))) {
      outputStream.write(objectMapper.writeValueAsBytes(jobExecution));
    } catch (Exception e) {
      throw new ContribotRuntimeException("Failed to persist job execution!", e);
    }
  }

}
