package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @Operation(summary = "사용자 생성", description = "사용자를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다 (중복 사용자, 유효성 실패 등)",
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
                    ))
    })
    public ResponseEntity<UserResponseDto> create(@ModelAttribute @Valid UserCreateDto dto) {
        UserResponseDto created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Operation(summary = "전체 사용자 조회", description = "전체 사용자를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{user-id}")
    @Operation(summary = "특정 사용자 조회", description = "특정 사용자(user-id)를 단일 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
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
    public ResponseEntity<UserResponseDto> getUser(@Parameter(name = "user-id", in = ParameterIn.PATH, description = "사용자 ID")
                                                       @PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(userService.find(userId));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{user-id}")
    @Operation(summary = "특정 사용자 수정", description = "특정 사용자(user-id)를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다 (중복 사용자명, 유효성 실패 등)",
                    content = @Content(
                            mediaType = "multipart/form-data"
                    )),
            @ApiResponse(responseCode = "404", description = "해당 사용자(user-id)가 존재하지 않습니다",
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
    public ResponseEntity<UserResponseDto> updateUser(@Parameter(name = "user-id", in = ParameterIn.PATH, description = "사용자 ID")
                                                          @PathVariable("user-id") UUID userId,
                                                      @ModelAttribute @Valid  UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(dto));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{user-id}/userstatus")
    @Operation(summary = "사용자의 접속 시간 업데이트", description = "특정 사용자(user-id)의 마지막으로 확인된 접속 시간을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "접속 시간이 정상적으로 갱신되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
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
    public ResponseEntity<UserResponseDto> touchOnline(@Parameter(name = "user-id", in = ParameterIn.PATH, description = "사용자 ID")
                                                           @PathVariable("user-id") UUID userId) {
        userStatusService.updateByUserId(userId);
        UserResponseDto response = userService.find(userId);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{user-id}")
    @Operation(summary = "특정 사용자 삭제", description = "특장 사용자(user-id)를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 삭제되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
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
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("user-id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
