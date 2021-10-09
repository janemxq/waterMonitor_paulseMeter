package com.icicles.wmms;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    private static final String VERSION = "1.0";

    @Value("${auth-server-url}")
    String authServerUrl;

    @Value("${auth-token-url}")
    String authTokenUrl;

    @Value("${auth-authorize-url}")
    String authAuthorizeUrl;

    @Value("${info.app.desc:接口文档}")
    private String serviceDesc;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${security.oauth2.client.client-id:client_1}")
    String clientId;

    @Value("${security.oauth2.client.client-secret:123456}")
    String clientSecret;

    @Autowired
    private ServletContext servletContext;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(serviceName + serviceDesc)
                .description("测试版本")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .contact(new Contact("damon", "", "715645233@qq.com"))
                .version(VERSION)
                .build();
    }

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.icicles"))
                .paths(PathSelectors.any())
                .build()
                //.globalOperationParameters(setHeaderToken())
                .securitySchemes(Lists.newArrayList(oauth()))
                //.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()))
                .securityContexts(Lists.newArrayList(securityContext()));
    }

   /* private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }*/

    @Bean
    SecurityScheme  apiKey() {
        return new ApiKey("BearerToken", "swagger", "header");
    }

    private SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("oauth2")
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }

    private List<AuthorizationScope> scopes() {
        List<AuthorizationScope> list = new ArrayList<>();
        list.add(new AuthorizationScope("read", "Grants read access"));
        list.add(new AuthorizationScope("write", "Grants write access"));
        list.add(new AuthorizationScope("all", "Grants all access"));
        return list;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }
    List<SecurityReference> defaultAuth() {
        /*AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return Lists.newArrayList(
                new SecurityReference("Authorization", authorizationScopes));*/

        return Lists.newArrayList(
                new SecurityReference("oauth2", scopes().toArray(new AuthorizationScope[0])));
    }

    @Bean
    List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(authTokenUrl);
        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant =
                new ResourceOwnerPasswordCredentialsGrant(authTokenUrl);
        AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(new TokenRequestEndpoint(authAuthorizeUrl
                , "clientId", "clientSecret"), new TokenEndpoint(authTokenUrl, "value"));
        ImplicitGrant implicitGrant = new ImplicitGrant(new LoginEndpoint(authTokenUrl), "value");
        grantTypes.add(resourceOwnerPasswordCredentialsGrant);
        grantTypes.add(implicitGrant);
        grantTypes.add(authorizationCodeGrant);
        grantTypes.add(clientCredentialsGrant);
        return grantTypes;
    }
/*    @Bean
    List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
                keycloakProperties.getClient().getUserAuthorizationUri(),
                keycloakProperties.getClient().getClientId(), keycloakProperties.getClient().getClientSecret());
        TokenEndpoint tokenEndpoint = new TokenEndpoint(keycloakProperties.getClient().getAccessTokenUri(), "access_token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
    }*/

    @Bean
    public SecurityConfiguration securityInfo() {
        return SecurityConfigurationBuilder.builder()
                .appName(serviceName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(",")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }
}