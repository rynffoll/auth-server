package com.example.authserver.service;

import com.example.authserver.entity.SecurityClient;
import com.example.authserver.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserClientService implements UserDetailsService, ClientDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.getByName(username);
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        User user = service.getByName(clientId);
        return SecurityClient.builder()
                .clientId(user.getName())
                .clientSecret(user.getPassword())
                .authorizedGrantTypes(Stream.of("client_credentials").collect(Collectors.toSet()))
                .scope(Stream.of("none").collect(Collectors.toSet()))
                .authorities(Stream.of("USER").map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
                .build()
                ;
    }

}
