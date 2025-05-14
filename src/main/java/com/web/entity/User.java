package com.web.entity;

import com.web.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String email;

    private String password;

    private Boolean actived;

    private String phone;

    private String fullName;

    private String address;

    private String activationKey;

    private Date createdDate;

    private String rememberKey;

    @Enumerated(EnumType.STRING)
    private Role role;
}
