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

import org.voninc.contribot.dto.JobExecution;

/**
 * Repository interface for managing {@link JobExecution} records.
 *
 * <p>This abstraction provides methods to persist and retrieve information
 * about job executions, such as those performed by background or scheduled
 * tasks within the application.</p>
 *
 * <p>Implementations may store job execution data in various backends
 * (e.g., a relational database, file system, or in-memory cache)
 * depending on the persistence requirements of the system.</p>
 */
public interface IJobExecutionRepository {

  /**
   * Retrieves the most recent {@link JobExecution} record.
   *
   * @return the last recorded {@link JobExecution}, or {@code null} if no records exist
   */
  JobExecution retrieveLast();

  /**
   * Persists a new {@link JobExecution} record.
   *
   * @param jobExecution the job execution metadata to persist; must not be {@code null}
   */
  void persist(JobExecution jobExecution);

}
