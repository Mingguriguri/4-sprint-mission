package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.response.CommonResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/binary-contents")
@RequiredArgsConstructor
@Tag(name = "🖇️ Binary Content", description = "Binary Content 관련 API")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    // 단일 조회
    @GetMapping( "/{binary-content-id}")
    @Operation(summary = "바이너리 컨텐츠 단일 조회", description = "바이너리 컨텐츠를 1개 조회합니다.",
            parameters = @Parameter(
                    name        = "binary-content-id",
                    in          = ParameterIn.PATH,
                    description = "바이너리 컨텐츠 ID (UUID)",
                    required    = true,
                    example     = "f146d333-1cff-4db4-9e32-b45f6f207950"
            ))
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
                                                        "id": "f146d333-1cff-4db4-9e32-b45f6f207950",
                                                        "bytes": "/9j/2wCEAAEBAQEBAQEBAQECAQEBAgICAQECAg...kRRSIov/9k=",
                                                        "type": "PROFILE",
                                                        "createdAt": "2025-07-08T04:34:06.798136Z"
                                                    },
                                                    "timestamp": "2025-07-09T18:29:14.490449"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않은 경우 예시",
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
    public ResponseEntity<CommonResponse<BinaryContentResponseDto>> getBinaryContent(@PathVariable("binary-content-id") UUID binaryContentId) {
        return ResponseEntity.ok(CommonResponse.success(
                HttpStatus.OK,
                binaryContentService.find(binaryContentId)
        ));
    }

    // 여러 개 조회
    @GetMapping
    @Operation(
            summary     = "바이너리 컨텐츠 여러 개 조회",
            description = "쉼표(,) 로 구분된 binary-content-ids 쿼리 파라미터를 받아 여러 건을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "binary-content-ids",
                            in   = ParameterIn.QUERY,
                            description = "검색할 바이너리 컨텐츠 ID 목록 (UUID)",
                            required = true,
                            schema = @Schema(
                                    type  = "array",
                                    // example 에는 JSON array 형태로라도 넣어주면 UI 상에 배열 예시가 뜹니다.
                                    example = "[\"fe838953-90ef-4a77-89a6-27fa9a79203e\", \"216dbce6-b503-4cf0-aa3f-f71ff01ddf8b\"]"
                            ),
                            style   = ParameterStyle.FORM,
                            explode = Explode.FALSE    // explode=false 이면 쉼표(CSV) 구분으로 인식
                    )
            }
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
                                                            "id": "fe838953-90ef-4a77-89a6-27fa9a79203e",
                                                            "bytes": "/9j/4AAQSkZJRgABAQAAAQABAAD/...b7Lm7r/dC835URVkHD1/maP//Z",
                                                            "type": "MESSAGE",
                                                            "createdAt": "2025-07-09T09:08:31.852110Z"
                                                        },
                                                        {
                                                            "id": "b85886e8-6608-4785-95af-3e539ceb369e",
                                                            "bytes": "/9j/4AAQSkZJRgABAQAAAQABAAD/...AIVYICrmeQH+Q3CszhHLqCh7D/9k=",
                                                            "type": "MESSAGE",
                                                            "createdAt": "2025-07-09T09:08:31.859162Z"
                                                        }
                                                    ],
                                                    "timestamp": "2025-07-09T18:30:16.61837"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않은 경우 예시",
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
    public ResponseEntity<CommonResponse<List<BinaryContentResponseDto>>> getBinaryContents(@RequestParam("binary-content-ids") List<UUID> binaryContentIds) {
        return ResponseEntity.ok(CommonResponse.success(
                HttpStatus.OK,
                binaryContentService.findAllByIdIn(binaryContentIds)
        ));
    }

    /*
     * DELETE 는 요구사항에 없었지만 추가해놓았습니다
     */
    @DeleteMapping("/{binary-content-id}")
    @Operation(summary = "바이너리 컨텐츠 삭제", description = "바이너리 컨텐츠를 삭제합니다.",
            parameters = @Parameter(
                    name        = "binary-content-id",
                    in          = ParameterIn.PATH,
                    description = "바이너리 컨텐츠 ID (UUID)",
                    required    = true,
                    example     = "3d7ac88a-4741-4a1a-9dec-875be29e6552"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 삭제되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "SuccessExample",
                                    summary = "성공 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 204,
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
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않은 경우 예시",
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
    public ResponseEntity<BinaryContentResponseDto> deleteBinaryContent(@Parameter(name = "binary-content-id", in = ParameterIn.PATH, description = "바이너리 컨텐츠 ID")
                                                                            @PathVariable("binary-content-id") UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
