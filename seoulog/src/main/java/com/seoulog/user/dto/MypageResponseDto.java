package com.seoulog.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MypageResponseDto {

    public MypageResponseDto() {
        groupInfo = new ArrayList<>();
    }

    @Setter
    private UserDto userInfo;
    private ArrayList<TeamDto> groupInfo;
    public void addTeam(TeamDto team) {
        groupInfo.add(team);
    }

}
