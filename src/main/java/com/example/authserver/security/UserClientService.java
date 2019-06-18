package com.example.authserver.security;

import com.example.authserver.entity.Role;
import com.example.authserver.entity.User;
import com.example.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserClientService implements ClientDetailsService {

    private final UserService service;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        User user = service.getByName(clientId);
        return SecurityClient.builder()
                .clientId(user.getName())
                .clientSecret(user.getPassword())
                // TODO add column for grant_types
                .authorizedGrantTypes(Stream.of("client_credentials").collect(Collectors.toSet()))
                .scope(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .authorities(user.getRoles().stream().map(SecurityRole::of).collect(Collectors.toSet()))
                .build();
    }

}
