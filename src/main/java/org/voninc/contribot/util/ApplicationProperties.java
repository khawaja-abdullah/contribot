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
package org.voninc.contribot.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationProperties {

  @Value("${github.issueSearch.pageSize}")
  private int githubIssueSearchPageSize;
  @Value("${github.issueSearch.query.template}")
  private String githubIssueSearchQueryTemplate;
  @Value("${github.issueSearch.query.label}")
  private String githubIssueSearchQueryLabel;
  @Value("${github.issueSearch.query.language}")
  private String githubIssueSearchQueryLanguage;

  @Value("${github.issueSearch.job.executionFilePath}")
  private String githubIssueSearchJobExecutionFilePath;
  @Value("${github.issueSearch.job.firstRunDeltaHours}")
  private long githubIssueSearchJobFirstRunDeltaHours;

}
