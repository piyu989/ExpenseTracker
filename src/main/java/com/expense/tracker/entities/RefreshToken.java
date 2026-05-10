package com.expense.tracker.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Table(name = "refresh_token")
@Builder
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String token;
    private Instant expireDate;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private UserInfo userInfo;

}