package com.seoulog.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
