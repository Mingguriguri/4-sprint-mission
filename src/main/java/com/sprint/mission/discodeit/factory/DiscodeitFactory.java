package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

// 이 부분은 팩토리 패턴을 일부 구현하기 위해서 만든 클래스입니다
public class DiscodeitFactory {
    private final PersistenceType type;

    public DiscodeitFactory(PersistenceType type) {
        this.type = type;
    }

    public UserService getUserService() {
        if (type == PersistenceType.JCF) {
            return new JCFUserService(new JCFUserRepository(), new JCFMessageRepository());
        } else {
            return new FileUserService(new FileUserRepository(), new FileMessageRepository());
        }
    }

    public ChannelService getChannelService() {
        if (type == PersistenceType.JCF) {
            return new JCFChannelService(new JCFChannelRepository(), new JCFMessageRepository());
        } else {
            return new FileChannelService(new FileChannelRepository(), new FileMessageRepository());
        }
    }

    public MessageService getMessageService() {
        if (type == PersistenceType.JCF) {
            return new JCFMessageService(new JCFMessageRepository());
        } else {
            return new FileMessageService(new FileMessageRepository());
        }
    }
}
