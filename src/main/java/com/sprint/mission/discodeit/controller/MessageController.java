package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/messages")
public class MessageController {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    public MessageController(MessageService messageService, BinaryContentService binaryContentService) {
        this.messageService = messageService;
        this.binaryContentService = binaryContentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponseDto> create(@RequestBody MessageCreateDto dto) {
        MessageResponseDto created = messageService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponseDto>> findAll(@RequestParam("channel-id") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    /*
     * 메시지 ID로 단일 조회는 요구사항에 없었지만 추가해놓았습니다
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<MessageResponseDto> find(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(messageService.find(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<MessageResponseDto> update(@PathVariable("id") UUID id,
                                                  @RequestBody MessageUpdateDto dto) {
        return ResponseEntity.ok(messageService.update(dto));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<MessageResponseDto> delete(@PathVariable("id") UUID id) {
        messageService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
