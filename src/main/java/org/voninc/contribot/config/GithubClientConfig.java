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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.io.IOException;

/**
 * Spring configuration class responsible for creating and exposing a singleton {@link GitHub} client bean.
 *
 * <p>This configuration authenticates a GitHub client using an access token retrieved from
 * {@link GithubProperties}. The token can represent a Fine-grained Personal Access Token (PAT),
 * a Classic PAT, or a GitHub App installation access token.</p>
 *
 * <p>The resulting {@link GitHub} instance is preconfigured for authenticated interaction
 * with the GitHub REST API, allowing other components to perform secure operations such as
 * repository management, issue tracking, or pull request handling.</p>
 */
@Configuration
public class GithubClientConfig {

  private final GithubProperties githubProperties;

  @Autowired
  public GithubClientConfig(GithubProperties githubProperties) {
    this.githubProperties = githubProperties;
  }

  /**
   * Creates and configures the core {@link GitHub} client bean.
   *
   * <p>This method initializes an authenticated GitHub client using the token defined in the
   * application properties. The resulting client provides authorized access to GitHub’s API
   * endpoints and can be injected into other Spring components as needed.</p>
   *
   * @return a fully initialized and authenticated {@link GitHub} client instance
   * @throws ContribotRuntimeException if the GitHub token is missing, invalid, or if a client
   *                                   initialization error occurs (e.g., network failure or I/O issue)
   */
  @Bean
  public GitHub gitHub() {
    String githubToken = githubProperties.getToken();
    if (githubToken == null || githubToken.isBlank()) {
      throw new ContribotRuntimeException("GitHub token is not configured. Set 'github.token' in application properties.");
    }
    try {
      return new GitHubBuilder()
          .withOAuthToken(githubToken)
          .build();
    } catch (IOException e) {
      throw new ContribotRuntimeException("Failed to initialize GitHub client: " + e.getMessage(), e);
    }
  }

}
