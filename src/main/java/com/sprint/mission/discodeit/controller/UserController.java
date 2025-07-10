package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.response.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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

/*
 * 기존: /v1/users, PathVariable: user-id
 * 요구사항: /api/users, PathVariable: userId
 * 요구사항에 맞춰 변경하였습니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "🙂 User", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    @Operation(
            summary = "사용자 생성",
            description = "JSON DTO + 프로필 이미지(바이너리)를 동시에 업로드하여 사용자를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UserCreateDto.class)
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
                                                        "id": "75591592-cf73-40b0-aa93-9f01976fee79",
                                                        "username": "maru2",
                                                        "email": "maru2@gmail.com",
                                                        "profileId": "76d60241-5f9c-451f-9fc4-c826fc529c9a",
                                                        "createdAt": "2025-07-09T07:14:44.175193Z",
                                                        "updatedAt": "2025-07-09T07:14:44.175194Z",
                                                        "online": true
                                                    },
                                                    "timestamp": "2025-07-09T16:14:44.185429"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다 (중복 사용자, 유효성 실패 등)",
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
                                                                    "field": "username",
                                                                    "message": "공백일 수 없습니다"
                                                                },
                                                                {
                                                                    "field": "email",
                                                                    "message": "공백일 수 없습니다"
                                                                }
                                                            ],
                                                            "timestamp": "2025-07-09T16:27:48.716149"
                                                        }
                                                      """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest Example",
                                            summary = "이미 존재하는 사용자명인 경우 (DUPLICATE)",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "This username maru is already in use",
                                                          "timestamp": "2025-07-09T15:42:13.725376"
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
                    )),
    })
    public ResponseEntity<CommonResponse<UserResponseDto>> create(@ModelAttribute @Valid UserCreateDto dto) {
        UserResponseDto created = userService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, created));
    }

    @GetMapping
    @Operation(summary = "전체 사용자 조회", description = "전체 사용자를 조회합니다")
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
                                                    "data": [
                                                        {
                                                            "id": "e5fb994a-0ec7-442f-8214-92919c5fedd6",
                                                            "username": "minggu",
                                                            "email": "minggu@test.com",
                                                            "profileId": null,
                                                            "createdAt": "2025-06-27T01:03:43.553057Z",
                                                            "updatedAt": "2025-06-27T01:03:43.553058Z",
                                                            "online": false
                                                        },
                                                        {
                                                            "id": "30055583-d546-4f29-8318-b791478d3673",
                                                            "username": "minjeong",
                                                            "email": "minjeong@test.com",
                                                            "profileId": null,
                                                            "createdAt": "2025-06-27T01:03:43.552159Z",
                                                            "updatedAt": "2025-06-27T01:03:43.552159Z",
                                                            "online": false
                                                        },
                                                        {
                                                            "id": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                            "username": "mingguriguri",
                                                            "email": "minggurigrui@example.com",
                                                            "profileId": null,
                                                            "createdAt": "2025-07-08T04:31:21.124756Z",
                                                            "updatedAt": "2025-07-08T04:31:21.124757Z",
                                                            "online": false
                                                        },
                                                        {
                                                            "id": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                            "username": "maru",
                                                            "email": "maru@gmail.com",
                                                            "profileId": "f146d333-1cff-4db4-9e32-b45f6f207950",
                                                            "createdAt": "2025-07-08T04:34:06.767515Z",
                                                            "updatedAt": "2025-07-08T04:34:06.767517Z",
                                                            "online": false
                                                        }
                                                    ],
                                                    "timestamp": "2025-07-09T16:20:33.593708"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<List<UserResponseDto>>> getUsers() {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, userService.findAll()));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "특정 사용자 조회", description = "특정 사용자(userId)를 단일 조회합니다.",
            parameters = @Parameter(
                            name = "userId",
                            in  = ParameterIn.PATH,
                            description = "사용자 ID (UUID)",
                            required = true,
                            example = "ad2343a0-a8ff-47d7-94cb-03681dbff078"
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
            @ApiResponse(responseCode = "404", description = "해당 사용자(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 사용자(userId)가 존재하지 않은 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "User with id 55e3a449-2c32-4432-8d0d-28620130a8af not found",
                                                    "timestamp": "2025-07-09T16:18:18.43085"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<UserResponseDto>> getUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, userService.find(userId)));
    }


    @PatchMapping("/{userId}")
    @Operation(
            summary = "사용자 부분 수정",
            description = "JSON DTO + 프로필 이미지(바이너리)를 업로드하여 특정 사용자(userId)를 부분적으로 수정합니다.",
            parameters = @Parameter(
                    name = "userId",
                    in = ParameterIn.PATH,
                    description = "사용자 ID",
                    required = true,
                    example  = "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UserUpdateDto.class)
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
                                                        "id": "2c2018f3-5ba8-490f-aef9-4d4521de0657",
                                                        "username": "minjeong2",
                                                        "email": "minjeong@gmail.com",
                                                        "profileId": "be026d40-6b34-4583-8fa2-65ab66a08291",
                                                        "createdAt": "2025-06-27T01:03:43.582450Z",
                                                        "updatedAt": "2025-07-09T07:27:01.020779Z",
                                                        "online": false
                                                    },
                                                    "timestamp": "2025-07-09T16:27:01.022713"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다 (중복 사용자)",
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
                                                                    "field": "id",
                                                                    "message": "널이어서는 안됩니다"
                                                                },
                                                                {
                                                                    "field": "username",
                                                                    "message": "공백일 수 없습니다"
                                                                },
                                                                {
                                                                    "field": "email",
                                                                    "message": "공백일 수 없습니다"
                                                                }
                                                            ],
                                                            "timestamp": "2025-07-09T16:27:48.716149"
                                                        }
                                                      """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest - 중복 사용자",
                                            summary = "이미 존재하는 사용자명인 경우",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "This username maru is already in use",
                                                          "timestamp": "2025-07-09T15:42:13.725376"
                                                        }
                                                      """
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "해당 사용자(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "사용자가 존재하지 않을 경우 Not Found",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "User with id 61604874-42a0-408b-a3a1-42eb300fbf8f not found",
                                                    "timestamp": "2025-07-09T16:22:48.540675"
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
    public ResponseEntity<CommonResponse<UserResponseDto>> updateUser(@PathVariable("userId") UUID userId,
                                                      @ModelAttribute  UserUpdateDto dto) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, userService.update(userId, dto)));
    }


    @PatchMapping("/{userId}/userStatus")
    @Operation(summary = "사용자의 접속 시간 업데이트", description = "특정 사용자(userId)의 마지막으로 확인된 접속 시간을 업데이트합니다.",
            parameters = @Parameter(
                            name = "userId",
                            in = ParameterIn.PATH,
                            description = "사용자 ID (UUID)",
                            required = true,
                            example = "ad2343a0-a8ff-47d7-94cb-03681dbff078"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "접속 시간이 정상적으로 갱신되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 업데이트된 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "2c2018f3-5ba8-490f-aef9-4d4521de0657",
                                                        "username": "minjeong2",
                                                        "email": "minjeong@gmail.com",
                                                        "profileId": "be026d40-6b34-4583-8fa2-65ab66a08291",
                                                        "createdAt": "2025-06-27T01:03:43.582450Z",
                                                        "updatedAt": "2025-07-09T07:27:01.020779Z",
                                                        "online": true
                                                    },
                                                    "timestamp": "2025-07-09T16:29:57.87799"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 사용자(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "사용자가 존재하지 않을 경우 Not Found",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "User with id 61604874-42a0-408b-a3a1-42eb300fbf8f not found",
                                                    "timestamp": "2025-07-09T16:22:48.540675"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<UserResponseDto>> touchOnline(@PathVariable("userId") UUID userId) {
        userStatusService.updateByUserId(userId);
        UserResponseDto response = userService.find(userId);
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, response));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "특정 사용자 삭제", description = "특장 사용자(userId)를 삭제합니다",
            parameters = @Parameter(
                            name = "userId",
                            in = ParameterIn.PATH,
                            description = "사용자 ID (UUID)",
                            required = true,
                            example = "e5fb994a-0ec7-442f-8214-92919c5fedd6"
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
            @ApiResponse(responseCode = "404", description = "해당 사용자(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "사용자가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "User with id 61604874-42a0-408b-a3a1-42eb300fbf8f not found",
                                                    "timestamp": "2025-07-09T16:22:48.540675"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
