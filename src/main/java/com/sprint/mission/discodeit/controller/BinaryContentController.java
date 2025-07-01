package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/binary-contents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @PostConstruct
    public void init() {
        BinaryContentCreateDto createProfile = new BinaryContentCreateDto(
                new byte[]{10,20,30},
                BinaryContentType.PROFILE
        );
        BinaryContentResponseDto content1 = binaryContentService.create(createProfile);

        BinaryContentCreateDto createAttachment = new BinaryContentCreateDto(
                new byte[]{10,20,30},
                BinaryContentType.MESSAGE
        );
        BinaryContentResponseDto content2 = binaryContentService.create(createAttachment);
        System.out.println("Binary Content 1: " + content1.getId());
        System.out.println("Binary Content 2: " + content2.getId());
    }

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // 단일 조회
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BinaryContentResponseDto> findOne(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.find(id));
    }

    // 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponseDto>> findMany(@RequestParam("ids") List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }

    /*
     * DELETE 는 요구사항에 없었지만 추가해놓았습니다
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<BinaryContentResponseDto> delete(@PathVariable("id") UUID id) {
        binaryContentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
