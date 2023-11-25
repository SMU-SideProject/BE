package com.seoulog.seoulog.repository;

import com.seoulog.seoulog.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface

UserRepository extends JpaRepository<User, Long> {
    //jpaRepository를 extends해서 findAll, save등의 메소드를 기본적으로 사용가능

    User findOneWithAuthoritiesByEmail(String email);
    User findByNickname(String nickname);
    //유저 정보와 권한 정보도 같이 가져오는 메소드
}