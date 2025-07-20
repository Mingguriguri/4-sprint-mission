package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
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
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "🖇️ Binary Content", description = "Binary Content 관련 API")
public interface BinaryContentApi {
    @Operation(summary = "첨부 파일 조회", description = "바이너리 컨텐츠를 1개 조회합니다.",
            parameters = @Parameter(
                    name        = "binaryContentId",
                    in          = ParameterIn.PATH,
                    description = "조회할 첨부파일 ID (UUID)",
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
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binaryContentId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 바이너리 컨텐츠(binaryContentId)가 존재하지 않은 경우 예시",
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
    ResponseEntity<BinaryContentResponseDto> getBinaryContent(UUID binaryContentId);

    @Operation(
            summary     = "여러 첨부 파일 조회",
            description = "쉼표(,) 로 구분된 binaryContentIds 쿼리 파라미터를 받아 여러 건을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "binaryContentIds",
                            in   = ParameterIn.QUERY,
                            description = "조회할 첨부 파일 ID 목록 (UUID)",
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
            @ApiResponse(responseCode = "404", description = "해당 첨부 파일(binaryContentId)이 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "NotFound - 해당 첨부파일(binaryContentId)이 존재하지 않은 경우 예시",
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
     ResponseEntity<List<BinaryContentResponseDto>> getBinaryContents(List<UUID> binaryContentIds);
}
