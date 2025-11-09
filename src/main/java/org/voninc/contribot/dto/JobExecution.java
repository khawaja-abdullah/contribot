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
package org.voninc.contribot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents the metadata of a single job execution.
 *
 * <p>This record stores information about the execution of a background or scheduled job,
 * including its unique identifier, start and end timestamps, and total duration.</p>
 *
 * @param jobId     the unique identifier of the job execution
 * @param startTime the timestamp when the job execution started
 * @param endTime   the timestamp when the job execution finished
 * @param duration  the duration of the job execution in milliseconds
 */
public record JobExecution(UUID jobId, LocalDateTime startTime, LocalDateTime endTime, long duration) {
}
