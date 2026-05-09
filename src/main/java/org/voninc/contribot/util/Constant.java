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

/**
 * Constants used in GitHub search query construction.
 *
 * <p>This utility class provides reusable constants for building GitHub issue search queries
 * according to GitHub's search API syntax. It consolidates the qualifier prefixes, field names,
 * and pre-built qualifier strings to ensure consistency across the application.</p>
 *
 * <p>This class is not instantiable.</p>
 */
public final class Constant {

  private Constant() {
    // Prevent instantiation
  }

  /** GitHub search qualifier prefix for sorting results (e.g., {@code sort:created-desc}) */
  public static final String SORT_QUALIFIER_FIELD = "sort:";
  private static final String SORT_QUALIFIER_VALUE_CREATED_DESC = "created-desc";

  /** GitHub search qualifier prefix for filtering by creation date (e.g., {@code created:>=2025-01-01}) */
  public static final String CREATED_QUALIFIER_FIELD = "created:";

  /** Comparison operator for "greater than or equal to" in GitHub searches */
  private static final String GTE = ">=";

  /** Pre-built qualifier to sort issues by creation date in descending order (newest first) */
  public static final String SORT_CREATED_DESC = SORT_QUALIFIER_FIELD + SORT_QUALIFIER_VALUE_CREATED_DESC;

  /** Pre-built qualifier prefix for filtering issues created on or after a specific date */
  public static final String CREATED_GTE = CREATED_QUALIFIER_FIELD + GTE;

}
