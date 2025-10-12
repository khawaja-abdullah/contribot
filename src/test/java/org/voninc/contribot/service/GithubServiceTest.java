package org.voninc.contribot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.voninc.contribot.exception.ContribotRuntimeException;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
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
    assertThrows(ContribotRuntimeException.class, () -> githubService.findIssues(ISSUE_SEARCH_QUERY).size());
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
    when(ghIssue.getTitle()).thenReturn("title");
    when(ghIssue.getBody()).thenReturn("body");
    try {
      when(ghIssue.getUrl()).thenReturn(URI.create("https://github.com/ownerName/repoName/issues/6").toURL());
    } catch (MalformedURLException e) {
      when(ghIssue.getUrl()).thenReturn(null);
    }
    when(ghIssue.getRepository()).thenReturn(ghRepository);
    return ghIssue;
  }

  private GHRepository getGHRepository() {
    GHRepository ghRepository = mock(GHRepository.class);
    when(ghRepository.getOwnerName()).thenReturn("ownerName");
    when(ghRepository.getName()).thenReturn("repoName");
    when(ghRepository.getFullName()).thenReturn("ownerName/repoName");
    try {
      when(ghRepository.getUrl()).thenReturn(URI.create("https://github.com/ownerName/repoName").toURL());
    } catch (MalformedURLException e) {
      when(ghRepository.getUrl()).thenReturn(null);
    }
    return ghRepository;
  }

}
