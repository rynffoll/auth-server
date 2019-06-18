package com.example.authserver.security;

import com.example.authserver.entity.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleEndpointAccessDecisionVoter implements AccessDecisionVoter<Object> {

    private final static String URI_PARAMETER = "uri";

    private final SecurityRequestContext securityRequestContext;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // TODO locked user?
        User user = (User) authentication.getPrincipal();

        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes());

        if (servletRequestAttributes == null) {
            return AccessDecisionVoter.ACCESS_DENIED;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();
        String method = request.getMethod();
        String uri = request.getParameter(URI_PARAMETER);

        Optional<Endpoint> endpoint = user.getAuthorities()
                .stream()
                .filter(r -> r instanceof SecurityRole)
                .map(SecurityRole.class::cast)
                .filter(r -> !r.isLocked())
                .flatMap(r -> r.getEndpoints().stream())
                .filter(e -> method.matches(e.getMethod()) && uri.matches(e.getPath()))
                .findAny();

        if (endpoint.isPresent()) {
            String service = endpoint.get().getService();
            securityRequestContext.setService(service);
            return AccessDecisionVoter.ACCESS_GRANTED;
        }

        return AccessDecisionVoter.ACCESS_DENIED;
    }
}
