package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.response.CommonResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/read-statuses")
@RequiredArgsConstructor
@Validated
@Tag(name = "👁️ ReadStatus", description = "ReadStatus(사용자가 채널 별 마지막으로 메시지를 읽은 시간) 관련 API")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    @Operation(summary = "ReadStatus 생성", description = "ReadStatus (사용자와 채널 관계)를 생성하여 사용자가 채널별로 마지막으로 메시지를 읽은 시간을 생성합니다.")
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
                                                        "id": "d7044e4d-58ff-4c17-9f27-44d4bfcd5973",
                                                        "lastReadAt": "2025-07-09T13:21:46.307932Z",
                                                        "userId": "2fd4b22a-2bda-4503-9adf-02f803cce082",
                                                        "channelId": "3d0b4ba7-5b04-4287-9fe7-3a69950ad7c1"
                                                    },
                                                    "timestamp": "2025-07-09T22:21:46.326786"
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
                                                              "message": "유효성 검사 실패",
                                                              "data": [
                                                                  {
                                                                      "field": "userId",
                                                                      "message": "널이어서는 안됩니다"
                                                                  },
                                                                  {
                                                                      "field": "channelId",
                                                                      "message": "널이어서는 안됩니다"
                                                                  }
                                                              ],
                                                              "timestamp": "2025-07-09T22:24:10.576419"
                                                          }
                                                      """
                                    ),
                                    @ExampleObject(
                                            name    = "BadRequest - 중복 채널",
                                            summary = "이미 존재하는 경우",
                                            value   = """
                                                        {
                                                          "success": false,
                                                          "code": 400,
                                                          "message": "Bad Request Exception",
                                                          "data": "ReadStatus already exists for user 2fd4b22a-2bda-4503-9adf-02f803cce082 and channel 3d0b4ba7-5b04-4287-9fe7-3a69950ad7c1",
                                                          "timestamp": "2025-07-09T22:24:22.696401"
                                                        }
                                                      """
                                    )
                            }
                    ))
    })
    public ResponseEntity<CommonResponse<ReadStatusResponseDto>> createReadStatus(@RequestBody @Valid ReadStatusCreateDto dto) {
        ReadStatusResponseDto created = readStatusService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED, created));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{read-status-id}")
    @Operation(
            summary = "ReadStatus (마지막으로 읽은 시간) 조회",
            description = "특정 read-status-id를 통해 마지막으로 메시지를 읽은 시간을 조회합니다",
            parameters = @Parameter(
                    name        = "read-status-id",
                    in          = ParameterIn.PATH,
                    description = "ReadStatus (마지막으로 읽은 시간) ID (UUID)",
                    required    = true,
                    example     = "16e245c7-3848-448f-a3dc-ca702ce9a491"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 조회하였습니다.",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 200,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "9e9d828f-29ec-4be6-a5ef-4ad7f0487628",
                                                        "lastReadAt": "2025-07-09T08:15:37.178888Z",
                                                        "userId": "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a",
                                                        "channelId": "73066e40-8ec5-49ea-a982-dcbf5041ef69"
                                                    },
                                                    "timestamp": "2025-07-09T22:18:31.444057"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "해당 ReadStatus(read-status-id)가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "ReadStatus with id 035433fc-83bc-4748-8675-bcc09ee953ea not found",
                                                    "timestamp": "2025-07-09T21:25:55.133318"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<ReadStatusResponseDto>> getReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                   @PathVariable("read-status-id") UUID readStatusId) {
        System.out.println(readStatusId + "readStatusId");
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, readStatusService.find(readStatusId)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{read-status-id}")
    @Operation(
            summary = "ReadStatus (마지막으로 읽은 시간)을 업데이트",
            description = "특정 read-status-id를 통해 마지막으로 메시지를 읽은 시간을 업데이트합니다.",
            parameters = @Parameter(
                    name        = "read-status-id",
                    in          = ParameterIn.PATH,
                    description = "ReadStatus (마지막으로 읽은 시간) ID (UUID)",
                    required    = true,
                    example     = "16e245c7-3848-448f-a3dc-ca702ce9a491"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 수정되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 업데이트된 예시",
                                    value = """
                                                {
                                                    "success": true,
                                                    "code": 201,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                        "id": "d7044e4d-58ff-4c17-9f27-44d4bfcd5973",
                                                        "lastReadAt": "2025-07-09T13:21:46.307932Z",
                                                        "userId": "2fd4b22a-2bda-4503-9adf-02f803cce082",
                                                        "channelId": "3d0b4ba7-5b04-4287-9fe7-3a69950ad7c1"
                                                    },
                                                    "timestamp": "2025-07-09T22:21:46.326786"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "해당 ReadStatus(read-status-id)가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "ReadStatus with id 035433fc-83bc-4748-8675-bcc09ee953ea not found",
                                                    "timestamp": "2025-07-09T21:25:55.133318"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<CommonResponse<ReadStatusResponseDto>> updateReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                      @PathVariable("read-status-id") UUID readStatusId) {
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK, readStatusService.update(new ReadStatusUpdateDto(readStatusId))));
    }

    /*
    * DELETE 는 요구사항에 없었지만 추가해놓았습니다
    */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{read-status-id}")
    @Operation(
            summary = "ReadStatus 삭제",
            description = "ReadStatus을 (사용자와 채널 관계를 통한 마지막으로 읽은 시간) 삭제합니다.",
            parameters = @Parameter(
                    name        = "read-status-id",
                    in          = ParameterIn.PATH,
                    description = "ReadStatus (마지막으로 읽은 시간) ID (UUID)",
                    required    = true,
                    example     = "16e245c7-3848-448f-a3dc-ca702ce9a491"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "정상적으로 조회되었습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Success Example",
                                    summary = "성공적으로 삭제된 경우 Body에 데이터 없이 204 응답코드만 전달됩니다.",
                                    value = ""
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(read-status-id)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "해당 ReadStatus(read-status-id)가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "ReadStatus with id 035433fc-83bc-4748-8675-bcc09ee953ea not found",
                                                    "timestamp": "2025-07-09T21:25:55.133318"
                                                }
                                            """
                            )
                    ))
    })
    public ResponseEntity<ReadStatusResponseDto> deleteReadStatus(@Parameter(name = "read-status-id", in = ParameterIn.PATH, description = "ReadStatus ID")
                                                                      @PathVariable("read-status-id") UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
