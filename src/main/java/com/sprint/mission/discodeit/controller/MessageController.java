package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.service.MessageService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Message", description = "메시지 관련 API")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages")
    @Operation(summary = "메시지 생성", description = "메시지를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "415", description = "파일 입출력 중 오류가 발생했습니다.",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
    })
    public ResponseEntity<MessageResponseDto> createMessage(@ModelAttribute @Valid MessageCreateDto dto) {
        MessageResponseDto created = messageService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/channels/{channel-id}/messages")
    @Operation(summary = "특정 채널의 메시지 전체 조회", description = "특정 채널(channel-id)의 메시지 목록을 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<List<MessageResponseDto>> getMessages(@Parameter(name = "channel-id", in = ParameterIn.PATH, description = "채널 ID")
                                                                    @PathVariable("channel-id") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    /*
     * 메시지 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다
     */
    @GetMapping("messages/{message-id}")
    @Operation(summary = "메시지 단일 조회", description = "메시지 아이디(message-id)로 메시지를 단일 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<MessageResponseDto> getMessage(@Parameter(name = "message-id", in = ParameterIn.PATH, description = "메시지 ID")
                                                             @PathVariable("message-id") UUID messageId) {
        return ResponseEntity.ok(messageService.find(messageId));
    }

    @PutMapping("messages/{message-id}")
    @Operation(summary = "메시지 수정", description = "메시지 아이디(message-id)로 메시지를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "415", description = "파일 입출력 중 오류가 발생했습니다.",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (파일 처리 실패 등)",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    ))
    })
    public ResponseEntity<MessageResponseDto> updateMessage(@Parameter(name = "message-id", in = ParameterIn.PATH, description = "메시지 ID")
                                                                @PathVariable("message-id") UUID messageId,
                                                  @ModelAttribute @Valid MessageUpdateDto dto) {
        return ResponseEntity.ok(messageService.update(dto));
    }

    @DeleteMapping("messages/{message-id}")
    @Operation(summary = "메시지 삭제", description = "메시지 아이디(message-id)로 메시지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 삭제되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<MessageResponseDto> deleteMessage(@Parameter(name = "message-id", in = ParameterIn.PATH, description = "메시지 ID")
                                                                @PathVariable("message-id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
