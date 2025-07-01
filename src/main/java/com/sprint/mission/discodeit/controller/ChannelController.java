package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.AllChannelByUserIdResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostConstruct
    public void init() {
        ChannelCreateDto pubCreate = new ChannelCreateDto(
                ChannelType.PUBLIC,
                "공지", "공지 채널입니다",
                null, null);
        channelService.createPublicChannel(pubCreate);
    }

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<ChannelResponseDto> createPublic(@RequestBody ChannelCreateDto dto) {
        ChannelResponseDto created = channelService.createPublicChannel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<ChannelResponseDto> createPrivate(@RequestBody ChannelCreateDto dto) {
        ChannelResponseDto created = channelService.createPrivateChannel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AllChannelByUserIdResponseDto> findAll(@RequestParam("user-id") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }


    /*
     * 채널 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ChannelResponseDto> find(@PathVariable("id") UUID channelId,
                                                   @RequestParam("user-id") UUID userId) {
        return ResponseEntity.ok(channelService.find(channelId, userId));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<ChannelResponseDto> update(@PathVariable("id") UUID id,
                                          @RequestBody ChannelUpdateDto dto) {
        return ResponseEntity.ok(channelService.update(dto));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<ChannelResponseDto> delete(@PathVariable("id") UUID id) {
        channelService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
