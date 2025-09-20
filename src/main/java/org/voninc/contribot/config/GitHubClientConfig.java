package org.voninc.contribot.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GitHubClientConfig {

  @Bean
  public GitHub gitHub() throws IOException {
    return new GitHubBuilder()
        .withOAuthToken(System.getenv("GITHUB_TOKEN"))
        .build();
  }

}
