package com.seoulog.user.controller;

import com.seoulog.common.annotation.CurrentUser;
import com.seoulog.user.dto.MypageResponseDto;
import com.seoulog.user.entity.User;
import com.seoulog.user.repository.UserRepository;
import com.seoulog.user.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final UserRepository userRepository;

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDto> getUserInfo(@CurrentUser User user) {

        MypageResponseDto mypageResponseDto = new MypageResponseDto();
        mypageService.getUserInfo(user, mypageResponseDto);
        mypageService.getTeamList(user, mypageResponseDto);
        return new ResponseEntity<>(mypageResponseDto, HttpStatus.OK);
    }

}
