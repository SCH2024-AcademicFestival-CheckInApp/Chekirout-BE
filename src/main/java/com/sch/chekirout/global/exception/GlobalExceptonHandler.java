package com.sch.chekirout.global.exception;

import com.sch.chekirout.common.exception.*;
import com.sch.chekirout.global.presentation.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptonHandler {

    @ExceptionHandler(value = {CustomNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(final CustomNotFoundException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CustomBadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(final CustomBadRequestException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(final CustomForbiddenException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {CustomUnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final CustomUnauthorizedException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {CustomDuplicatedException.class})
    public ResponseEntity<ErrorResponse> handleDuplicatedException(final CustomDuplicatedException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            DateTimeException.class
    })
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(final DateTimeException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse("DateTime 형식이 잘못되었습니다. 서버 관리자에게 문의해주세요.");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
