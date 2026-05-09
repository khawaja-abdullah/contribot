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
package org.voninc.contribot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Contribot Spring Boot application.
 *
 * <p>Contribot is a lightweight utility that periodically discovers fresh open-source
 * GitHub issues suitable for first-time or quick contributions. Upon startup, it executes
 * the configured GitHub issue search job to find matching issues and optionally sends
 * email notifications with the results.</p>
 *
 * <p>This class serves as the application's bootstrapping point and does not contain
 * business logic. Configuration and component initialization are delegated to Spring Boot
 * and the configured beans (see {@link org.voninc.contribot.config} package).</p>
 */
@SpringBootApplication
public class ContribotApplication {

  /**
   * Starts the Contribot application.
   *
   * <p>This method initializes the Spring Boot application context, loads configuration
   * from application properties, instantiates all managed beans, and triggers the
   * {@link org.voninc.contribot.JobRunner} to execute the GitHub issue search job.</p>
   *
   * @param args command-line arguments (not directly used; can be passed through to Spring Boot)
   */
  public static void main(String[] args) {
    SpringApplication.run(ContribotApplication.class, args);
  }

}
