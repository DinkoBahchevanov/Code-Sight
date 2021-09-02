package com.codesight.codesight_api.infrastructure.configuration.security;

import com.codesight.codesight_api.domain.user.repository.UserRepository;
import com.codesight.codesight_api.infrastructure.configuration.filters.CustomAuthenticationFilter;
import com.codesight.codesight_api.infrastructure.configuration.filters.CustomAuthorizationFilter;
import com.codesight.codesight_api.infrastructure.configuration.security.handlers.JWTAuthenticationEntryPoint;
import com.codesight.codesight_api.infrastructure.configuration.security.handlers.JWTAuthenticationFailureHandler;
import com.codesight.codesight_api.infrastructure.configuration.security.handlers.JWTAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private JWTAuthenticationEntryPoint entryPoint;
    private JWTAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new
                CustomAuthenticationFilter(authenticationManagerBean(), userRepository);
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth");

        http.csrf().disable();
//        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        }).accessDeniedHandler((request, response, accessDeniedException) -> {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//        });

        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(POST, "/api/v1/challenges").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/v1/challenges/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/auth/", "/api/auth/error","/http://localhost:8080/swagger-ui/#/").permitAll()
                .anyRequest().authenticated().and().exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(entryPoint)
                .and().formLogin().loginPage("/api/auth").failureHandler(failureHandler);
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.formLogin().failureHandler(failureHandler);
//        http.authorizeRequests().antMatchers(POST, "/api/auth").permitAll().anyRequest().authenticated().and().exceptionHandling().accessDeniedHandler(this.jwtAccessDeniedHandler);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtAccessDeniedHandler(JWTAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Autowired
    public void setFailureHandler(JWTAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Autowired
    public void setEntryPoint(JWTAuthenticationEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
    }
}
