package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User extends Base {
    private String username;
    private String email;
    private String password;

    private final Set<Channel> channels = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();

    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public Set<Channel> getChannels() {
        return channels;
    }
    public void addChannel(Channel channel) {
        channels.add(channel);
        if (!channel.getUsers().contains(this)) {
            channel.addUser(this);
        }
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        if (channel.getUsers().contains(this)) {
            channel.removeUser(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
