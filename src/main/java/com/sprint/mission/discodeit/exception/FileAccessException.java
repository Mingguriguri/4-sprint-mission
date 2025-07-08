package com.sprint.mission.discodeit.exception;

import lombok.Getter;

public class FileAccessException extends RuntimeException {

    @Getter
    private ExceptionCode exceptionCode;

    public FileAccessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
