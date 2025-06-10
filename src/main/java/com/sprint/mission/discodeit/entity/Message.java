package com.sprint.mission.discodeit.entity;

/**
 * Message 엔티티는 특정 채널에서 특정 유저가 전송한 메시지를 나타낸다.
 * <p>주요 속성:</p>
 * <ul>
 *   <li>channel: 이 메시지가 속해 있는 채널 객체</li>
 *   <li>user: 이 메시지를 작성한 유저 객체</li>
 *   <li>content: 메시지 내용</li>
 * </ul>
 * <p>Soft Delete/Hard Delete 로직은 MessageService 책임지며, 이 엔티티는 RecordStatus를 통해 상태 관리를 한다.</p>
 */
public class Message extends BaseEntity {
    private Channel channel;
    private User user;
    private String content;

    public Message(Channel channel, User user, String content) {
        super();
        this.channel = channel;
        this.user = user;
        this.content = content;
    }

    /* =========================================================
     * Getter
     * =========================================================*/

    public String getContent() {
        return content;
    }

    /* =========================================================
     * Setter
     * =========================================================*/

    public void sendMessageContent(String content) {
        this.content = content;
    }

    /* =========================================================
     * Channel 관계 메소드
     * =========================================================*/

    public Channel getChannel() {
        return channel;
    }

    /**
     * 이 메시지에 특정 채널과의 관계를 연결한다.
     * <p>
     *  양방향 관계: Channel.getMessages()에서도 자동으로 연결됨
     * </p>
     *
     * @param channel 연결할 Channel 객체
     */
    public void addChannel(Channel channel) {
        this.channel = channel;
        if (!channel.getMessages().contains(this)) {
            channel.addMessage(this);
        }
    }

    /**
     * 이 메시지에서 특정 채널과의 관계를 해제한다.
     * <p>
     *  양방향 관계: Channel.getMessages()에서도 자동으로 연결 해제됨
     * </p>
     *
     * @param channel 연결을 해제할 Channel 객체
     */
    public void removeChannel(Channel channel) {
        if (channel.getMessages().contains(this)) {
            this.channel = channel;
            if (channel.getMessages() != null) {
                channel.removeMessage(null);
            }
        }
    }

    /* =========================================================
     * Message 관계 메소드
     * =========================================================*/

    public User getUser() {
        return user;
    }

    /**
     * 이 메시지에 User를 연결한다.
     * <p>
     *  양방향 관계: User.getMessages()에서도 자동으로 연결
     * </p>
     *
     * @param user 연결할 User 객체
     */
    public void addUser(User user) {
        this.user = user;
        if (!user.getMessages().contains(this)) {
            user.addMessage(this);
        }
    }

    /**
     * 이 메시지에 User를 연결을 해제한다.
     * <p>
     *  양방향 관계: User.getMessages()에서도 자동으로 연결 해제됨
     * </p>
     *
     * @param user 연결을 해제할 User 객체
     */
    public void removeUser(User user) {
        this.user = user;
        if (user.getMessages().contains(this)) {
            user.removeMessage(null);
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + super.getId() +
                ", channelName=" + channel.getChannelName() +
                ", senderName=" + user.getUsername() +
                ", content='" + content + '\'' +
                '}';
    }
}
