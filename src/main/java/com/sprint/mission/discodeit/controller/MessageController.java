package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.response.CommonResponse;
import com.sprint.mission.discodeit.service.MessageService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
@Tag(name = "💬 Message", description = "메시지 관련 API")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @Operation(
            summary = "메시지 생성",
            description = "JSON DTO + 이미지(바이너리)를 동시에 업로드하여 메시지를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MessageCreateDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 201,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "1c0d1342-6542-4afd-9c41-7ef68c353321",
                                                        "channelId": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                        "authorId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                        "content": "첫 번째  공지! 필독! 읽고 나서 체크 이모지 눌러주세요! ",
                                                        "attachmentIds": [
                                                            "59589644-0e18-42f0-970e-0eaaeaa4ff09",
                                                            "6e931d5e-3de0-44e6-8ec1-9261e0e89ca5"
                                                        ],
                                                        "createdAt": "2025-07-09T08:57:09.969043Z",
                                                        "updatedAt": "2025-07-09T08:57:09.969044Z"
                                                    },
                                                    "timestamp": "2025-07-09T17:57:10.010431"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name    = "BadRequest Example (INVALID)",
                                            summary = "필드 유효성 검사에 실패한 경우",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "유효성 검사 실패",
                                                          "data": [
                                                              {
                                                                  "field": "authorId",
                                                                  "message": "널이어서는 안됩니다"
                                                              },
                                                              {
                                                                  "field": "content",
                                                                  "message": "공백일 수 없습니다"
                                                              },
                                                              {
                                                                  "field": "channelId",
                                                                  "message": "널이어서는 안됩니다"
                                                              }
                                                          ],
                                                          "timestamp": "2025-07-09T18:10:15.90603"
                                                      }
                                                      """
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "415", description = "파일 입출력 중 오류가 발생했습니다.",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "FileIOException Example",
                                    summary = "Unsupported Media Type” 또는 서버 I/O 오류",
                                    value = """
                                        {
                                            "success": false,
                                            "code": 415,
                                            "message": "파일 입출력 중 오류가 발생했습니다.",
                                            "data": null,
                                            "timestamp": "2025-07-09T15:42:13.725376"
                                        }
                                    """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<MessageResponseDto>> createMessage(@ModelAttribute @Valid MessageCreateDto dto) {
        MessageResponseDto created = messageService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, created));
    }

    @GetMapping
    @Operation(summary = "특정 채널의 메시지 전체 조회", description = "특정 채널(channel-id)의 메시지 목록을 전체 조회합니다.",
            parameters = @Parameter(
                            name        = "channel-id",
                            in          = ParameterIn.QUERY,
                            description = "채널 ID (UUID)",
                            required    = true,
                            example     = "655d7d4f-609c-4a22-b90a-2ca1a7c49099"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": [
                                                        {
                                                            "id": "1c0d1342-6542-4afd-9c41-7ef68c353321",
                                                            "channelId": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                            "authorId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                            "content": "첫 번째  공지! 필독! 읽고 나서 체크 이모지 눌러주세요! ",
                                                            "attachmentIds": [
                                                                "59589644-0e18-42f0-970e-0eaaeaa4ff09",
                                                                "6e931d5e-3de0-44e6-8ec1-9261e0e89ca5"
                                                            ],
                                                            "createdAt": "2025-07-09T08:57:09.969043Z",
                                                            "updatedAt": "2025-07-09T08:57:09.969044Z"
                                                        },
                                                        {
                                                            "id": "ccbae575-73d3-428b-8d41-4eba6e207e7f",
                                                            "channelId": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                            "authorId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                            "content": "두 번째  공지! 필독! 읽고 나서, ✅ 이모지 눌러주세요! ",
                                                            "attachmentIds": [
                                                                "fe838953-90ef-4a77-89a6-27fa9a79203e",
                                                                "b85886e8-6608-4785-95af-3e539ceb369e"
                                                            ],
                                                            "createdAt": "2025-07-09T09:08:31.851554Z",
                                                            "updatedAt": "2025-07-09T09:08:31.851554Z"
                                                        }
                                                    ],
                                                    "timestamp": "2025-07-09T18:09:12.406703"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<List<MessageResponseDto>>> getMessages(
                                                                    @RequestParam(name = "channel-id") UUID channelId) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, messageService.findAllByChannelId(channelId)));
    }

    /*
     * 메시지 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다
     */
    @GetMapping("/{message-id}")
    @Operation(summary = "메시지 단일 조회", description = "메시지 아이디(message-id)로 메시지를 단일 조회합니다.",
            parameters = @Parameter(
                    name = "message-id",
                    in = ParameterIn.PATH,
                    description = "메시지 ID",
                    required = true,
                    example = "1c0d1342-6542-4afd-9c41-7ef68c353321"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                        "username": "maru",
                                                        "email": "maru@gmail.com",
                                                        "profileId": "f146d333-1cff-4db4-9e32-b45f6f207950",
                                                        "createdAt": "2025-07-08T04:34:06.767515Z",
                                                        "updatedAt": "2025-07-08T04:34:06.767517Z",
                                                        "online": false
                                                    },
                                                    "timestamp": "2025-07-09T16:16:17.146881"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "메시지가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Message with id d77d7333-cc37-4e34-9c3f-849c2c058a77 not found",
                                                    "timestamp": "2025-07-09T17:39:15.977931"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<MessageResponseDto>> getMessage(@PathVariable("message-id") UUID messageId) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, messageService.find(messageId)));
    }

    @PutMapping("/{message-id}")
    @Operation(
            summary = "메시지 수정",
            description = "JSON DTO + 이미지(바이너리)를 동시에 업로드하여 특정 메시지(message-id)를 수정합니다.",
            parameters = @Parameter(
                    name = "message-id",
                    in = ParameterIn.PATH,
                    description = "메시지 ID",
                    required = true,
                    example = "cbd2c76f-0bf1-4927-847a-e13877fbbea1"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MessageUpdateDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "1c0d1342-6542-4afd-9c41-7ef68c353321",
                                                        "channelId": "655d7d4f-609c-4a22-b90a-2ca1a7c49099",
                                                        "authorId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                        "content": "첫 번째  공지입니다. 모두 읽고 나서 체크 이모지(✅) 눌러주세요! ",
                                                        "attachmentIds": [
                                                            "9768f2cc-2bc4-4f56-a41c-07434b1ce7e8"
                                                        ],
                                                        "createdAt": "2025-07-09T08:57:09.969043Z",
                                                        "updatedAt": "2025-07-09T09:15:49.558163Z"
                                                    },
                                                    "timestamp": "2025-07-09T18:15:49.559753"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다",
                    content = @Content(
                            examples = @ExampleObject(
                                            name    = "BadRequest Example (INVALID)",
                                            summary = "필드 유효성 검사에 실패한 경우",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "유효성 검사 실패",
                                                          "data": [
                                                              {
                                                                  "field": "id",
                                                                  "message": "널이어서는 안됩니다"
                                                              },
                                                              {
                                                                  "field": "content",
                                                                  "message": "공백일 수 없습니다"
                                                              }
                                                          ],
                                                          "timestamp": "2025-07-09T18:16:26.728659"
                                                        }
                                                      """
                            )

                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "메시지가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Message with id d77d7333-cc37-4e34-9c3f-849c2c058a77 not found",
                                                    "timestamp": "2025-07-09T17:39:15.977931"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "415", description = "파일 입출력 중 오류가 발생했습니다.",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "FileIOException Example",
                                    summary = "Unsupported Media Type” 또는 서버 I/O 오류",
                                    value = """
                                        {
                                            "success": false,
                                            "code": 415,
                                            "message": "파일 입출력 중 오류가 발생했습니다.",
                                            "data": null,
                                            "timestamp": "2025-07-09T15:42:13.725376"
                                        }
                                    """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<MessageResponseDto>> updateMessage(@PathVariable("message-id") UUID messageId,
                                                  @ModelAttribute @Valid MessageUpdateDto dto) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, messageService.update(dto)));
    }

    @DeleteMapping("/{message-id}")
    @Operation(summary = "메시지 삭제", description = "메시지 아이디(message-id)로 메시지를 삭제합니다.",
            parameters = @Parameter(
                    name = "message-id",
                    in = ParameterIn.PATH,
                    description = "메시지 ID",
                    required = true,
                    example = "1c0d1342-6542-4afd-9c41-7ef68c353321"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 삭제되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 삭제된 경우 Body에 데이터 없이 204 응답코드만 전달됩니다.",
                                    value = ""
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 메시지(message-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "메시지가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "Message with id d77d7333-cc37-4e34-9c3f-849c2c058a77 not found",
                                                    "timestamp": "2025-07-09T17:39:15.977931"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<MessageResponseDto> deleteMessage(@PathVariable("message-id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
