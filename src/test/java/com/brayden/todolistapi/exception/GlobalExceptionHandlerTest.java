package com.brayden.todolistapi.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleException_returnsConfiguredStatusAndMessage() {
        ApiException ex = new ApiException("bad request", HttpStatus.BAD_REQUEST);

        ResponseEntity<?> response = handler.handleException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("bad request", ((ErrorResponse) response.getBody()).getError());
    }

    @Test
    void handleValidationException_returnsFirstFieldError() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "title", "must not be blank"));

        Method method = DummyValidationTarget.class.getDeclaredMethod("target", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<?> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("title: must not be blank", ((ErrorResponse) response.getBody()).getError());
    }

    private static class DummyValidationTarget {
        @SuppressWarnings("unused")
        void target(String value) {
        }
    }
}
