package account.security;

import account.bruteforce.LoginAuthenticationFailureHandler;
import account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;


    //try
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("api/admin/user/role", "api/admin/user", "api/admin/user").hasRole("ADMINISTRATOR")
                .mvcMatchers("api/acct/payments", "api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers("api/empl/payment").hasAnyRole("ACCOUNTANT", "USER")
                .mvcMatchers("api/auth/changepass").hasAnyRole("ADMINISTRATOR", "USER", "ACCOUNTANT")
                .mvcMatchers("api/admin/user").hasRole("ADMINISTRATOR")
                .mvcMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                .mvcMatchers("/", "api/auth/signup", "/h2-console", "/actuator/shutdown", "api/auth/signup").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())
//                 .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();

    }


    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public LoginAuthenticationFailureHandler failureHandler() {
        LoginAuthenticationFailureHandler failureHandler = new LoginAuthenticationFailureHandler();
        failureHandler.setDefaultFailureUrl("/login?error=true");
        return failureHandler;
    }

//    try
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getEncoder());
        daoAuthenticationProvider.setUserDetailsService(accountService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }



}
