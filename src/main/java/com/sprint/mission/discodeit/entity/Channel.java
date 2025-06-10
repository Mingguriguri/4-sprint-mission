package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Channel 엔티티는 한 개의 채널을 나타낸다.
 * <p>주요 속성:</p>
 * <ul>
 *   <li>channelName: 채널 고유 이름 (예: "#announcements", "#general")</li>
 *   <li>description: 채널에 대한 간략한 설명 텍스트</li>
 *   <li>users: 이 채널에 참여 중인 User 집합</li>
 *   <li>ownerId: 채널을 만든 소유자(User)의 ID</li>
 *   <li>messages: 이 채널에서 오간 Message 객체 목록</li>
 * </ul>
 * <p>Soft Delete/Hard Delete 로직은 ChannelService 책임지며, 이 엔티티는 RecordStatus를 통해 상태 관리를 한다.</p>
 * @see User
 * @see Message
 */

public class Channel extends BaseEntity {
    private String channelName;
    private String description;
    private String ownerId;

    private final Set<User> users;
    private final List<Message> messages = new ArrayList<>();

    public Channel(String channelName, String description, Set<User> users, String ownerId) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.users = users;
        this.ownerId = ownerId;
    }

    /* =========================================================
     * Getter
     * =========================================================*/

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    /* =========================================================
     * Setter
     * =========================================================*/

    // 채널명 변경하는 메서드
    public void changeChannelName(String channelName) {
        this.channelName = channelName;
    }

    // 채널 설명을 업데이트하는 메서드
    public void updateChannelDesc(String description) {
        this.description = description;
    }

    // 채널 소유자를 변경하는 메서드
    public void changeChannelOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /* =========================================================
    * User 관계 메소드
    * =========================================================*/

    public Set<User> getUsers() {
        return users;
    }

    /**
     * 이 채널에서 User를 추가한다.
     * <p>
     *   양방향 관계: User.getChannels()에서도 자동으로 연결됨.<br>
     *   이미 해당 User가 채널에 속해 있으면 아무 동작도 하지 않음.
     * </p>
     *
     * @param user 추가할 User 객체
     */
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            if (!user.getChannels().contains(this)) {
                user.addChannel(this);
            }
        }
    }

    /**
     * 이 채널에서 User를 제거한다.
     * <p>
     *  양방향 관계: User.getChannels()에서도 자동으로 연결 해제<br>
     *  채널에 해당 User가 속해 있지 않으면 아무 동작도 하지 않음
     * </p>
     *
     * @param user 제거할 User 객체
     */
    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            if (user.getChannels() != null) {
                user.removeChannel(this);
            }
        }
    }

    /* =========================================================
     * Message 관계 메소드
     * =========================================================*/

    public List<Message> getMessages() {
        return messages;
    }


    /**
     * 이 채널에 Message를 추가한다.
     * <p>
     *  양방향 관계: Message.getChannel()에도 자동으로 연결<br>
     *  이미 해당 Message 채널에 속해 있으면 아무 동작도 하지 않음
     * </p>
     *
     * @param message 추가할 Message 객체
     */
    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            if (!message.getChannel().equals(this)) {
                message.addChannel(this);
            }
        }
    }

    /**
     * 이 채널에 Message를 제거한다.
     * <p>
     *  양방향 관계: Message.getChannel()에서도 연결 해제<br>
     *  채널에 해당 Message 속해 있지 않으면 아무 동작도 하지 않음
     * </p>
     *
     * @param message 제거할 Message 객체
     */
    public void removeMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
            if (message.getChannel() != null) {
                message.removeChannel(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + super.getId() +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}

