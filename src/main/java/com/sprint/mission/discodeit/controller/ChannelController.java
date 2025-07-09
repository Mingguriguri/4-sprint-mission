package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.AllChannelByUserIdResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
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

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Channel", description = "채널 관련 API")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/channels")
    @Operation(summary = "채널 생성", description = "channel-type에 따라 비공개 채널 또는 공개 채널을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생",
                    content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChannelResponseDto.class)
            ))
    })
    public ResponseEntity<ChannelResponseDto> createChannel(@Parameter(name = "channel-type", in = ParameterIn.QUERY, description = "채널 타입")
                                                                @RequestParam("channel-type") ChannelType type,
                                                            @RequestBody @Valid ChannelCreateDto dto) {
        ChannelResponseDto created = new ChannelResponseDto();
        switch (type) {
            case PUBLIC:
                created = channelService.createPublicChannel(dto);
            break;
            case PRIVATE:
                created = channelService.createPrivateChannel(dto);
                break;
            default:
                break;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/users/{user-id}/channels")
    @Operation(summary = "채널 목록 조회", description = "유저 아이디(user-id)가 참여하고 있는 전체 채널을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AllChannelByUserIdResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "404", description = "해당 사용자(user-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<AllChannelByUserIdResponseDto> getChannels(@Parameter(name = "user-id", in = ParameterIn.PATH, description = "사용자 ID")
                                                                         @PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

    /*
     * 채널 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다.
     * userId의 경우, 헤더로부터 전달받습니다. (추후 JWT 와 같은 토큰과 같은 용도라고 보시면 좋을 것 같습니다)
     */
    @GetMapping("channels/{channel-id}")
    @Operation(summary = "채널 단일 조회", description = "채널 아이디(channel-id)로 채널을 단일 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channel-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ChannelResponseDto> getChannel(@Parameter(name = "channel-id", in = ParameterIn.PATH, description = "채널 ID")
                                                             @PathVariable("channel-id") UUID channelId,
                                                         @Parameter(name = "user-id", in = ParameterIn.HEADER, description = "사용자 ID")
                                                             @RequestHeader("user-id") UUID userId) {
        return ResponseEntity.ok(channelService.find(channelId, userId));
    }

    @PutMapping("channels/{channel-id}")
    @Operation(summary = "채널 수정", description = "채널 아이디(channel-id)로 채널을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channel-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (파일 처리 실패 등)",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ChannelResponseDto> updateChannel(@Parameter(name = "channel-id", in = ParameterIn.PATH, description = "채널 ID")
                                                                @PathVariable("channel-id") UUID channelId,
                                                          @RequestBody @Valid ChannelUpdateDto dto) {
        return ResponseEntity.ok(channelService.update(dto));
    }

    @DeleteMapping("channels/{channel-id}")
    @Operation(summary = "채널 삭제", description = "채널 아이디(channel-id)로 채널을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channel-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (파일 처리 실패 등)",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<ChannelResponseDto> deleteChannel(@Parameter(name = "channel-id", in = ParameterIn.PATH, description = "채널 ID")
                                                                @PathVariable("channel-id") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
