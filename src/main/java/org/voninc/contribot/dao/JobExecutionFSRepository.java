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
import org.voninc.contribot.config.GithubProperties;
import org.voninc.contribot.dto.JobExecution;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File system–based implementation of {@link IJobExecutionRepository}.
 *
 * <p>This repository persists and retrieves {@link JobExecution} records from the local file system
 * using a JSON file defined in the GitHub configuration properties.</p>
 *
 * <p>It uses Jackson's {@link ObjectMapper} for serialization and deserialization of job execution
 * metadata and automatically creates the target file if it does not exist.</p>
 *
 * <p>Example configuration reference (from {@code application.yml}):</p>
 * <pre>{@code
 * github:
 *   issue-search:
 *     job:
 *       execution-file: data/job-execution.json
 * }</pre>
 *
 * <p><b>Note:</b> This implementation is suitable for lightweight or single-instance deployments.
 * For distributed or concurrent environments, consider a database-backed repository instead.</p>
 */
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

  /**
   * Retrieves the most recent {@link JobExecution} record from the configured file path.
   *
   * <p>If the file does not exist, it will be created automatically and {@code null} will be returned.
   * If the file exists but is empty, {@code null} will also be returned.</p>
   *
   * @return the last recorded {@link JobExecution}, or {@code null} if no record is available
   * @throws ContribotRuntimeException if the file cannot be read or deserialized
   */
  public JobExecution retrieveLast() {
    try {
      Path filePath = Paths.get(githubProperties.getIssueSearch().getJob().getExecutionFile());
      if (!Files.exists(filePath)) {
        LOGGER.debug("No previous job execution file found...creating one");
        Path parent = filePath.getParent();
        if (parent != null) {
          Files.createDirectories(parent);
        }
        Files.createFile(filePath);
        LOGGER.debug("Created new file for storing job execution: {}", filePath);
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

  /**
   * Persists a {@link JobExecution} record to the configured file path, overwriting any existing content.
   *
   * @param jobExecution the job execution metadata to persist; must not be {@code null}
   * @throws ContribotRuntimeException if the file cannot be written to or serialized
   */
  public void persist(JobExecution jobExecution) {
    try {
      Path targetPath = Paths.get(githubProperties.getIssueSearch().getJob().getExecutionFile());
      Path tempPath = targetPath.resolveSibling(targetPath.getFileName() + ".tmp");
      Files.write(tempPath, objectMapper.writeValueAsBytes(jobExecution));
      Files.move(tempPath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
    } catch (Exception e) {
      throw new ContribotRuntimeException("Failed to persist job execution!", e);
    }
  }

}
