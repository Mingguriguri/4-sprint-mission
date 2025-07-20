package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "👁️ ReadStatus", description = "ReadStatus(사용자가 채널 별 마지막으로 메시지를 읽은 시간) 관련 API")
public interface ReadStatusApi {
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
    ResponseEntity<ReadStatusResponseDto> createReadStatus(ReadStatusCreateDto dto);


    @Operation(
            summary = "ReadStatus (마지막으로 읽은 시간) 전체 목록 조회",
            description = "특정 사용자(userId)의 전체 ReadStatus(마지막으로 메시지를 읽은 시간) 목록을 조회합니다",
            parameters = @Parameter(
                    name = "userId",
                    in  = ParameterIn.QUERY,
                    description = "사용자 ID (UUID)",
                    required  = true,
                    example = "ad2343a0-a8ff-47d7-94cb-03681dbff078"
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
                                                    "data": [
                                                        {
                                                            "id": "6d6c0912-2c33-43bf-b3a9-2b5f0f2fb739",
                                                            "lastReadAt": "2025-07-09T08:11:38.425541Z",
                                                            "userId": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                            "channelId": "67b4d79c-d813-4e9a-905d-2cd29d190bf6"
                                                        },
                                                        {
                                                            "id": "25010975-3297-4681-a99d-44212fdf03b6",
                                                            "lastReadAt": "2025-07-08T04:36:30.641806Z",
                                                            "userId": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                            "channelId": "ad183068-c67e-4a1d-8729-7e0ede43eb86"
                                                        },
                                                        {
                                                            "id": "4242c2e3-9de0-44e5-a2d7-ac99ecc8e658",
                                                            "lastReadAt": "2025-07-08T04:36:17.731896Z",
                                                            "userId": "ad2343a0-a8ff-47d7-94cb-03681dbff078",
                                                            "channelId": "3d0b4ba7-5b04-4287-9fe7-3a69950ad7c1"
                                                        }
                                                    ],
                                                    "timestamp": "2025-07-10T16:29:11.992503"
                                                }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "해당 User(userId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "해당 User(userId)가 존재하지 않을 경우 예시",
                                    value = """
                                                {
                                                    "success": false,
                                                    "code": 404,
                                                    "message": "Not Found Exception",
                                                    "data": "User not found with id 16e245c7-3848-448f-a3dc-ca702ce9a491",
                                                    "timestamp": "2025-07-10T16:30:35.987413"
                                                }
                                            """
                            )
                    ))
    })
    ResponseEntity<List<ReadStatusResponseDto>> getReadStatuses(UUID userId);


    @Operation(
            summary = "ReadStatus (마지막으로 읽은 시간)을 업데이트",
            description = "특정 readStatusId를 통해 마지막으로 메시지를 읽은 시간을 업데이트합니다.",
            parameters = @Parameter(
                    name        = "readStatusId",
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
            @ApiResponse(responseCode = "404", description = "해당 ReadStatus(readStatusId)가 존재하지 않습니다",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "NotFound Example",
                                    summary = "해당 ReadStatus(readStatusId)가 존재하지 않을 경우 예시",
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
    ResponseEntity<ReadStatusResponseDto> updateReadStatus(UUID readStatusId, ReadStatusUpdateDto dto);
}
