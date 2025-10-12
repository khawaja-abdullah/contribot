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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

  private static final String ISSUE_SEARCH_QUERY = "is:issue is:open label:\"good first issue\" language:Java sort:created-desc created:>=2025-10-11T08:00:00Z";

  @Mock
  private PagedSearchIterable<GHIssue> pagedSearchIterableOfGHIssue;
  @Mock
  private GHIssueSearchBuilder ghIssueSearchBuilder;
  @Mock
  private GitHub gitHub;

  @InjectMocks
  private GithubService githubService;

  @Test
  void shouldFindIssuesWhenQueryIsValid() {
    givenMocksInitializedForIssueSearch(getGHIssue(getGHRepository()));
    assertEquals(1, githubService.findIssues(ISSUE_SEARCH_QUERY).size());
  }

  @Test
  void shouldFilterIssueFromSearchResultsWhenNotRepositoryLinked() {
    givenMocksInitializedForIssueSearch(getGHIssue(null));
    assertEquals(0, githubService.findIssues(ISSUE_SEARCH_QUERY).size());
  }

  @Test
  void shouldThrowExceptionWhenSearchFails() {
    when(gitHub.searchIssues()).thenThrow(new RuntimeException("Something went wrong"));
    assertThrows(ContribotRuntimeException.class, () -> githubService.findIssues(ISSUE_SEARCH_QUERY));
  }

  private void givenMocksInitializedForIssueSearch(GHIssue ghIssue) {
    doAnswer((Answer<Void>) invocation -> {
      Consumer<GHIssue> consumer = invocation.getArgument(0);
      consumer.accept(ghIssue);
      return null;
    }).when(pagedSearchIterableOfGHIssue).forEach(any());
    when(pagedSearchIterableOfGHIssue.withPageSize(100)).thenReturn(pagedSearchIterableOfGHIssue);
    when(ghIssueSearchBuilder.list()).thenReturn(pagedSearchIterableOfGHIssue);
    when(ghIssueSearchBuilder.q(ISSUE_SEARCH_QUERY)).thenReturn(ghIssueSearchBuilder);
    when(gitHub.searchIssues()).thenReturn(ghIssueSearchBuilder);
  }

  private GHIssue getGHIssue(GHRepository ghRepository) {
    GHIssue ghIssue = mock(GHIssue.class);
    when(ghIssue.getRepository()).thenReturn(ghRepository);
    if (ghRepository == null) return ghIssue; // no further stubbing needed if repository is passed is null.
    when(ghIssue.getTitle()).thenReturn("title");
    when(ghIssue.getBody()).thenReturn("body");
    try {
      when(ghIssue.getHtmlUrl()).thenReturn(URI.create("https://github.com/ownerName/repoName/issues/6").toURL());
    } catch (MalformedURLException e) {
      when(ghIssue.getHtmlUrl()).thenReturn(null);
    }
    return ghIssue;
  }

  private GHRepository getGHRepository() {
    GHRepository ghRepository = mock(GHRepository.class);
    when(ghRepository.getOwnerName()).thenReturn("ownerName");
    when(ghRepository.getName()).thenReturn("repoName");
    when(ghRepository.getFullName()).thenReturn("ownerName/repoName");
    try {
      when(ghRepository.getHtmlUrl()).thenReturn(URI.create("https://github.com/ownerName/repoName").toURL());
    } catch (MalformedURLException e) {
      when(ghRepository.getHtmlUrl()).thenReturn(null);
    }
    return ghRepository;
  }

}
