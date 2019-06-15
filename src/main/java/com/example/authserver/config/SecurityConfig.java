package com.example.authserver.config;

import com.example.authserver.service.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
public class SecurityConfig {

    @EnableAuthorizationServer
    @Configuration
    public static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        private final AuthenticationManager authenticationManager;
        private final KeyPair keyPair;
        private final UserClientService userClientService;

        public AuthorizationServerConfig(
                AuthenticationConfiguration authenticationConfiguration,
                KeyPair keyPair,
                UserClientService userClientService
        ) throws Exception {
            this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
            this.keyPair = keyPair;
            this.userClientService = userClientService;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(userClientService);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(this.authenticationManager)
                    .tokenStore(tokenStore())
                    .accessTokenConverter(accessTokenConverter())
            ;
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(this.keyPair);

            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            converter.setAccessTokenConverter(accessTokenConverter);

            return converter;
        }
    }

    @Configuration
    @RequiredArgsConstructor
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .csrf().disable()
            ;
        }
    }

    @Bean
    public KeyPair keyPair() {
        return new KeyStoreKeyFactory(
                new ClassPathResource("auth_server.jks"),
                "secret".toCharArray()
        ).getKeyPair("auth_server");
    }

}
