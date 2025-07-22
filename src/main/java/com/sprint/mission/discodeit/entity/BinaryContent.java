package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다.
* 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
* 수정이 불가능합니다.
* */

@NoArgsConstructor
@Getter
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long size;

    public BinaryContent(byte[] bytes, String fileName, String contentType, long size) {
        super();
        this.bytes = bytes;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }
}
