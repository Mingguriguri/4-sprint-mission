package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "사용자 로그인 관련 API")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "로그인", description = "email과 password로 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 로그인되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
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
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginUserDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
