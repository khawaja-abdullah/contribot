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

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.voninc.contribot.dto.JobExecution;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IJobExecutionRepositoryTest {

  private static List<IJobExecutionRepository> jobExecutionRepositoryImplementations() {
    return List.of(mock(JobExecutionFSRepository.class), new IJobExecutionRepository() {
      @Override
      public JobExecution retrieveLast() {
        return null;
      }

      @Override
      public void persist(JobExecution jobExecution) {
        // do nothing
      }
    });
  }

  @MethodSource("jobExecutionRepositoryImplementations")
  @ParameterizedTest
  void shouldNotThrowWhenRetrievingLastJobExecution(IJobExecutionRepository jobExecutionRepository) {
    assertDoesNotThrow(jobExecutionRepository::retrieveLast);
  }

  @MethodSource("jobExecutionRepositoryImplementations")
  @ParameterizedTest
  void shouldNotThrowWhenPersistingJobExecution(IJobExecutionRepository jobExecutionRepository) {
    assertDoesNotThrow(() -> jobExecutionRepository.persist(mock(JobExecution.class)));
  }

}
