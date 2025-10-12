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
package org.voninc.contribot.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IGitProviderServiceTest {

  private static List<IGitProviderService> gitProviderServiceImplementations() {
    return List.of(mock(GithubService.class), query -> Collections.emptyList());
  }

  @MethodSource("gitProviderServiceImplementations")
  @ParameterizedTest
  void shouldNotThrowWhenFindingIssuesForAnyConcreteProvider(IGitProviderService gitProviderService) {
    assertDoesNotThrow(() -> gitProviderService.findIssues("some query"));
  }

}
