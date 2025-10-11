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
package org.voninc.contribot.dto;

import java.net.URL;

/**
 * A minimal, platform-agnostic Data Transfer Object (DTO) representing the essential metadata for a Git repository.
 * This record provides the necessary context for where a {@link GitIssue} resides.
 *
 * @param ownerName The username or organization name that owns the repository (e.g., "khawaja-abdullah").
 * @param repositoryName The short, unique name of the repository (e.g., "contribot").
 * @param fullRepositoryName The combined owner and repository name (e.g., "khawaja-abdullah/contribot").
 * @param url The base URL for the repository on the Git host platform.
 */
public record GitRepository(String ownerName, String repositoryName, String fullRepositoryName,
                            URL url) {
}
