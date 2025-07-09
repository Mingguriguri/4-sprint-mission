package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
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
@Tag(name = "Binary Content", description = "Binary Content 관련 API")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    // 단일 조회
    @GetMapping( "/{binary-content-id}")
    @Operation(summary = "바이너리 컨텐츠 단일 조회", description = "바이너리 컨텐츠를 1개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BinaryContentResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(@PathVariable("binary-content-id") UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    // 여러 개 조회
    @GetMapping
    @Operation(summary = "바이너리 컨텐츠 여러 개 조회", description = "바이너리 컨텐츠를 여러 개 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BinaryContentResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<List<BinaryContentResponseDto>> getBinaryContents(@Parameter(name = "binary-content-id", in = ParameterIn.PATH, description = "바이너리 컨텐츠 ID")
                                                                                @RequestParam("binary-content-ids") List<UUID> binaryContentIds) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

    /*
     * DELETE 는 요구사항에 없었지만 추가해놓았습니다
     */
    @DeleteMapping("/{binary-content-id}")
    @Operation(summary = "바이너리 컨텐츠 삭제", description = "바이너리 컨텐츠를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 삭제되었습니다",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BinaryContentResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "해당 바이너리 컨텐츠(binary-content-id)가 존재하지 않습니다",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    public ResponseEntity<BinaryContentResponseDto> deleteBinaryContent(@Parameter(name = "binary-content-id", in = ParameterIn.PATH, description = "바이너리 컨텐츠 ID")
                                                                            @PathVariable("binary-content-id") UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
