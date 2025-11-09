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

import org.voninc.contribot.dto.GitIssue;

import java.util.List;

/**
 * Service interface defining the contract for interacting with a Git provider.
 *
 * <p>Implementations of this interface act as adapters for specific Git platforms (e.g., GitHub, GitLab)
 * and are responsible for translating generic queries into platform-specific search requests, as well
 * as mapping results into standardized {@link GitIssue} DTOs.</p>
 */
public interface IGitProviderService {

  /**
   * Searches for open issues that match the given provider-specific query string.
   *
   * <p>Concrete implementations must handle the translation of the generic or provider-specific query
   * into the appropriate API calls and return the results mapped to {@link GitIssue} DTOs.</p>
   *
   * @param query A search string formatted according to the Git provider's API requirements
   *              (for example, GitHub query syntax such as 'label:bug is:open').
   * @return A list of {@link GitIssue} objects representing the issues found that match the query.
   */
  List<GitIssue> findIssues(String query);

}
