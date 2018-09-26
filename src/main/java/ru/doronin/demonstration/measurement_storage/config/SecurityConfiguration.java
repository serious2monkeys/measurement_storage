package ru.doronin.demonstration.measurement_storage.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import ru.doronin.demonstration.measurement_storage.security.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationContext context;
    private final CustomUserDetailService userDetailsService;

    public SecurityConfiguration(ApplicationContext context,
                                 CustomUserDetailService userDetailsService) {
        this.context = context;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public RoleHierarchy hierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_CUSTOMER");
        return hierarchy;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/assets/**")
                .antMatchers("/fonts/**")
                .antMatchers("/js/**")
                .antMatchers("/styles/**")
                .antMatchers("/i/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(hierarchy());
        handler.setApplicationContext(context);

        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic()
                .and()
                .formLogin()/*.loginPage("/login").permitAll()*/.usernameParameter("login").passwordParameter("password")
                .and()
                .userDetailsService(userDetailsService)
                .authorizeRequests()
                .expressionHandler(handler)
                .anyRequest().authenticated()
                .antMatchers("/rest/**").permitAll();
    }
}
