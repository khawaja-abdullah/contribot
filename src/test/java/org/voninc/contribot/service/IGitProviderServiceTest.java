package org.voninc.contribot.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IGitProviderServiceTest {

  private static List<IGitProviderService> gitProviderServiceImplementations() {
    return List.of(mock(GithubService.class), query -> Collections.emptyList());
  }

  @MethodSource("gitProviderServiceImplementations")
  @ParameterizedTest
  void shouldNotThrowWhenFindingIssuesForAnyConcreteProvider(IGitProviderService gitProviderService) {
    assertDoesNotThrow(() -> gitProviderService.findIssues("some query"));
  }

}
