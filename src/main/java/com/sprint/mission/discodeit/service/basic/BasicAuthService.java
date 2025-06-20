package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    @Qualifier("JCFUserRepository")
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User login(LoginUserDto loginUserDto) {
        // username으로 사용자 조회
        User user = userRepository.findByUsername(loginUserDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 비밀번호가 일치하는지 조회
        if (!user.getPassword().equals(loginUserDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 같지 않습니다.");
        }

        // 로그인으로 인한 온라인 시간 갱신
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserStatus newUserStatus = new UserStatus(user.getId());
                    userStatusRepository.save(newUserStatus);
                    return newUserStatus;
                });
        userStatus.touch();
        userStatus.updateLastConnectedAt();

        return user;
    }
}
