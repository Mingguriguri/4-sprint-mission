package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.AllChannelByUserIdResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.response.CommonResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

/*
 * 기존: /v1/channels, PathVariable: channel-id
 * 요구사항: /api/channels, PathVariable: channelId
 * 요구사항에 맞춰 변경하였습니다.
 */
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "🚪Channel", description = "채널 관련 API")
public class ChannelController {
    private final ChannelService channelService;

    /*
    * 기존: POST /v1/channels => body의 channel-type 값에 따라 분기 처리하여 비공개/공개 채널 생성
    * 요구사항: POST /api/channels/private, POST /api/channels/public => 핸들러 메서드 분리하여 채널 생성
    * */
    @PostMapping("/public")
    @Operation(summary = "공개 채널 생성", description = "공개 채널을 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "PUBLIC 채널 생성 DTO (type/name/description)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelCreateDto.class),
                            examples = @ExampleObject(
                                            name    = "PUBLIC 생성 예시",
                                            summary = "공개 채널 생성",
                                            value   = """
                                                        {
                                                          "type": "PUBLIC",
                                                          "name": "공지 채널",
                                                          "description": "공지를 하는 채널입니다."
                                                        }
                                                      """
                                    )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                            name = "Success Example(PUBLIC)",
                                            summary = "공개 채널 생성 성공 예시",
                                            value = """
                                                    {
                                                        "success": true,
                                                        "code": 201,
                                                        "message": "요청이 성공적으로 처리되었습니다.",
                                                        "data": {
                                                            "id": "fb306f45-af59-46b1-adcb-449f9d3dfb04",
                                                            "type": "PUBLIC",
                                                            "name": "개발 기록 챌린지",
                                                            "description": "#📝개발-기록-챌린지 채널에 오신 것을 환영합니다.",
                                                            "userId": null,
                                                            "otherUserId": null,
                                                            "lastMessageSentAt": null
                                                        },
                                                        "timestamp": "2025-07-09T17:11:00.361902"
                                                    }
                                            """
                                    )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name    = "BadRequest - 유효성 검증 실패",
                                            summary = "필드 유효성 검사에 실패한 경우",
                                            value   = """
                                                            {
                                                              "success": false,
                                                              "code": 400,
                                                              "message": "Bad Request Exception",
                                                              "data": "Public 채널 생성 시 name 이 필요합니다.",
                                                              "timestamp": "2025-07-09T17:16:31.902853"
                                                            }
                                                      """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest - 중복 채널",
                                            summary = "이미 존재하는 채널명인 경우(PUBLIC)",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "이미 사용 중인 채널 이름입니다: 개발 기록 챌린지",
                                                          "timestamp": "2025-07-09T17:15:48.807846"
                                                      }
                                                      """
                                    )
                            }
                    ))
    })
    public ResponseEntity<CommonResponse<ChannelResponseDto>> createPublicChannel(@RequestBody @Valid ChannelCreateDto dto) {
        ChannelResponseDto created = channelService.createPublicChannel(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, created));
    }

    @PostMapping("/private")
    @Operation(summary = "비공개 채널 생성", description = "비공개 채널을 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "비공개 채널 생성 DTO (type/userId/otherUserId)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChannelCreateDto.class),
                            examples = @ExampleObject(
                                            name    = "PRIVATE 생성 예시",
                                            summary = "비공개 채널 생성",
                                            value   = """
                                                        {
                                                          "type": "PRIVATE",
                                                          "userId": "55e3a449-2c32-4432-8d0d-28620130a8af",
                                                          "otherUserId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a"
                                                        }
                                                      """
                                    )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                            name = "Success Example(PRIVATE)",
                                            summary = "비공개 채널 성공 예시",
                                            value = """
                                                    {
                                                        "success": true,
                                                        "code": 201,
                                                        "message": "요청이 성공적으로 처리되었습니다.",
                                                        "data": {
                                                            "id": "67b4d79c-d813-4e9a-905d-2cd29d190bf6",
                                                            "type": "PRIVATE",
                                                            "name": null,
                                                            "description": null,
                                                            "userId": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                            "otherUserId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                            "lastMessageSentAt": "2025-07-09T08:11:38.417692Z"
                                                        },
                                                        "timestamp": "2025-07-09T17:11:38.429442"
                                                    }
                                            """
                                    )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name    = "BadRequest - 유효성 검증 실패",
                                            summary = "필드 유효성 검사에 실패한 경우",
                                            value   = """
                                                            {
                                                              "success": false,
                                                              "code": 400,
                                                              "message": "Bad Request Exception",
                                                              "data": "Public 채널 생성 시 name 이 필요합니다.",
                                                              "timestamp": "2025-07-09T17:16:31.902853"
                                                            }
                                                      """
                                    )
                            }
                    ))
    })
    public ResponseEntity<CommonResponse<ChannelResponseDto>> createPrivateChannel(@RequestBody @Valid ChannelCreateDto dto) {
        ChannelResponseDto created = channelService.createPrivateChannel(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, created));
    }

    @GetMapping
    @Operation(summary = "채널 목록 조회", description = "유저 아이디(userId)가 참여하고 있는 전체 채널을 조회합니다.",
            parameters = @Parameter(
                    name        = "userId",
                    in          = ParameterIn.HEADER,
                    description = "사용자 ID (UUID)",
                    required    = true,
                    example     = "ad2343a0-a8ff-47d7-94cb-03681dbff078"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "publicChannels": [
                                                            {
                                                                "id": "fb306f45-af59-46b1-adcb-449f9d3dfb04",
                                                                "type": "PUBLIC",
                                                                "name": "개발 기록 챌린지",
                                                                "description": "#📝개발-기록-챌린지 채널에 오신 것을 환영합니다.",
                                                                "userId": null,
                                                                "otherUserId": null,
                                                                "lastMessageSentAt": null
                                                            },
                                                            {
                                                                "id": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                                "type": "PUBLIC",
                                                                "name": "📒학습-공지",
                                                                "description": "#📒학습-공지 채널의 시작이에요. 학습 관련 사항이 공지되는 채널이에요. 이 채널에 공유되는 소식은 모든 멤버가 꼭 확인해 주세요!",
                                                                "userId": null,
                                                                "otherUserId": null,
                                                                "lastMessageSentAt": null
                                                            }
                                                        ],
                                                        "privateChannels": [
                                                            {
                                                                "id": "73066e40-8ec5-49ea-a982-dcbf5041ef69",
                                                                "type": "PRIVATE",
                                                                "name": null,
                                                                "description": null,
                                                                "userId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                                "otherUserId": "55e3a449-2c32-4432-8d0d-28620130a8af",
                                                                "lastMessageSentAt": "2025-07-09T08:15:37.168959Z"
                                                            },
                                                            {
                                                                "id": "67b4d79c-d813-4e9a-905d-2cd29d190bf6",
                                                                "type": "PRIVATE",
                                                                "name": null,
                                                                "description": null,
                                                                "userId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                                "otherUserId": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                                "lastMessageSentAt": "2025-07-09T08:11:38.417692Z"
                                                            }
                                                        ]
                                                    },
                                                    "timestamp": "2025-07-09T17:33:20.318112"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 사용자(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "채널이  존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Channel with id cc8f67a7-55c8-46f8-a909-b323f14136c4 not found",
                                                    "timestamp": "2025-07-09T17:06:20.219015"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<AllChannelByUserIdResponseDto>> getChannels(@RequestHeader(name = "userId") UUID userId) {
        return ResponseEntity
                .ok(CommonResponse.success(HttpStatus.OK, channelService.findAllByUserId(userId)));
    }

    /*
     * 채널 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다.
     * userId의 경우, 헤더로부터 전달받습니다. (추후 JWT 와 같은 토큰과 같은 용도라고 보시면 좋을 것 같습니다)
     */
    @GetMapping("/{channelId}")
    @Operation(summary = "채널 단일 조회", description = "채널 아이디(channelId)로 채널을 단일 조회합니다.",
            parameters = {
                @Parameter(
                        name        = "channelId",
                        in          = ParameterIn.PATH,
                        description = "채널 ID (UUID)",
                        required    = true,
                        example     = "8fba4d61-84c2-4d84-9808-ded529f5ecca"
                ),
                @Parameter(
                        name        = "userId",
                        in          = ParameterIn.HEADER,
                        description = "사용자 ID (UUID)",
                        required    = true,
                        example     = "028c503a-121d-4a6c-bd25-179a1a5c2bcd"
                )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                        "type": "PUBLIC",
                                                        "name": "공지",
                                                        "description": "공지 채널입니다",
                                                        "userId": null,
                                                        "otherUserId": null,
                                                        "lastMessageSentAt": null
                                                    },
                                                    "timestamp": "2025-07-09T17:07:36.492667"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channelId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "채널이  존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Channel with id cc8f67a7-55c8-46f8-a909-b323f14136c4 not found",
                                                    "timestamp": "2025-07-09T17:06:20.219015"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<ChannelResponseDto>> getChannel(@PathVariable("channelId") UUID channelId,
                                                                         @RequestHeader("userId") UUID userId) {
        return ResponseEntity
                .ok(CommonResponse.success(HttpStatus.OK, channelService.find(channelId, userId)));
    }

    @PatchMapping("/{channelId}")
    @Operation(summary = "채널 부분 수정", description = "채널 아이디(channelId)에 해당하는 채널을 부분적으로 수정합니다.",
            parameters = @Parameter(
                    name        = "channelId",
                    in          = ParameterIn.PATH,
                    description = "채널 ID (UUID)",
                    required    = true,
                    example     = "8fba4d61-84c2-4d84-9808-ded529f5ecca"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                        "type": "PUBLIC",
                                                        "name": "📒학습-공지",
                                                        "description": "#📒학습-공지 채널의 시작이에요. 학습 관련 사항이 공지되는 채널이에요. 이 채널에 공유되는 소식은 모든 멤버가 꼭 확인해 주세요!",
                                                        "userId": null,
                                                        "otherUserId": null,
                                                        "lastMessageSentAt": null
                                                    },
                                                    "timestamp": "2025-07-09T17:20:32.583617"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name    = "BadRequest Example",
                                            summary = "필드 유효성 검사에 실패한 경우",
                                            value   = """
                                                           {
                                                             "success": false,
                                                             "code": 400,
                                                             "message": "유효성 검사 실패",
                                                             "data": [
                                                                 {
                                                                     "field": "name",
                                                                     "message": "공백일 수 없습니다"
                                                                 },
                                                                 {
                                                                     "field": "type",
                                                                     "message": "널이어서는 안됩니다"
                                                                 },
                                                                 {
                                                                     "field": "id",
                                                                     "message": "널이어서는 안됩니다"
                                                                 }
                                                             ],
                                                             "timestamp": "2025-07-09T17:22:48.416379"
                                                           }
                                                      """
                                    ),
                                    // TODO: 중복 채널명에 대해 로직 처리해야 함.
                                    @ExampleObject(
                                            name    = "BadRequest - 중복 채널",
                                            summary = "이미 존재하는 채널명인 경우(PUBLIC)",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "이미 사용 중인 채널 이름입니다: 개발 기록 챌린지",
                                                          "timestamp": "2025-07-09T17:15:48.807846"
                                                      }
                                                      """
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channelId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "채널이  존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Channel with id cc8f67a7-55c8-46f8-a909-b323f14136c4 not found",
                                                    "timestamp": "2025-07-09T17:06:20.219015"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<ChannelResponseDto>> updateChannel(@PathVariable("channelId") UUID channelId,
                                                          @RequestBody @Valid ChannelUpdateDto dto) {
        ChannelResponseDto updated = channelService.update(channelId, dto);
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, updated));
    }

    @DeleteMapping("/{channelId}")
    @Operation(summary = "채널 삭제", description = "채널 아이디(channelId)로 채널을 삭제합니다.",
            parameters = @Parameter(
                        name        = "channelId",
                        in          = ParameterIn.PATH,
                        description = "채널 ID (UUID)",
                        required    = true,
                        example     = "e74bb313-294e-47db-9eae-1d0ea3c3842c"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 삭제되었습니다. (이미 삭제된 경우 404가 뜰 수 있습니다)",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 삭제된 경우 Body에 데이터 없이 204 응답코드만 전달됩니다.",
                                    value = ""
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 채널(channelId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "채널이  존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Channel with id cc8f67a7-55c8-46f8-a909-b323f14136c4 not found",
                                                    "timestamp": "2025-07-09T17:06:20.219015"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<Void>> deleteChannel(@Parameter(name = "channelId", in = ParameterIn.PATH, description = "채널 ID")
                                                                @PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
