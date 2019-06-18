package com.example.authserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String password;
    private Date creationDate;
    private Date updateDate;
    private boolean locked;
    private Integer accessTokenTtl;
    private Integer refreshTokenTtl;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
