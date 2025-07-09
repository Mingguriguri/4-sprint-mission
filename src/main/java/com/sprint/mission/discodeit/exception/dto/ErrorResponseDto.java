package com.sprint.mission.discodeit.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
// TODO: 지우기
@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String message;
}
