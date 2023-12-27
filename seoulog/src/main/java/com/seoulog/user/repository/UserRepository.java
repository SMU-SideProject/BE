package com.seoulog.user.repository;

import com.seoulog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //jpaRepository를 extends해서 findAll, save등의 메소드를 기본적으로 사용가능

    User findOneWithAuthoritiesByEmail(String email);

    User findByNickname(String nickname);

    User findByOauthId(String oauthId);

}