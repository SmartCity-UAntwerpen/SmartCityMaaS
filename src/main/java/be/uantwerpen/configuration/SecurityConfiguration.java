package be.uantwerpen.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by dries on 29/05/2017.
 */

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/users/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll()
                .and().authorizeRequests().antMatchers("/console/**").permitAll()
                .and().authorizeRequests().antMatchers("/webjars/**").permitAll().anyRequest().fullyAuthenticated()
                .and().formLogin().loginPage("/login")
                .failureUrl("/login?error").and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .exceptionHandling().accessDeniedPage("/access?error");
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

/*
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        //Permit access to H2 console --Development only (Order of defining is important!)
        http.authorizeRequests().antMatchers("/h2console/**")
                .permitAll();

        http.authorizeRequests().antMatchers("/webjars/**")
                .permitAll();

        http.authorizeRequests().antMatchers("/login")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .exceptionHandling()
                .accessDeniedPage("/Access?accessdenied");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }*/

}
