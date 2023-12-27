package com.seoulog.user.repository;

import com.seoulog.user.entity.User;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    @Modifying
    @Query("update User u set u.refreshToken = :refreshToken where u.email =:email")
    void save(String refreshToken, String email);

    @Modifying
    @Query("update User u set u.refreshToken =:refreshToken where u.oauthId =:oauthId and u.type = :type")
    void save(String refreshToken, String oauthId, User.Type type);
}
