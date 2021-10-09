package com.icicles.wmms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * OAuth 资源服务器配置
 * @author damon
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final String DEMO_RESOURCE_ID = "wmms";
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Since we want the protected resources to be accessible in the UI as well we need
        // session creation to be allowed (it's disabled by default in 2.0.6)
            http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/sysParam/findByKey").permitAll()
                .antMatchers("/meterDeal/testConfigAddr").permitAll()
                .antMatchers("/meterDeal/testConfigVal").permitAll()
                .antMatchers("/meterDeal/testConfigPulse").permitAll()
                .antMatchers("/meterDeal/testConfigCodeVal").permitAll()
                .antMatchers("/meterDeal/testReadSingle").permitAll()
                .antMatchers("/loginout").permitAll()
                .antMatchers("/wxBind").permitAll()
                .antMatchers("/refreshToken").permitAll()
                .antMatchers("/oauth/*").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers("/wmms/tools/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // 对option不校验
                //.antMatchers(ignoreResources).permitAll()
                .anyRequest().authenticated();
    }

}
