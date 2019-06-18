package com.example.authserver.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityClient implements ClientDetails {

    private String clientId;
    private Set<String> resourceIds;
    private boolean secretRequired;
    private String clientSecret;
    private boolean scoped;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Collection<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Set<String> autoApproveScopes;
    private Map<String, Object> additionalInformation;

    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if (auto.equals("true") || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

}
