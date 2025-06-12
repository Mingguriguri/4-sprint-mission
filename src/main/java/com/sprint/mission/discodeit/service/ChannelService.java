package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 채널 관련 CRUD(생성·조회·수정·삭제) 기능을 제공하는 서비스 인터페이스.
 * 존재하지 않는 채널을 참조하거나 잘못된 상태로 처리 요청 시에는 IllegalArgumentException을 던진다.
 */
public interface ChannelService {

    /* =========================================================
     * READ
     * ========================================================= */

    /**
     * 모든 ACTIVE 상태의 채널을 조회한다.
     *
     * @return recordStatus가 ACTIVE인 채널 객체들의 Set
     */
    Set<Channel> getAllChannels();

    /**
     * 주어진 채널 ID에 해당하고, recordStatus가 ACTIVE인 채널을 조회한다.
     *
     * @param id 조회하려는 채널의 ID
     * @return 조건을 만족하는 채널을 Optional로 반환. 존재하지 않으면 Optional.empty()
     * @throws IllegalArgumentException id가 null인 경우
     */
    Optional<Channel> getChannelById(String id);

    /**
     * 주어진 채널 이름에 해당하고, recordStatus가 ACTIVE인 채널 목록을 조회한다.
     *
     * @param channelName 조회하려는 채널 이름 (예: "#qna")
     * @return 이름이 일치하고 recordStatus가 ACTIVE인 채널들의 List
     * @throws IllegalArgumentException channelName이 null인 경우
     */
    List<Channel> getChannelByName(String channelName);

    /**
     * 주어진 userId에 해당하고, recordStatus가 ACTIVE인 채널 목록을 조회한다.
     *
     * @param userId 조회할 User의 ID
     * @return recordStatus가 ACTIVE이고, 해당 User가 참여 중인 채널들의 List
     * @throws IllegalArgumentException userId가 null인 경우
     */
    List<Channel> getChannelByUserId(String userId);

    /* =========================================================
     * CREATE
     * ========================================================= */

    /**
     * 새로운 채널을 생성하여 저장한다.
     * <ul>
     *   <li>createdAt, updatedAt 필드를 현재 시각으로 갱신</li>
     *   <li>recordStatus를 ACTIVE로 초기화</li>
     *   <li>members와 ownerId가 반드시 유효한 상태여야 함</li>
     * </ul>
     *
     * @param channelName 생성할 채널 이름
     * @param description 생성할 채널 설명
     * @param members     초기 멤버로 등록할 User 집합 (각 User는 recordStatus=ACTIVE여야 함)
     * @param ownerId     채널 소유자(User)의 ID (must be ACTIVE)
     * @return 생성된 Channel 객체
     * @throws IllegalArgumentException
     *         - members가 null이거나, members 중 하나라도 recordStatus != ACTIVE인 경우
     *         - ownerId가 null이거나, 소유자 User가 recordStatus != ACTIVE인 경우
     */
    Channel createChannel(String channelName, String description, Set<User> members, String ownerId);

    /* =========================================================
     * UPDATE
     * ========================================================= */

    /**
     * 주어진 채널 ID에 해당하는 채널의 이름과 설명을 수정한다.
     * <ul>
     *   <li>채널이 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param id          수정하려는 채널 ID
     * @param channelName 새 채널 이름
     * @param description 새 채널 설명
     * @return 수정된 Channel 객체
     * @throws IllegalArgumentException
     *         - id가 null이거나 존재하지 않는 경우
     *         - recordStatus가 ACTIVE가 아닌 경우 (삭제된 채널)
     */
    Channel updateChannelInfo(String id, String channelName, String description);

    /**
     * 주어진 채널 ID에 해당하는 채널에 새로운 사용자를 참여시킨다.
     * <ul>
     *   <li>채널이 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param channelId 유저를 추가할 채널의 ID
     * @param user      참여시킬 User 객체 (must be ACTIVE)
     * @throws IllegalArgumentException
     *         - channelId가 null이거나 존재하지 않는 경우
     *         - channel의 recordStatus가 ACTIVE가 아닌 경우
     *         - user가 null이거나 recordStatus != ACTIVE인 경우
     */
    void joinUser(String channelId, User user);

    /**
     * 주어진 채널 ID에 속한 사용자를 탈퇴시킨다.
     * <ul>
     *   <li>채널이 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>채널에 해당 user가 속해 있지 않으면 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param channelId 사용자 제거 대상 채널의 ID
     * @param user      제거할 User 객체 (must be ACTIVE)
     * @throws IllegalArgumentException
     *         - channelId가 null이거나 존재하지 않는 경우
     *         - channel의 recordStatus가 ACTIVE가 아닌 경우
     *         - user가 null이거나 recordStatus != ACTIVE인 경우
     *         - user가 해당 채널에 참여 중이 아닌 경우
     */
    void leaveUser(String channelId, User user);

    /**
     * 주어진 채널 ID에 해당하는 채널의 소유자(User)를 변경한다.
     * <ul>
     *   <li>채널이 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>ownerId가 null이거나, 해당 User가 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param id      수정하려는 채널의 ID
     * @param ownerId 새 주인(User)의 ID (must be ACTIVE)
     * @return 수정된 Channel 객체
     * @throws IllegalArgumentException
     *         - id가 null이거나 존재하지 않는 경우
     *         - recordStatus가 ACTIVE가 아닌 경우 (삭제된 채널)
     *         - ownerId가 null이거나, 해당 User의 recordStatus != ACTIVE인 경우
     */
    Channel updateChannelOwner(String id, String ownerId);

    /* =========================================================
     * DELETE
     * ========================================================= */

    /**
     * 채널을 Soft Delete 상태로 변경한다.
     * <ul>
     *   <li>채널이나 매개변수가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>채널에 속한 모든 메시지들의 recordStatus를 DELETED로 변경</li>
     *   <li>채널 객체의 recordStatus를 DELETED로 변경</li>
     *   <li>유저–채널 관계는 유지 (연결은 삭제되지 않음)</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param channel Soft Delete 대상 Channel 객체
     * @throws IllegalArgumentException
     *         - channel이 null이거나, recordStatus != ACTIVE인 경우
     */
    void softDeleteChannel(Channel channel);

    /**
     * Soft Delete된 채널을 복원한다.
     * <ul>
     *   <li>channel이 null이거나 recordStatus != DELETED인 경우 예외</li>
     *   <li>해당 채널의 메시지들을 recordStatus=ACTIVE로 변경</li>
     *   <li>채널 객체의 recordStatus를 ACTIVE로 변경</li>
     *   <li>유저–채널 관계는 Soft Delete 시점 그대로 유지</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param channel 복원할 Channel 객체
     * @throws IllegalArgumentException
     *         - channel이 null이거나 recordStatus != DELETED인 경우
     */
    void restoreChannel(Channel channel);

    /**
     * Soft Delete된 채널을 완전 삭제(Hard Delete)한다.
     * <ul>
     *   <li>channel이 null이거나 recordStatus != DELETED인 경우 예외</li>
     *   <li>채널에 속한 메시지들과의 관계를 모두 제거</li>
     *   <li>채널에 속한 유저–채널 관계를 모두 제거</li>
     *   <li>내부 채널 저장소(channelSet)에서 채널을 완전 삭제</li>
     * </ul>
     *
     * @param channel Hard Delete 대상 Channel 객체
     * @throws IllegalArgumentException
     *         - channel이 null이거나 recordStatus != DELETED인 경우
     */
    void hardDeleteChannel(Channel channel);
}
