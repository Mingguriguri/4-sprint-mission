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
    public ResponseEntity<UserResponseDto> create(@RequestParam("username") String username,
                                                  @RequestParam("email") String email,
                                                  @RequestParam("password") String password,
                                                  @RequestParam("file") MultipartFile file
                                                  ) throws IOException {
        System.out.println("# username "  + username);
        System.out.println("# email "  + email);
        System.out.println("# password "  + password);
        System.out.println("# file "  + file.getName() + "." +file.getContentType());
        BinaryContentCreateDto bcDto = null;
        if (!file.isEmpty()) {
            bcDto = new BinaryContentCreateDto(
                    file.getBytes(),
                    BinaryContentType.PROFILE
            );
        }
        UserResponseDto created = userService.create(
                new UserCreateDto(username, email, password, bcDto)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<UserResponseDto> find(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userService.find(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") UUID id,
                                                  @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(dto));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/online")
    public ResponseEntity<UserResponseDto> touchOnline(@PathVariable("id") UUID id) {
        userStatusService.updateByUserId(id);
        UserResponseDto response = userService.find(id);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
