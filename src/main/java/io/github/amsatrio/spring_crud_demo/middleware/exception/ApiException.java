package io.github.amsatrio.spring_crud_demo.middleware.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import io.github.amsatrio.spring_crud_demo.dto.response.Response;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class ApiException {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final Response<Object> response = new Response<>();

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Response<Object>> handleInsufficientAuthenticationException(InsufficientAuthenticationException exception) {

        response.setStatus(401);
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("unauthenticated");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Response<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {

        response.setStatus(403);
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("unauthorized");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<Object>> handleRuntimeException(RuntimeException exception) {
        log.error("message:", exception);

        response.setStatus(400);
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("RuntimeException");
        response.setData(exception.getMessage());
        if (exception.getMessage() != null) {
            response.setData(exception.getMessage());

            if(exception.getMessage().toString().equalsIgnoreCase("access denied")){
                response.setStatus(403);
                response.setMessage("forbidden");
                response.setData(null);
            }
        }

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Response<Object>> handleClientException(HttpClientErrorException exception) {
        response.setStatus(exception.getStatusCode().value());
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("error");
        response.setData(exception.getMessage());
        if (exception.getMessage() != null) {
            response.setData(exception.getMessage().substring(4));
        }

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<Object>> handleClientException400(HttpClientErrorException exception) {
        response.setStatus(exception.getStatusCode().value());
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("error");
        response.setData(exception.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<Object>> handleClientException404(HttpClientErrorException exception) {
        response.setStatus(exception.getStatusCode().value());
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("error");
        response.setData(exception.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<Response<Object>> handleClientException429(HttpClientErrorException exception) {
        response.setStatus(exception.getStatusCode().value());
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("error");
        response.setData(exception.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
