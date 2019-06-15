package com.example.authserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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

    private Set<Endpoint> endpoints;
}
