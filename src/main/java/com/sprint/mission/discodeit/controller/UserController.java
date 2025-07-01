package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    @PostConstruct
    public void init() {
        UserCreateDto createDto = new UserCreateDto(
                "mingguriguri",
                "minggurigrui@example.com",
                "password123",
                null
        );
        userService.create(createDto);
    }

    public UserController(UserService userService,
                          UserStatusService userStatusService,
                          BinaryContentService binaryContentService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
        this.binaryContentService = binaryContentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> create(@ModelAttribute UserCreateDto dto) {
        UserResponseDto created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{user-id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok(userService.find(userId));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{user-id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("user-id") UUID userId,
                                                      @ModelAttribute UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(dto));
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/{user-id}/online")
    public ResponseEntity<UserResponseDto> touchOnline(@PathVariable("user-id") UUID userId) {
        userStatusService.updateByUserId(userId);
        UserResponseDto response = userService.find(userId);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{user-id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("user-id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
