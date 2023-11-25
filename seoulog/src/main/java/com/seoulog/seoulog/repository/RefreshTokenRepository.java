package com.seoulog.seoulog.repository;

import com.seoulog.seoulog.dto.TokenDto;
import com.seoulog.seoulog.entity.User;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final EntityManager em;
    public User findByEmail(String email) {
        return em.createQuery("select u.refreshToken from User as u where u.email=:email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void saveToken(String refreshToken, String email) {
         em.createQuery("update User as u set u.refreshToken=:refreshToken where u.email=:email")
                .setParameter("refreshToken", refreshToken)
                .setParameter("email", email)
                 .executeUpdate();
        System.out.println("RefreshTokenRepository.saveToken");
    }
}
