package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/read-statuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDto> create(@RequestBody ReadStatusCreateDto dto) {
        ReadStatusResponseDto created = readStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ReadStatusResponseDto> find(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(readStatusService.find(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<ReadStatusResponseDto> update(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(readStatusService.update(new ReadStatusUpdateDto(id)));
    }

    /*
    * DELETE 는 요구사항에 없었지만 추가해놓았습니다
    */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<ReadStatusResponseDto> delete(@PathVariable("id") UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
