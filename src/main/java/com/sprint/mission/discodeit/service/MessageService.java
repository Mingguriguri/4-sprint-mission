package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 메시지 관련 CRUD(생성·조회·수정·삭제) 기능을 제공하는 서비스 인터페이스.
 * 존재하지 않는 메시지를 참조하거나 잘못된 상태로 처리 요청 시에는 IllegalArgumentException을 던진다.
 */
public interface MessageService {

    /* =========================================================
     * READ
     * ========================================================= */

    /**
     * 모든 ACTIVE 상태의 메시지를 조회한다.
     *
     * @return recordStatus가 ACTIVE인 메시지 객체들의 List
     */
    List<Message> getAllMessages();

    /**
     * 주어진 메시지 ID에 해당하고, recordStatus가 ACTIVE인 메시지를 조회한다.
     *
     * @param messageId 조회하려는 메시지 ID
     * @return 조건을 만족하는 메시지를 Optional로 반환. 존재하지 않으면 Optional.empty()
     * @throws IllegalArgumentException id가 null인 경우
     */
    Optional<Message> getMessageById(String messageId);

    /**
     * 주어진 메시지 ID와 recordStatus 조건을 만족하는 메시지를 조회한다.
     *
     * @param messageId    조회하려는 메시지 ID
     * @param recordStatus 조회하려는 메시지 상태 (RecordStatus.ACTIVE or RecordStatus.DELETED)
     * @return 주어진 ID와 recordStatus가 일치하는 메시지 Optional. 없으면 Optional.empty()
     * @throws IllegalArgumentException
     *         - id가 null인 경우
     *         - recordStatus가 null인 경우
     */
    Optional<Message> getMessageByIdWithStatus(String messageId, RecordStatus recordStatus);

    /**
     * 주어진 userId에 해당하면서, recordStatus가 ACTIVE인 메시지 목록을 조회한다.
     *
     * @param userId 조회할 메시지 전송자의 User ID
     * @return recordStatus가 ACTIVE이고, 해당 User가 전송한 메시지들의 List
     * @throws IllegalArgumentException userId가 null인 경우
     */
    List<Message> getMessageByUserId(String userId);

    /**
     * 주어진 channelId에 해당하면서, recordStatus가 ACTIVE인 메시지 목록을 조회한다.
     *
     * @param channelId 조회할 메시지가 전송된 채널의 ID
     * @return recordStatus가 ACTIVE이고, 해당 채널에 전송된 메시지들의 List
     * @throws IllegalArgumentException channelId가 null인 경우
     */
    List<Message> getMessageByChannelId(String channelId);

    /* =========================================================
     * CREATE
     * ========================================================= */

    /**
     * 새로운 메시지를 생성하여 저장한다.
     * <ul>
     *   <li>createdAt, updatedAt 필드를 현재 시각으로 갱신</li>
     *   <li>recordStatus를 ACTIVE로 초기화</li>
     *   <li>channel과 user는 반드시 recordStatus가 ACTIVE인 상태여야 함</li>
     * </ul>
     *
     * @param channel 메시지를 생성할 채널 객체 (must be ACTIVE)
     * @param user    메시지를 전송할 유저 객체 (must be ACTIVE)
     * @param content 생성할 메시지 내용
     * @return 생성된 Message 객체
     * @throws IllegalArgumentException
     *         - channel이 null이거나 recordStatus != ACTIVE일 경우
     *         - user가 null이거나 recordStatus != ACTIVE일 경우
     */
    Message createMessage(Channel channel, User user, String content);

    /* =========================================================
     * UPDATE
     * ========================================================= */

    /**
     * 주어진 메시지 ID에 해당하는 메시지의 채널, 전송자(User), 내용을 수정한다.
     * <ul>
     *   <li>메시지가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>새 channel 또는 user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param messageId 수정하려는 메시지 ID
     * @param channel   새 채널 객체 (must be ACTIVE)
     * @param user      새 전송자(User) 객체 (must be ACTIVE)
     * @param content   새 메시지 내용
     * @return 수정된 Message 객체
     * @throws IllegalArgumentException
     *         - messageId가 null이거나 존재하지 않거나 recordStatus != ACTIVE인 경우
     *         - channel이 null이거나 recordStatus != ACTIVE인 경우
     *         - user가 null이거나 recordStatus != ACTIVE인 경우
     */
    Message updateMessage(String messageId, Channel channel, User user, String content);

    /* =========================================================
     * DELETE
     * ========================================================= */

    /**
     * 메시지를 Soft Delete 상태로 변경한다.
     * <ul>
     *   <li>recordStatus를 DELETED로 변경</li>
     *   <li>updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param messageId Soft Delete 대상 메시지 ID
     * @throws IllegalArgumentException
     *         - messageId가 null이거나 존재하지 않는 경우
     *         - 이미 recordStatus가 DELETED인 경우
     */
    void deleteMessageById(String messageId);

    /**
     * Soft Delete된 메시지를 복원한다.
     * <ul>
     *   <li>recordStatus를 ACTIVE로 변경</li>
     *   <li>updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param messageId 복원할 메시지 ID
     * @throws IllegalArgumentException
     *         - messageId가 null이거나 존재하지 않는 경우
     *         - recordStatus가 ACTIVE인 경우 (이미 복원된 상태)
     */
    void restoreMessageById(String messageId);

    /**
     * Soft Delete된 메시지를 완전 삭제(Hard Delete)한다.
     * <ul>
     *   <li>내부 메시지 저장소(messageList)에서 해당 메시지를 완전히 제거</li>
     * </ul>
     *
     * @param messageId Hard Delete할 메시지 ID
     * @throws IllegalArgumentException
     *         - messageId가 null이거나 존재하지 않는 경우
     *         - recordStatus가 ACTIVE인(Soft Delete되지 않은) 메시지인 경우
     */
    void hardDeleteMessageById(String messageId);
}
