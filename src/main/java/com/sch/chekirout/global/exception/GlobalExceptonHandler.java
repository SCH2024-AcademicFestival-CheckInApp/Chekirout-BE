package com.sch.chekirout.global.exception;

import com.sch.chekirout.common.exception.*;
import com.sch.chekirout.global.presentation.ErrorResponse;
import com.sch.chekirout.user.exception.EmailAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

import static com.sch.chekirout.common.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptonHandler {

    @ExceptionHandler(value = {CustomNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(final CustomNotFoundException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CustomBadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(final CustomBadRequestException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(final CustomForbiddenException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {CustomUnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(final CustomUnauthorizedException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {CustomDuplicatedException.class})
    public ResponseEntity<ErrorResponse> handleDuplicatedException(final CustomDuplicatedException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            DateTimeException.class
    })
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(final DateTimeException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "DateTime 형식이 잘못되었습니다. 서버 관리자에게 문의해주세요.",
                INVALID_DATE_TIME_FORMAT,
                INVALID_DATE_TIME_FORMAT.getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                INTERNAL_SERVER_ERROR.getMessage(),
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(final EmailAlreadyExists exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getErrorCode().getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);  // 400 Bad Request
    }


}
