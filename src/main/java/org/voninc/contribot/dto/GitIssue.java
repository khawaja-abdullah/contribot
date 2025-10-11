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
 * A minimal, platform-agnostic Data Transfer Object (DTO) representing a Git issue.
 * This record serves as the standardized contract for issue data returned by the
 * {@link org.voninc.contribot.service.IGitProviderService} implementations.
 *
 * @param title The primary title or subject of the issue.
 * @param body The main description content of the issue.
 * @param url The direct, clickable URL to the issue on the host platform.
 * @param gitRepository The repository context where the issue resides represented by {@link GitRepository}.
 */
public record GitIssue(String title, String body, URL url, GitRepository gitRepository) {
}
