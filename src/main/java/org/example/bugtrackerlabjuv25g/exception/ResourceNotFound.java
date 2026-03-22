package org.example.bugtrackerlabjuv25g.exception;

/**
 * Exception representing a specific scenario where a requested resource cannot be found.
 * <p>
 * This exception is typically used in cases where operations depend on the existence of
 * certain resources, and the requested resource is not available or does not exist. When
 * thrown, it can be handled centrally in a global exception handling mechanism to provide
 * appropriate responses or error messages to the client.
 * <p>
 * Intended to work in conjunction with Spring's {@code @ControllerAdvice}, where an exception
 * handler for {@code ResourceNotFound} can generate a user-friendly response, such as an HTTP
 * 404 Not Found status or an error page.
 */
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }
}
