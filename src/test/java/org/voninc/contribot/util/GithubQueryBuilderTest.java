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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.voninc.contribot.config.GithubProperties;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubQueryBuilderTest {

  private GithubProperties githubProperties;

  @BeforeEach
  void setUp() {
    githubProperties = new GithubProperties();
  }

  @Test
  void shouldBuildIssueSearchQueryWithQualifiersAndDefaults() {
    GithubProperties.IssueSearch.Query queryProperties = new GithubProperties.IssueSearch.Query();
    queryProperties.setQualifiers(List.of("label:bug", "is:open", "sort:created-asc", "created:<=2025-11-09T12:00:00"));
    GithubProperties.IssueSearch issueSearchProperties = new GithubProperties.IssueSearch();
    issueSearchProperties.setQuery(queryProperties);
    githubProperties.setIssueSearch(issueSearchProperties);

    LocalDateTime now = LocalDateTime.of(2025, 11, 9, 12, 0);

    String query = GithubQueryBuilder.buildIssueSearchQuery(githubProperties, now);

    assertTrue(query.contains("label:bug"));
    assertTrue(query.contains("is:open"));
    assertTrue(query.contains("sort:created-desc"));
    assertTrue(query.contains("created:>=" + now));
  }

}
