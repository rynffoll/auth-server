package com.example.authserver.security;

import com.example.authserver.entity.Role;
import lombok.Data;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;

@Data(staticConstructor = "of")
public class SecurityRole implements GrantedAuthority {

    @Delegate
    private final Role role;

    @Override
    public String getAuthority() {
        return this.getName();
    }

}
