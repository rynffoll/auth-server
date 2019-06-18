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
public class Role {
    private int id;
    private String name;
    private Date creationDate;
    private Date updateDate;
    private boolean locked;

    @Builder.Default
    private Set<Endpoint> endpoints = new HashSet<>();
}
