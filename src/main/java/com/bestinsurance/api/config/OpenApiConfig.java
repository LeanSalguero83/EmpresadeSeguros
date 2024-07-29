package com.bestinsurance.api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oauthflow.authorizationurl}",
                tokenUrl = "${springdoc.oauthflow.tokenurl}",
                scopes = {
                    @OAuthScope(name = "openid", description = "openid"),
                    @OAuthScope(name = "profile", description = "profile")
                })))
public class OpenApiConfig {}
