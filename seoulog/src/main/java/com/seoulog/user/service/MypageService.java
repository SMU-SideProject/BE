package com.seoulog.user.service;

import com.seoulog.user.dto.MypageResponseDto;
import com.seoulog.user.dto.TeamDto;
import com.seoulog.user.dto.UserDto;
import com.seoulog.user.entity.Team;
import com.seoulog.user.entity.User;
import com.seoulog.user.entity.UserTeam;
import com.seoulog.user.repository.TeamRepository;
import com.seoulog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ArrayList<Team> teamNameList = new ArrayList<>();

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
