package com.seoulog.user.entity;

import jakarta.persistence.*;
import lombok.*;

;import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    //탈퇴시 false
    private boolean activated;

    private String email;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        NATIVE, NAVER, KAKAO
    }

    private String oauthId;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> teamList = new ArrayList<>();

    public void updateUserInfo(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}