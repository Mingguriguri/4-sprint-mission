// TODO: ChannelMember로 나누기 전에 따로 만들어본 후, 강사님 피드백 받고 추가하기

//package com.sprint.mission.discodeit.entity;
//
//import java.util.List;
//import java.util.ArrayList;
//
//public class ChannelMember extends Base {
//    private User user;
//    private final Channel channel;
//    private final String role;
//
//    private final List<Message> messages = new ArrayList<>();
//
//    public ChannelMember(User user, Channel channel, String role) {
//        super();
//        this.user = user;
//        this.channel = channel;
//        this.role = role;
//
//        channel.addChannelMember(this.user, this.role);
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public Channel getChannel() {
//        return channel;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public List<Message> getMessages() { return List.copyOf(messages); }
//
//    void addMessage(Message msg) {
//        if (!messages.contains(msg)) {
//            messages.add(msg);
//        }
//    }
//    void removeMessage(Message msg) {
//        messages.remove(msg);
//    }
//
//    @Override
//    public String toString() {
//        return "ChannelMember{" +
//                "role='" + role + '\'' +
//                ", channel=" + channel +
//                ", user=" + user +
//                '}';
//    }
//}