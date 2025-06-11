package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class DiscodeitFactory {
    // JCF 기반 구현체로 실제 비즈니스 로직이 처리되는 Service 인스턴스
    UserService userService = new FileUserService();
    ChannelService channelService = new FileChannelService();
    MessageService messageService = new FileMessageService(userService, channelService);

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
