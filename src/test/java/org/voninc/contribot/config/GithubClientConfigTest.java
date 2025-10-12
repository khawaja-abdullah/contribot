package org.voninc.contribot.config;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GithubClientConfigTest {

  private static final String GITHUB_TOKEN_FIELD_NAME = "githubToken";
  private static final String GITHUB_PAT = "ghp_THIS_IS_A_DUMMY_TOKEN_REPLACE_ME_FOR_REAL_USE";

  @InjectMocks
  private GithubClientConfig githubClientConfig;

  @Test
  void shouldGitHubClientInitializeWhenGitHubAuthenticationTokenIsValid() {
    ReflectionTestUtils.setField(githubClientConfig, GITHUB_TOKEN_FIELD_NAME, GITHUB_PAT);

    GitHub gitHub = githubClientConfig.gitHub();

    assertNotNull(gitHub);
  }

  @Test
  void shouldThrowExceptionWhenGitHubAuthenticationTokenIsInValid() {
    ReflectionTestUtils.setField(githubClientConfig, GITHUB_TOKEN_FIELD_NAME, null);
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());

    ReflectionTestUtils.setField(githubClientConfig, GITHUB_TOKEN_FIELD_NAME, Strings.EMPTY);
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());

    ReflectionTestUtils.setField(githubClientConfig, GITHUB_TOKEN_FIELD_NAME, "   ");
    assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());
  }

  @Test
  void shouldThrowExceptionWhenGitHubClientInitializationFails() {
    ReflectionTestUtils.setField(githubClientConfig, GITHUB_TOKEN_FIELD_NAME, GITHUB_PAT);
    try (MockedConstruction<GitHubBuilder> gitHubBuilderMockedConstruction = mockConstruction(GitHubBuilder.class, (gitHubBuilder, context) -> {
      when(gitHubBuilder.withOAuthToken(anyString())).thenReturn(gitHubBuilder);
      when(gitHubBuilder.build()).thenThrow(new IOException("Simulated Invalid Token Error"));
    })) {
      assertThrows(ContribotRuntimeException.class, () -> githubClientConfig.gitHub());
    }
  }

}