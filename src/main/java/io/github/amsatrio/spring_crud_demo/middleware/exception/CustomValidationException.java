package io.github.amsatrio.spring_crud_demo.middleware.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.amsatrio.spring_crud_demo.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomValidationException {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final Response<Object> response = new Response<>();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        response.setStatus(400);
        response.setDate(new Date());
        response.setPath(httpServletRequest.getRequestURI());
        response.setMessage("handleMethodArgumentNotValidException");
        response.setData(exception.getMessage());
        response.setData(errors);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
