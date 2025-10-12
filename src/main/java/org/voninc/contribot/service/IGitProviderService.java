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
 * Defines the essential contract for all Git provider clients (adapters).
 */
public interface IGitProviderService {

  /**
   * Finds open issues matching the provider's native search query.
   * <p>
   * Concrete implementations of this method are responsible for translating the common query string into the specific
   * syntax required by the underlying platform's API and mapping the results to the standardized {@link GitIssue} DTO.
   *
   * @param query The provider-specific search string (e.g., GitHub's 'label:x is:open').
   * @return A list of standardized {@link GitIssue} DTOs.
   */
  List<GitIssue> findIssues(String query);

}
