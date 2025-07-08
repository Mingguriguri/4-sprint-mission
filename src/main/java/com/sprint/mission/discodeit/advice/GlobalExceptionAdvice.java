package com.sprint.mission.discodeit.advice;

import com.sprint.mission.discodeit.exception.FileAccessException;
import com.sprint.mission.discodeit.exception.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNotFound(NoSuchElementException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleBadRequest(IllegalArgumentException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(FileAccessException.class)
    public ResponseEntity handleFileAccessException(FileAccessException e) {
        return ResponseEntity
                .status(HttpStatus.valueOf(e.getExceptionCode().getStatus()))
                .body(new ErrorResponseDto(
                        e.getExceptionCode().getStatus(),
                        e.getExceptionCode().getMessage()));
    }

    // 나머지 예외 처리
    // TODO: 추후 ErrorResponse 만들어서 response에 담아서 보내기
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }
    
}
