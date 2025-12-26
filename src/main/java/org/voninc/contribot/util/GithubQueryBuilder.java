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

import org.voninc.contribot.config.GithubProperties;

import java.time.LocalDateTime;

/**
 * Utility class for constructing Git provider-specific issue search queries.
 *
 * <p>This class is not instantiable and provides static helper methods for building
 * query strings that comply with the target provider's search API syntax, currently
 * focused on GitHub issue search.</p>
 */
public final class GithubQueryBuilder {

  private GithubQueryBuilder() {
    // Prevent instantiation
  }

  /**
   * Builds a GitHub issue search query string using the provided {@link GithubProperties} and
   * a starting creation date.
   *
   * <p>The method combines user-defined qualifiers from {@code githubProperties}, excludes
   * any existing {@code sort} or {@code created} qualifiers, and appends a default sort
   * by creation date descending and a filter for issues created since the specified time.</p>
   *
   * <p>Example output: <br>
   * {@code "label:bug is:open sort:created-desc created:>=2025-11-09T12:00:00"}</p>
   *
   * @param githubProperties Configuration properties containing issue search qualifiers.
   * @param createdAt        The starting point for filtering issues by creation date.
   * @return A formatted GitHub issue search query string.
   */
  public static String buildIssueSearchQuery(GithubProperties githubProperties, LocalDateTime createdAt) {
    StringBuilder query = new StringBuilder();
    githubProperties.getIssueSearch()
        .getQuery()
        .getQualifiers()
        .stream()
        .filter(qualifier -> !qualifier.startsWith(Constant.SORT_QUALIFIER_FIELD) &&
            !qualifier.startsWith(Constant.CREATED_QUALIFIER_FIELD)
        )
        .forEach(qualifier -> query.append(qualifier).append(" "));
    query.append(Constant.SORT_CREATED_DESC).append(" ")
        .append(Constant.CREATED_GTE).append(createdAt);
    return query.toString();
  }

}
