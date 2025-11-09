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
package org.voninc.contribot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class that provides a customized Jackson {@link ObjectMapper} bean.
 *
 * <p>This configuration ensures proper serialization and deserialization of Java 8+ date and time
 * classes (e.g., {@link java.time.LocalDateTime}, {@link java.time.Instant}) by registering
 * the {@link JavaTimeModule}.</p>
 *
 * <p>Additionally, it disables timestamp-based date serialization
 * ({@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS}) so that dates are written as
 * ISO-8601 formatted strings, which improves readability and interoperability in JSON responses.</p>
 *
 * <p>The configured {@link ObjectMapper} is available as a Spring-managed bean and will be
 * automatically picked up by Spring Boot’s Jackson autoconfiguration.</p>
 */
@Configuration
public class ObjectMapperConfig {

  /**
   * Creates and configures the application-wide {@link ObjectMapper} bean.
   *
   * @return a customized {@link ObjectMapper} with Java Time support and ISO-8601 date formatting
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

}
