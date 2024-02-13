package com.myblog.config;

import com.myblog.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // After adding this annotation goto the controller layer which all things to be admin should be accessd  go and write @PreAuthorize("hasRole(ADMIN)")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*  What is configure method does?
    => Here we configure which URL how it needs to be accessed*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }


/**    What is InMemoryAuthentication?
  =>When you are developing an application If we don't want to store username and password to database
  If you want store your usename and password in the form of object then we are using inMemoryAuthentication
  this is for testing */
 /**   @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(getpasswordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder().username("admin").password(getpasswordEncoder().encode("admin")).roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user,admin);
    }*/

    protected void configure(AuthenticationManagerBuilder auth ) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(getpasswordEncoder());
    }
}
