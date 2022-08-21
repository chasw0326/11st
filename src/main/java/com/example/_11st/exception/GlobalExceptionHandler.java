package com.example._11st.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    @AllArgsConstructor
    static class ErrorResult<T> {
        private T message;

        public static <T> ErrorResult from(T exceptionMessage) {
            return new ErrorResult<>(exceptionMessage);
        }
    }

    private <T> ResponseEntity<ErrorResult<T>> createErrorResponse(T exception, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ErrorResult.from(exception), headers, status);
    }

    // @Valid exception catch
    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentConversionNotSupportedException.class})
    public ResponseEntity<?> validExceptionHandler(BindException ex) {
        log.warn("valid Error: {}", ex.getMessage());
        ValidExceptionDTO dto = ValidExceptionDTO.toDto(ex);
        return createErrorResponse(dto, HttpStatus.CONFLICT);
    }

    // for ValidateUtil
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> validateUtilHandler(ConstraintViolationException ex){
        log.warn("validate Error: {}", ex.getMessage());
        String message = ex.getMessage();
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    // Validation(@Valid 제외) exception catch
    @ExceptionHandler({
            IllegalArgumentException.class, IllegalStateException.class,
            TypeMismatchException.class, HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class, EntityNotFoundException.class
    })
    public ResponseEntity<?> badRequestExceptionHandler(Exception ex) {
        log.warn("Bad request exception occurred: {}", ex.getMessage());
        String message = ex.getMessage();
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<?> exceptionHandler(Exception ex) {
        String message = ex.getMessage();
        log.error("Unexpected Error: {}", message, ex);
        String clientMessage = "알 수 없는 에러가 발생했습니다. 서버 관리자에게 문의하세요.";
        return createErrorResponse(clientMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}