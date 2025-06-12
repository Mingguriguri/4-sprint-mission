package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.factory.PersistenceType;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.factory.DiscodeitFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaApplication {
    public static void main(String[] args) {
        DiscodeitFactory jcfFactory  = new DiscodeitFactory(PersistenceType.JCF);
        DiscodeitFactory fileFactory = new DiscodeitFactory(PersistenceType.FILE);
        // JCF로 할 때
//        UserService userService = jcfFactory.getUserService();
//        ChannelService channelService = jcfFactory.getChannelService();
//        MessageService messageService = jcfFactory.getMessageService();

        // File I/O로 할 때
        UserService userService = fileFactory.getUserService();
        ChannelService channelService = fileFactory.getChannelService();
        MessageService messageService = fileFactory.getMessageService();

        System.out.println("\n===================================================================================================");
        System.out.println("-------------------------------[ 유저 생성 ]------------------------------- ");

        User user1 = userService.createUser("Minjeong", "minjeong@gmail.com", "1q2w3e4r!");
        User user2 = userService.createUser("Joy", "joy@gmail.com", "1q2w3e4r!");
        User user3 = userService.createUser("John", "jhon@gmail.com", "1q2w3e4r!");
        User user4 = userService.createUser("Alice", "alice@example.com", "1q2w3e4r!");
        User user5 = userService.createUser("David", "david@example.com", "1q2w3e4r!");

        System.out.println("\n-------------------------------[ 유저 단건 조회 ]------------------------------- ");
        String findUserId = user1.getId();
        String findUserName = user2.getUsername();
        String findUserEmail = user3.getEmail();

        System.out.println("1. 유저 아이디로 조회: " + findUserId);
        System.out.println(userService.getUserById(findUserId));

        System.out.println("\n2. 유저 이름으로 조회: " + findUserName);
        System.out.println(userService.getUserByUsername(findUserName));

        System.out.println("\n3. 유저 이메일로 조회: " + findUserEmail);
        System.out.println(userService.getUserByEmail(findUserEmail));

        System.out.println("\n-------------------------------[ 유저 다건 조회 ]------------------------------- ");
        List<User> userList = userService.getAllUsers();
        userList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 유저 수정 ]------------------------------- ");
        System.out.println("1. 수정 전 유저4: " + user4);

        userService.updateUserInfo(user4.getId(), "Codeit", "codeit@gmail.com", "1q2w3e4r!");

        // TODO: 이 부분 질문: 이렇게 새로 받아와서 하는 점 맞을지 궁금합니다.
        User changeUser4 = userService
                .getUserById(user4.getId())
                .orElseThrow(() -> new IllegalStateException("수정된 유저를 못 찾았습니다."));

        System.out.println("\n2. 수정 후 유저4: " + changeUser4);

        System.out.println("\n-------------------------------[ 유저 비활성화 ]------------------------------- ");
        System.out.println("1. 비활성화 전 유저4: " + userService.getUserById(user4.getId()));

        userService.inactivateUser(user4);

        System.out.println("\n2. 비활성화 후 유저4: " + userService.getUserById(user4.getId()));

        System.out.println("\n-------------------------------[ 비활성화된 유저 활성화 ]------------------------------- ");
        System.out.println("1. 활성화 전 유저4: " + userService.getUserById(user4.getId()));
        userService.activateUser(user4);
        System.out.println("\n2. 활성화 후 유저4: " + userService.getUserById(user4.getId()));

        System.out.println("\n-------------------------------[ 유저 삭제 (Soft Delete) ]------------------------------- ");
        userList = userService.getAllUsers();
        System.out.println("1. 삭제 전 유저 리스트: (" + userList.size() + "개)");
        userList.forEach(System.out::println);

        System.out.println("\n2. 삭제할 유저5: " + user5.getUsername());
        userService.deleteUser(user5);

        userList = userService.getAllUsers();
        System.out.println("\n3. 삭제 후 유저 리스트: (" + userList.size() + "개)");
        userList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 삭제한 유저 복원 ]------------------------------- ");

        System.out.println("1. 복원 전 유저 리스트(" + userList.size() + "개)");
        userList.forEach(System.out::println);

        // TODO: 이 부분도 삭제한 유저를 다시 가져오는 부분입니다. 기존에 없었지만 새로 추가되었습니다. 적절한지 궁금합니다.
        User deletedUser = userService
                .getUserById(user5.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 유저를 못 찾았습니다."));

        System.out.println("\n2. 복원할 유저5: " + deletedUser);
        userService.restoreUser(deletedUser);

        userList = userService.getAllUsers();
        System.out.println("\n3. 복원 후 유저 리스트(" + userList.size() + "개)");
        userList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 유저 영구 삭제 (Hard Delete) ]-------------------------------");
        userList = userService.getAllUsers();
        System.out.println("1. 영구 삭제 전 유저 리스트(" + userList.size() + "개)");
        userList.forEach(System.out::println);

        System.out.println("\n2. 삭제할 유저: " + userService.getUserById(user5.getId()));

        userService.deleteUser(user5);
        System.out.println("\n3. 소프트 삭제 후, 영구 삭제 진행");

        deletedUser = userService
                .getUserById(user5.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 유저를 못 찾았습니다."));
        userService.hardDeleteUser(deletedUser);

        userList = userService.getAllUsers();
        System.out.println("\n4. 삭제 후 유저 리스트(" + userList.size() + "개)");
        userList.forEach(System.out::println);

        System.out.println("\n===================================================================================================");
        System.out.println("-------------------------------[ 채널 생성 ]------------------------------- ");
        String ownerId = user1.getId();
        List<User> memberList = List.of(user1, user2, user3);
        Set<User> members1 = new HashSet<>(memberList);
        Set<User> members2 = new HashSet<>(memberList);
        Set<User> members3 = new HashSet<>(memberList);
        Set<User> members4 = new HashSet<>(memberList);
        Set<User> members5 = new HashSet<>(memberList);

        Channel channel1 = channelService.createChannel("#announcements", "서버의 중요한 소식이나 업데이트를 공지하는 채널입니다. 모두 확인 필수!", members1, ownerId);
        Channel channel2 = channelService.createChannel("#now-playing", "지금 듣고 있는 노래를 공유하거나, 추천 음악을 소개하는 뮤직 채널입니다.", members2, ownerId);
        Channel channel3 = channelService.createChannel("#daily-goals", "오늘 할 일, 목표, 공부 계획을 공유하고 서로 동기 부여하는 채널이에요.", members3, ownerId);
        Channel channel4 = channelService.createChannel("#off-topic", "주제 자유! 게임, 밈, 일상 얘기 등 아무 이야기나 나눠요.", members4, ownerId);
        Channel channel5 = channelService.createChannel("#qna", "질문이 있다면 이 채널에 남겨주세요.", members5, ownerId);

        System.out.println("\n-------------------------------[ 채널 단건 조회 ]-------------------------------");
        String findChannelId = channel1.getId();
        String findChannelName = channel2.getChannelName();
        List<Channel> findChannelByUserId = channelService.getChannelByUserId(user1.getId());

        System.out.println("1. 채널 아이디로 조회: " + findChannelId);
        System.out.println(channelService.getChannelById(findChannelId));

        System.out.println("\n2. 채널 이름으로 조회: " + findChannelName);
        System.out.println(channelService.getChannelByName(findChannelName));

        System.out.println("\n3. 특정 유저 아이디로 조회 (유저명: " + user1.getUsername() + ", 유저의 채널 개수: (" + findChannelByUserId.size() + "개))");
        findChannelByUserId.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 채널 다건 조회 ]-------------------------------");
        Set<Channel> channels = channelService.getAllChannels();
        channels.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 채널 기본 정보 수정 (name, description) ]------------------------------- ");
        System.out.println("1. 수정 전 채널4: " + channel4);

        System.out.println("\n2. 채널명과 설명 변경");
        channelService.updateChannelInfo(channel4.getId(), "#free-topic", "자유롭게 게임, 밈, 일상 얘기 등 아무 이야기나 나눠요.");

        System.out.println("\n3. 수정 후 채널4: " + channelService.getChannelById(channel4.getId()));

        System.out.println("\n-------------------------------[ 채널 Owner 수정 (ownerId) ]------------------------------- ");
        System.out.println("1. 수정 전 채널4 Owner(user1): " + channel4.getOwnerId() + ", " + userService.getUserById(channel4.getOwnerId()).get().getUsername());

        System.out.println("2. owner를 user1 에서 user2로 변경");
        channelService.updateChannelOwner(channel4.getId(), user2.getId());

        // TODO: 이 부분도 다시 가져오는 부분입니다. 기존에 없었지만 새로 추가되었습니다. 적절한지 궁금합니다.
        Channel updatedChannel4 = channelService
                .getChannelById(channel4.getId())
                .orElseThrow(() -> new IllegalStateException("변경된 채널을 못 찾았습니다."));

        System.out.println("3. 수정 후 채널4 Owner(user2): " + updatedChannel4.getOwnerId() + ", " + userService.getUserById(updatedChannel4.getOwnerId()).get().getUsername());

        System.out.println("\n-------------------------------[ 채널4에 유저 추가 ]------------------------------ ");
        System.out.println("1. 수정 전 채널4 Users: (" + channel4.getUsers().size() + "명)");
        channel4.getUsers().forEach(System.out::println);

        System.out.println("\n2. user4(Codeit) 추가");
        channelService.joinUser(channel4.getId(), changeUser4);

        updatedChannel4 = channelService
                .getChannelById(channel4.getId())
                .orElseThrow(() -> new IllegalStateException("변경된 채널을 못 찾았습니다."));

        System.out.println("\n3. 수정 후 채널4 Users: (" + updatedChannel4.getUsers().size() + "명)");
        updatedChannel4.getUsers().forEach(System.out::println);

        System.out.println("\n-------------------------------[ 채널5에 유저 제거 ]------------------------------ ");
        System.out.println("1. 수정 전 채널5 Users (" + channel5.getUsers().size() + "명)");
        channel5.getUsers().forEach(System.out::println);

        System.out.println("\n2. user3(John) 제거");
        channelService.leaveUser(channel5.getId(), user3);

        Channel updatedChannel5 = channelService
                .getChannelById(channel5.getId())
                .orElseThrow(() -> new IllegalStateException("변경된 채널을 못 찾았습니다."));

        System.out.println("\n3. 수정 후 채널5 Users (" + updatedChannel5.getUsers().size() + "명)");
        updatedChannel5.getUsers().forEach(System.out::println);

        System.out.println("\n-------------------------------[ 채널 삭제 (Soft Delete) ]------------------------------- ");
        System.out.println("1. 삭제 전 채널 리스트("+channels.size()+"개)");
        channels.forEach(System.out::println);

        System.out.println("\n2. 삭제할 채널: " + channel5.getChannelName());
        channelService.softDeleteChannel(channel5);

        channels = channelService.getAllChannels();
        System.out.println("\n3. 삭제 후 채널 리스트("+channels.size()+"개)");
        channels.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 삭제한 채널 복원 ]------------------------------- ");

        System.out.println("1. 복원 전 메시지 리스트("+channels.size()+"개)");
        channels.forEach(System.out::println);

        Channel deletedChannel = channelService
                .getChannelById(channel5.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 채널을 못 찾았습니다."));

        System.out.println("\n2. 복원할 메시지: " + deletedChannel);
        channelService.restoreChannel(deletedChannel);

        channels = channelService.getAllChannels();
        System.out.println("\n3. 복원 후 메시지 리스트("+channels.size()+"개)");
        channels.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 채널 영구 삭제 (Hard Delete) ]-------------------------------");
        channels = channelService.getAllChannels();
        System.out.println("1. 영구 삭제 전 채널 리스트(" + channels.size()+"개)");
        channels.forEach(System.out::println);

        System.out.println("\n2. 삭제할 채널: " + channel5);
        channelService.softDeleteChannel(channel5);
        System.out.println("\n3. 소프트 삭제 후, 영구 삭제 진행");

        deletedChannel = channelService
                .getChannelById(channel5.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 채널을 못 찾았습니다."));

        channelService.hardDeleteChannel(deletedChannel);

        channels = channelService.getAllChannels();
        System.out.println("\n4. 삭제 후 채널 리스트(" + channels.size()+"개)");
        channels.forEach(System.out::println);

        System.out.println("\n===================================================================================================");
        System.out.println("-------------------------------[ 메시지 등록 ]-------------------------------");

        // ===  #daily-goals 채널 ===
        System.out.println("💬메시지를 보낼 채널: " + channelService.getChannelById(channel3.getId()) + "\n");

        Message message1 = messageService.createMessage(channel3, user1, "목표 1: 알고리즘 문제 2개 풀기");
        System.out.println("[" + channelService.getChannelById(message1.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message1 + "] => 메시지 등록 완료");

        Message message2 = messageService.createMessage(channel3, user2, "오늘은 영어 회화 연습 30분!");
        System.out.println("[" + channelService.getChannelById(message2.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message2 + "] => 메시지 등록 완료");

        Message message3 = messageService.createMessage(channel3, user3, "책 20페이지 읽고 요약 올릴게요");
        System.out.println("[" + channelService.getChannelById(message3.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message3 + "] => 메시지 등록 완료");

        Message message4 = messageService.createMessage(channel3, user4, "운동 루틴 3일차 돌입!");
        System.out.println("[" + channelService.getChannelById(message4.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message4 + "] => 메시지 등록 완료\n");

        // === #off-topic(= #free-topic) 채널 ===
        System.out.println("💬메시지를 보낼 채널: " + channelService.getChannelById(channel4.getId()) + "\n");

        Message message5 = messageService.createMessage(channel4, user1, "점심 뭐 먹지 고민 중 🍜");
        System.out.println("[" + channelService.getChannelById(message5.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message5 + "] => 메시지 등록 완료");

        Message message6 = messageService.createMessage(channel4, user2, "어제 본 드라마 진짜 꿀잼이었음ㅋㅋ");
        System.out.println("[" + channelService.getChannelById(message6.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message6 + "] => 메시지 등록 완료");

        Message message7 = messageService.createMessage(channel4, user3, "고양이 사진 보러 왔어요 🐱");
        System.out.println("[" + channelService.getChannelById(message7.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message7 + "] => 메시지 등록 완료");

        Message message8 = messageService.createMessage(channel4, user4, "이모지 테스트 중 😂🔥💯");
        System.out.println("[" + channelService.getChannelById(message8.getChannel().getId()).get().getChannelName() + "] 채널에 [" + message8 + "] => 메시지 등록 완료");

        System.out.println("\n-------------------------------[ 메시지 단건 조회 ]-------------------------------");
        String findMessageId = message1.getId();
        String findMessageSenderId = message2.getUser().getId();
        String findMessageChannelId = message3.getChannel().getId();

        System.out.println("1. 메시지 아이디로 조회: " + findMessageId);
        System.out.println(messageService.getMessageById(findMessageId));

        System.out.println("\n2. 메시지 전송자 아이디로 조회: " + findMessageSenderId + "(" + userService.getUserById(findMessageSenderId).get().getUsername() + ")");
        messageService.getMessageByUserId(findMessageSenderId).forEach(System.out::println);

        System.out.println("\n3. 메시지 채널 아이디로 조회: " + findMessageChannelId + "(" + channelService.getChannelById(findMessageChannelId).get().getChannelName() + ")");
        messageService.getMessageByChannelId(findMessageChannelId).forEach(System.out::println);

        System.out.println("\n-------------------------------[ 메시지 다건 조회 ]-------------------------------");
        List<Message> messageList = messageService.getAllMessages();
        messageList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 메시지 수정 ]-------------------------------");

        System.out.println("1. 수정 전 메시지4: " + message4.getContent());

        System.out.println("\n2. 수정할 메시지 내용: '운동 루틴 3일차 성공! 내일도 화이팅!'");

        Message updatedMessage = messageService.updateMessage(message4.getId(), message4.getChannel(), message4.getUser(), "운동 루틴 3일차 성공! 내일도 화이팅!");
        System.out.println("\n3. 수정 후 메시지4: " + updatedMessage.getContent());

        System.out.println("\n-------------------------------[ 메시지 삭제 (Soft Delete) ]-------------------------------");

        System.out.println("1. 삭제 전 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);

        System.out.println("\n2. 삭제할 메시지: " + message7);
        messageService.deleteMessageById(message7.getId());

        messageList = messageService.getAllMessages();
        System.out.println("\n3. 삭제 후 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 삭제한 메시지 복원 ]-------------------------------");
        messageList = messageService.getAllMessages();
        System.out.println("1. 복원 전 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);

        Message deletedMessage = messageService
                .getMessageById(message7.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 메시지를 못 찾았습니다."));

        System.out.println("\n2. 복원할 메시지: " + deletedMessage);
        messageService.restoreMessageById(deletedMessage.getId());

        messageList = messageService.getAllMessages();
        System.out.println("\n3. 복원 후 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);

        System.out.println("\n-------------------------------[ 메시지 영구 삭제 (Hard Delete) ]-------------------------------");

        messageList = messageService.getAllMessages();
        System.out.println("1. 영구 삭제 전 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);

        deletedMessage = messageService
                .getMessageById(message7.getId())
                .orElseThrow(() -> new IllegalStateException("삭제된 메시지를 못 찾았습니다."));

        System.out.println("\n2. 삭제할 메시지: " + deletedMessage);

        messageService.deleteMessageById(deletedMessage.getId());
        System.out.println("\n3. 소프트 삭제 후, 영구 삭제 진행");
        messageService.hardDeleteMessageById(deletedMessage.getId());

        messageList = messageService.getAllMessages();
        System.out.println("\n4. 삭제 후 메시지 리스트("+messageList.size()+"개)");
        messageList.forEach(System.out::println);


        System.out.println("\n===================================================================================================");
        System.out.println("-------------------------------[ 유저 삭제 & 메시지 연쇄 삭제 확인 ]-------------------------------");

        userList = userService.getAllUsers();
        System.out.println("1. 유저 삭제 전 유저 리스트 목록 (" + userList.size() + "개)");
        userList.forEach(System.out::println);

        User userToDelete = user3;
        System.out.println("\n2. 삭제할 유저3: " + user3.getUsername());

        List<Message> user3Messages = messageService.getMessageByUserId(userToDelete.getId());
        System.out.println("\n3. 삭제할 유저3의 메시지 목록 (" + user3Messages.size() + "개)");
        user3Messages.forEach(System.out::println);

        // 유저 삭제
        userService.deleteUser(userToDelete);
        System.out.println("\n4. 유저3 삭제 완료");

        User deleteUser = userService
                .getUserById(userToDelete.getId())
                .orElseThrow(() -> new IllegalArgumentException("삭제된 유저를 못 찾았습니다."));

        // 유저 삭제 확인
        userList = userService.getAllUsers();
        System.out.println("\n5. 유저 삭제 후 유저 리스트 목록 (" + userList.size() + "개)");
        userList.forEach(System.out::println);

        // 유저 메시지 재조회
        user3Messages = messageService.getMessageByUserId(deleteUser.getId());
        System.out.println(">>> getMessages로 확인" + deletedUser.getMessages());

        System.out.println("\n6. 유저 삭제 후 유저 메시지 목록 (" + user3Messages.size() + "개)");
        user3Messages.forEach(System.out::println);

        if (user3Messages.isEmpty()) {
            System.out.println("-> 채널 관련 메시지 모두 삭제됨\n");
        }

        System.out.println("-------------------------------[ 채널 삭제 & 메시지 연쇄 삭제 확인 ]-------------------------------");

//        channels = channelService.getAllChannels();
        System.out.println("1. 채널 삭제 전 채널 리스트 목록 (" + channels.size() + "개)");
        channels.forEach(System.out::println);

        System.out.println("\n2. 삭제할 채널3: " + channel3.getChannelName());

        List<Message> channel3Messages = messageService.getMessageByChannelId(channel3.getId());
        System.out.println("\n3. 삭제할 채널3의 메시지 목록 (" + channel3Messages.size() + "개)");
        channel3Messages.forEach(System.out::println);

        // 채널 삭제
        channelService.softDeleteChannel(channel3);
        System.out.println("\n4. 채널3 삭제 완료");

        // 채널 삭제 확인
        channels = channelService.getAllChannels();
        System.out.println("\n5. 채널 삭제 후 채널 리스트 목록 (" + channels.size() + "개)");
        channels.forEach(System.out::println);
        Channel channelToDelete = channelService.getChannelById(channel3.getId())
                .orElseThrow(() -> new IllegalArgumentException("Channel Not Found"));

        // 채널 메시지 재조회
        channels = channelService.getAllChannels();
        List<Message> updateChannel3Messages = messageService.getMessageByChannelId(channelToDelete.getId());
        System.out.println("\n6. 채널 삭제 후 채널의 메시지 목록 (" + updateChannel3Messages.size() + "개)");
        updateChannel3Messages.forEach(System.out::println);

        if (updateChannel3Messages.isEmpty()) {
            System.out.println("-> 채널 관련 메시지 모두 삭제됨\n");
        }
    }
}