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

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Configuration class responsible for setting up and providing the {@link org.kohsuke.github.GitHub} client bean.
 * <p>
 * This class uses an authenticated GitHub token (which could be a Fine-grained PAT, a Classic PAT, or a GitHub App
 * Installation Access Token) from application properties to authenticate the client for making secure API calls.
 */
@Configuration
public class GithubClientConfig {

  @Value("${github.token}")
  private String gitHubToken;

  /**
   * Creates and configures the core {@link org.kohsuke.github.GitHub} client bean.
   * <p>
   * It initializes the client using the configured OAuth token for authenticated access to the GitHub REST API.
   *
   * @return The configured GitHub client instance.
   */
  @Bean
  public GitHub gitHub() {
    if (gitHubToken == null || gitHubToken.isBlank()) {
      throw new IllegalStateException("GitHub token is not configured. Set 'github.token' in application properties.");
    }
    try {
      return new GitHubBuilder()
          .withOAuthToken(gitHubToken)
          .build();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to initialize GitHub client: " + e.getMessage(), e);
    }
  }

}
