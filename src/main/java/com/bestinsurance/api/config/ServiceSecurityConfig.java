package com.bestinsurance.api.config;

import com.bestinsurance.api.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class ServiceSecurityConfig {

    @Bean
    public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {
        //http.anonymous();
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui.html")).permitAll()
                        .requestMatchers(HttpMethod.GET, "/subscriptions/*/*", "/subscriptions")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.PUT, "/subscriptions/*/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/subscriptions/*/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.POST,  "/subscriptions")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.POST,  "/subscriptions/upload")
                        .hasAnyAuthority( Role.BACK_OFFICE.value(), Role.ADMIN.value())// Role.ADMIN.value())
                        .requestMatchers(HttpMethod.GET,  "/subscriptions/revenues")
                        .hasAnyAuthority( Role.BACK_OFFICE.value(), Role.ADMIN.value())

                        .requestMatchers(HttpMethod.GET, "/policies/*", "/policies")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.PUT, "/policies/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/policies/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.POST,  "/policies")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())

                        .requestMatchers(HttpMethod.GET, "/coverages/*", "/coverages")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.PUT, "/coverages/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/coverages/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.POST,  "/coverages")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())

                        .requestMatchers(HttpMethod.GET, "/customers/*", "/customers")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.PUT, "/customers/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        .requestMatchers(HttpMethod.DELETE, "/customers/*")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value())
                        .requestMatchers(HttpMethod.POST,  "/customers")
                        .hasAnyAuthority(Role.FRONT_OFFICE.value(), Role.BACK_OFFICE.value(), Role.ADMIN.value(), Role.CUSTOMER.value())
                        //Other settings for the customerController are in the controller class with method annotations

                        .anyRequest().authenticated())
                //.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
                .oauth2ResourceServer((oauth2ResourceServer) ->
                        oauth2ResourceServer
                                .jwt((jwt) ->
                                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                                )
                );

        return http.csrf().disable().build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
