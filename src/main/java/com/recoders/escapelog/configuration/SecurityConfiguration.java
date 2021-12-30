package com.recoders.escapelog.configuration;

import com.recoders.escapelog.security.UserLoginFailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserLoginFailHandler loginFailHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        //TODO - "/insert_theme" : 테마정보 삽입
        
        http.authorizeRequests()

                .mvcMatchers("/user/**","/send_check_token_email","/change_nickname","/change_pw","/map",
                        "/library","/library/{no}","/recode/**","/recode/theme_search").authenticated()
                .mvcMatchers("/","/signup","/login","/doLogin","/doLogout",
                        "/check_nickname","/find_pw","/check_email","/send_code_email","/check_code","/find_change_pw","/email_check_token",
                        "/themes","/themes/{no}","/theme_search","/feedback/add","/feedback/info","/insert_theme","/review_filter", "/admin/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("email")
                .passwordParameter("pwd")
                .defaultSuccessUrl("/",true)
                .failureHandler(loginFailHandler)

                .and()
                .logout()
                .logoutUrl("/doLogout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true);


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
