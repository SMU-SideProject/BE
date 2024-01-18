package com.seoulog.user.service;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import com.seoulog.user.dto.MypageResponseDto;
import com.seoulog.user.dto.TeamDto;
import com.seoulog.user.dto.UserDto;
import com.seoulog.user.entity.User;
import com.seoulog.user.entity.UserTeam;
import com.seoulog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Transactional
    public void updateUserInfo(User user, UserDto userDto) {

        if (userDto.getNickname().isBlank()) {
            throw new BusinessException(ErrorCode.SIGNUP_NICKNAME_EMPTY);
        } else if (user.getNickname().equals(userDto.getNickname())) {
            throw new BusinessException(ErrorCode.SIGNUP_REDUNDANT_NICKNAME);
        }
        User updateUser = userRepository.findOneWithAuthoritiesByEmail(user.getEmail());
        updateUser.updateUserInfo(userDto.getNickname(), encoder.encode(userDto.getPassword())); //더티체킹

    }

    @Transactional
    public MypageResponseDto getUserInfo(User user, MypageResponseDto mypageResponseDto) {
        UserDto userDto = UserDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();

        mypageResponseDto.setUserInfo(userDto);
        return mypageResponseDto;

    }

    @Transactional
    public MypageResponseDto getTeamList(User user, MypageResponseDto mypageResponseDto) {
        User findUser = userRepository.findByNickname(user.getNickname());
        List<UserTeam> userTeams = findUser.getTeamList();

        for (UserTeam userTeam : userTeams) {
            mypageResponseDto.getGroupInfo().add(new TeamDto(userTeam.getTeam().getTeamName()));
        }

        return mypageResponseDto;
    }
}
