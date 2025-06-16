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
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

// 이 부분은 팩토리 패턴을 일부 구현하기 위해서 만든 클래스입니다
// JCFUser
public class DiscodeitFactory {
    private final PersistenceType type;

    public DiscodeitFactory(PersistenceType type) {
        this.type = type;
    }

    public UserService createUserService() {
        switch (type) {
            case JCF:
                // JCFUserService -> BasicUserService로 교체
                return new BasicUserService(new JCFUserRepository(), new JCFMessageRepository());
            case FILE:
                // FileUserService -> BasicUserService로 교체
                return new BasicUserService(new FileUserRepository(), new FileMessageRepository());
            default:
                return null;
        }
    }

    public ChannelService createChannelService() {
        switch (type) {
            case JCF:
                // JCFChannelService -> BasicChannelService로 교체
                return new BasicChannelService(new JCFChannelRepository(), new JCFMessageRepository());
            case FILE:
                // FileChannelService -> BasicChannelService로 교체
                return new BasicChannelService(new FileChannelRepository(), new FileMessageRepository());
            default:
                return null;
        }
    }

    public MessageService creteMessageService() {
        switch (type) {
            case JCF:
                // JCFMessageService -> BasicMessageService
                return new BasicMessageService(new JCFMessageRepository());
            case FILE:
                // FileMessageService -> BasicMessageService
                return new BasicMessageService(new FileMessageRepository());
            default:
                return null;
        }
    }
}
