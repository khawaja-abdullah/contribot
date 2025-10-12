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
package org.voninc.contribot.exception;

/**
 * A custom unchecked exception for errors occurring within the Contribot application.
 * <p>
 * This exception serves as a central point for handling unexpected failures that are generally non-recoverable at the
 * point of origin, such as critical configuration errors or unexpected API failures.
 */
public class ContribotRuntimeException extends RuntimeException {

  /**
   * Constructs a new ContribotRuntimeException with the specified detail message.
   *
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   */
  public ContribotRuntimeException(String message) {
    super(message);
  }

  /**
   * Constructs a new ContribotRuntimeException with the specified detail message and cause.
   *
   * @param message The detail message.
   * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method).
   * (A {@code null} value is permitted, and indicates that the cause is unknown or non-existent).
   */
  public ContribotRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
