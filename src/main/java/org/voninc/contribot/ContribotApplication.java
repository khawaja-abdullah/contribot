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
package org.voninc.contribot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.voninc.contribot.dto.GitIssue;
import org.voninc.contribot.service.IGitProviderService;

import java.util.List;

@SpringBootApplication
public class ContribotApplication implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContribotApplication.class);

  private final IGitProviderService gitProviderService;

  @Autowired
  public ContribotApplication(IGitProviderService gitProviderService) {
    this.gitProviderService = gitProviderService;
  }

  public static void main(String[] args) {
    SpringApplication.run(ContribotApplication.class, args);
  }

  @Override
  public void run(String... args) {
    List<GitIssue> gitIssues = gitProviderService.findIssues(
        "is:issue is:open label:\"good first issue\" language:Java sort:created-desc created:>=2025-10-11T08:00:00Z"
    );
    LOGGER.info("Total issues: {}", gitIssues.size());
  }

}
