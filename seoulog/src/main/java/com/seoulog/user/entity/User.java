package com.seoulog.user.entity;

import jakarta.persistence.*;
import lombok.*;

;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "activated") //탈퇴시 false
    private boolean activated;

    @Column(nullable = false)
    private String email;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        NATIVE, NAVER, KAKAO
    }

}