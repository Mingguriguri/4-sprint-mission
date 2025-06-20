package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	static User setupUser(UserService userService) {
		CreateUserRequestDto userRequestDto = new CreateUserRequestDto(
				"woody",
				"woody@codeit.com",
				"woody1234",
				null
				);
		return userService.create(userRequestDto);
	}

	static Channel setupChannel(ChannelService channelService) {
		CreatePublicChannelRequestDto channelRequestDto = new CreatePublicChannelRequestDto(
				"공지",
				"공지 채널입니다"
			);

		return channelService.createPublicChannel(channelRequestDto);
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
		System.out.println("메시지 생성: " + message.getId() +  "-" + message.getContent() + "(" + message.getAuthorId() + "," + message.getCreatedAt() + ")");
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// context에서 Bean을 조회하여 각 서비스 구현체 할당
		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		// 테스트
		messageCreateTest(messageService, channel, user);
		System.out.println(channelService.findPublicChannel(channel.getId()));
	}

}
