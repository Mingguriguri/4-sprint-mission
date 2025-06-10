package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User 엔티티는 디스코드잇 회원을 나타낸다.
 * <p>주요 속성:</p>
 * <ul>
 *   <li>username: 유저 고유 이름</li>
 *   <li>email: 유저 이메일</li>
 *   <li>password: 유저 비밀번호(암호화 전 텍스트)</li>
 *   <li>status: 유저 상태 (ACTIVE, INACTIVE, WITHDREW)</li>
 *   <li>channels: 이 유저가 속해 있는 채널 집합</li>
 *   <li>messages: 이 유저가 작성한 메시지 목록</li>
 * </ul>
 * <p>Soft Delete/Hard Delete 로직은 UserService 책임지며, 이 엔티티는 RecordStatus를 통해 상태 관리를 한다.</p>
 */
public class User extends BaseEntity {
    private String username;
    private String email;
    private String password;
    private UserStatus status;

    private final Set<Channel> channels = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();

    public User(String username, String email, String password, UserStatus status) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /* =========================================================
     * Channel 관계 메소드
     * =========================================================*/

    public Set<Channel> getChannels() {
        return channels;
    }

    /**
     * 이 유저에서 특정 채널과의 관계를 연결한다.
     * <p>
     *  양방향 관계: Channel.getUsers()에서도 자동으로 연결됨<br>
     *  이미 User에 채널이 연결되어 있다면 아무 동작도 하지 않음
     * </p>
     *
     * @param channel 추가할 Channel 객체
     */
    public void addChannel(Channel channel) {
        if (!channels.contains(channel)) {
            channels.add(channel);
            if (!channel.getUsers().contains(this)) {
                channel.addUser(this);
            }
        }
    }

    /**
     * 이 유저에서 특정 채널과의 관계를 해제한다.
     * <p>
     *  양방향 관계: Channel.getUsers()에서도 자동으로 연결 해제됨<br>
     *  유저에 해당 채널이 없다면 아무 동작도 하지 않음
     * </p>
     *
     * @param channel 제거할 Channel 객체
     */
    public void removeChannel(Channel channel) {
        if (channels.contains(channel)) {
            channels.remove(channel);
            if (channel.getUsers() != null) {
                channel.removeUser(this);
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
     * 이 유저에 Message를 추가한다.
     * <p>
     *  양방향 관계: Message.getUser()에도 자동으로 연결됨<br>
     *  이미 메시지가 User에 속해 있다면 아무 동작도 하지 않음
     * </p>
     *
     * @param message 추가할 Message 객체
     */
    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            if (!message.getUser().equals(this)) {
                message.addUser(this);
            }
        }
    }

    /**
     * 해당 유저에 Message를 제거한다.
     * <p>
     *  양방향 관계: Message.getUser()에서도 연결 해제<br>
     *  유저에 해당 Message가 속해 있지 않으면 아무 동작도 하지 않음
     * </p>
     *
     * @param message 제거할 Message 객체
     */
    public void removeMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
            if (message.getUser() != null) {
                message.removeUser(this);
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
