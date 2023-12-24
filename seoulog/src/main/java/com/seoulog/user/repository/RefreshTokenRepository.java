package com.seoulog.user.repository;

import com.seoulog.user.entity.User;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
