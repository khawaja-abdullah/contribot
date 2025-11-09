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
package org.voninc.contribot.config;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubClientConfigTest {

  private static final String GITHUB_PAT = "ghp_THIS_IS_A_DUMMY_TOKEN_REPLACE_ME_FOR_REAL_USE";

  @Mock
  private GithubProperties githubProperties;
  @InjectMocks
  private GithubClientConfig githubClientConfig;

  @Test
  void shouldGitHubClientInitializeWhenGitHubAuthenticationTokenIsValid() {
    when(githubProperties.getToken()).thenReturn(GITHUB_PAT);
    try (MockedConstruction<GitHubBuilder> gitHubBuilderMockedConstruction = mockConstruction(GitHubBuilder.class, (gitHubBuilder, context) -> {
      when(gitHubBuilder.withOAuthToken(anyString())).thenReturn(gitHubBuilder);
      when(gitHubBuilder.build()).thenReturn(mock(GitHub.class));
    })) {
      GitHub gitHub = githubClientConfig.gitHub();

      assertNotNull(gitHub);
    }
  }

  @Test
  void shouldThrowExceptionWhenGitHubAuthenticationTokenIsInValid() {
    when(githubProperties.getToken()).thenReturn(null);
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());

    when(githubProperties.getToken()).thenReturn(Strings.EMPTY);
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());

    when(githubProperties.getToken()).thenReturn("   ");
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());
  }

  @Test
  void shouldThrowExceptionWhenGitHubClientInitializationFails() {
    when(githubProperties.getToken()).thenReturn(GITHUB_PAT);
    try (MockedConstruction<GitHubBuilder> gitHubBuilderMockedConstruction = mockConstruction(GitHubBuilder.class, (gitHubBuilder, context) -> {
      when(gitHubBuilder.withOAuthToken(anyString())).thenReturn(gitHubBuilder);
      when(gitHubBuilder.build()).thenThrow(new IOException("Simulated Invalid Token Error"));
    })) {
      assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());
    }
  }

}
