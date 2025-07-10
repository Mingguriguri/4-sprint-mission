package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.response.CommonResponse;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/login")
@RequiredArgsConstructor
@Tag(name = "🔐 Login", description = "사용자 로그인 관련 API")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "로그인", description = "email과 password로 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인되었습니다",
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
                                                        "id": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                        "username": "mingguriguri",
                                                        "email": "minggurigrui@example.com",
                                                        "profileId": null,
                                                        "createdAt": "2025-07-08T04:31:21.124756Z",
                                                        "updatedAt": "2025-07-08T04:31:21.124757Z",
                                                        "online": true
                                                    },
                                                    "timestamp": "2025-07-09T18:18:00.968114"
                                                }
                                            """
                            )                    )),
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
                                                            "field": "username",
                                                            "message": "공백일 수 없습니다"
                                                        },
                                                        {
                                                            "field": "password",
                                                            "message": "공백일 수 없습니다"
                                                        }
                                                    ],
                                                    "timestamp": "2025-07-09T18:19:23.666912"
                                                }
                                              """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest Example (INVALID 2)",
                                            summary = "사용자가 존재하지 않은 경우",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "유저가 존재하지 않습니다: mingguriguri22",
                                                          "timestamp": "2025-07-09T18:20:46.61426"
                                                        }
                                                      """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest Example (INVALID 3)",
                                            summary = "비밀번호가 일치하지 않은 경우",
                                            value   = """
                                                {
                                                      "success": false,
                                                      "code": 400,
                                                      "message": "Bad Request Exception",
                                                      "data": "비밀번호가 일치하지 않습니다.",
                                                      "timestamp": "2025-07-09T18:20:06.403191"
                                                }
                                              """
                                    )
                            }
                    ))
    })
    public ResponseEntity<CommonResponse<UserResponseDto>> login(@RequestBody @Valid LoginUserDto dto) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, authService.login(dto)));
    }
}
