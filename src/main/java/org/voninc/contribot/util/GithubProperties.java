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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

    private String token;

    private IssueSearch issueSearch;

    @Getter
    @Setter
    public static class IssueSearch {
        private int pageSize;
        private Query query;
        private Job job;

        @Getter
        @Setter
        public static class Query {
            private List<String> qualifiers;
        }

        @Getter
        @Setter
        public static class Job {
            private String executionFile;
            private long initialLookbackHours;
        }
    }
}
