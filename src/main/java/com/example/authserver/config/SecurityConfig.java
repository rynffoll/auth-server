package com.example.authserver.config;

import com.example.authserver.security.RoleEndpointAccessDecisionVoter;
import com.example.authserver.security.ServiceJwtAccessTokenConverter;
import com.example.authserver.security.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.security.KeyPair;
import java.util.Arrays;

@Configuration
//    @EnableAuthorizationServer
@Import({
        AuthorizationServerEndpointsConfiguration.class,
        // Use CustomAuthorizationServerSecurityConfiguration instead
//            AuthorizationServerSecurityConfiguration.class
})
@RequiredArgsConstructor
public class SecurityConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserClientService userClientService;

    @Autowired
    private RoleEndpointAccessDecisionVoter roleEndpointAccessDecisionVoter;

    @Autowired
    private ServiceJwtAccessTokenConverter serviceJwtAccessTokenConverter;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(userClientService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(serviceJwtAccessTokenConverter)
        ;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(serviceJwtAccessTokenConverter);
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

    @Bean
    public KeyPair keyPair() {
        return new KeyStoreKeyFactory(
                new ClassPathResource("auth_server.jks"),
                "secret".toCharArray()
        ).getKeyPair("auth_server");
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new UnanimousBased(
                Arrays.asList(
                        new WebExpressionVoter(),
                        roleEndpointAccessDecisionVoter
                )
        );
    }

}
