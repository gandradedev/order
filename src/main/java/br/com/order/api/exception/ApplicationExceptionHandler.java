package br.com.order.api.exception;

import br.com.order.api.exception.representation.ErrorMessageResponse;
import br.com.order.domain.item.exception.ItemNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleItemNotFound(final ItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorMessageResponse(e.getMessage())
        );
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e,
                                                               final HttpHeaders headers,
                                                               final HttpStatusCode statusCode,
                                                               final WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String errorMessage = fieldError.getDefaultMessage();
            String fieldName = fieldError.getField();
            errorMessages.add(fieldName + ": " + errorMessage);
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}
