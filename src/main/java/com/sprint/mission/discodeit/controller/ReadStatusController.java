package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "ReadStatus", description = "ReadStatus(사용자가 채널 별 마지막으로 메시지를 읽은 시간) 관련 API")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @Operation(summary = "ReadStatus 생성", description = "ReadStatus (사용자와 채널 관계)를 생성하여 사용자가 채널별로 마지막으로 메시지를 읽은 시간을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatusResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody @Valid ReadStatusCreateDto dto) {
        ReadStatusResponseDto created = readStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{read-status-id}")
    @Operation(summary = "ReadStatus (마지막으로 읽은 시간) 조회", description = "특정 read-status-id를 통해 마지막으로 메시지를 읽은 시간을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatusResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ReadStatusResponseDto> getReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                   @PathVariable("read-status-id") UUID readStatusId) {
        return ResponseEntity.ok(readStatusService.find(readStatusId));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{read-status-id}")
    @Operation(summary = "ReadStatus (마지막으로 읽은 시간)을 업데이트", description = "특정 read-status-id를 통해 마지막으로 메시지를 읽은 시간을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatusResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ReadStatusResponseDto> updateReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                      @PathVariable("read-status-id") UUID readStatusId) {
        return ResponseEntity.ok(readStatusService.update(new ReadStatusUpdateDto(readStatusId)));
    }

    /*
    * DELETE 는 요구사항에 없었지만 추가해놓았습니다
    */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{read-status-id}")
    @Operation(summary = "ReadStatus 삭제", description = "ReadStatus을 (사용자와 채널 관계를 통한 마지막으로 읽은 시간) 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatusResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ReadStatusResponseDto> deleteReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                      @PathVariable("read-status-id") UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
