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
