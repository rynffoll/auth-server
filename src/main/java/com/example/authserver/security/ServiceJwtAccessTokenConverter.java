package com.example.authserver.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Map;

@Component
public class ServiceJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private final static String URI_PARAMETER = "uri";
    private final static String SERVICE_PARAMETER = "service";

    private final SecurityRequestContext securityRequestContext;

    public ServiceJwtAccessTokenConverter(SecurityRequestContext securityRequestContext,
                                          KeyPair keyPair,
                                          AccessTokenConverter accessTokenConverter) {
        this.securityRequestContext = securityRequestContext;
        this.setKeyPair(keyPair);
        this.setAccessTokenConverter(accessTokenConverter);
    }


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        OAuth2AccessToken token = super.enhance(accessToken, authentication);
        Map<String, Object> additionalInformation = token.getAdditionalInformation();
        additionalInformation.put(URI_PARAMETER, authentication.getOAuth2Request().getRequestParameters().get(URI_PARAMETER));
        additionalInformation.put(SERVICE_PARAMETER, securityRequestContext.getService());
        return token;
    }

}
