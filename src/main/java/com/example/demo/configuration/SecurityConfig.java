package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @EnableMethodSecurity                 (for method level security)
public class SecurityConfig {

  @Value("${user1.name}")
  private String user1Name;

  @Value("${user2.name}")
  private String user2Name;

  @Value("${user1.pass}")
  private String user1Pass;

  @Value("${user2.pass}")
  private String user2Pass;

  @Value("${user1.role}")
  private String user1Role;

  @Value("${user2.role}")
  private String user2Role;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // 1-> Here if we see then for API '/publicMethod' we provided access to all users but when it
    // comes to API 'adminMethod' then only registered users with 'user1Role' are allowed and for
    // other API's like 'http://localhost:9090/actuator' only registered users with any Roles are
    // allowed.
    // 2-> If we want to define multiple roles then we can use method 'hasAnyRole(multiple role
    // strings)'.
    // 3-> We can also provide regex for URL's like '/security/demo/**' means anything after
    // '/security/demo/'.
    // 4-> Here we provided security for all the API's in our project.

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorizeHttpRequests) ->
                authorizeHttpRequests
                    .requestMatchers("/security/demo/publicMethod")
                    .permitAll()
                    .requestMatchers("/security/demo/adminMethod")
                    .hasRole(user1Role)
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults());
    return httpSecurity.build();

    // when we need to provide security on our API's Directly not from configuration.
    //    httpSecurity                                          (for method level security)
    //        .csrf(AbstractHttpConfigurer::disable)
    //        .authorizeHttpRequests(
    //            (authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated())
    //        .formLogin(Customizer.withDefaults());
    //    return httpSecurity.build();
  }

  // let's create some in-memory users with different roles
  // One thing when we will use inMemory users, the users from application.yaml file will not be
  // considered.
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 =
        User.withUsername(user1Name)
            .password(passwordEncoder().encode(user1Pass))
            .roles(user1Role)
            .build();

    UserDetails user2 =
        User.withUsername(user2Name)
            .password(passwordEncoder().encode(user2Pass))
            .roles(user2Role)
            .build();

    InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    inMemoryUserDetailsManager.createUser(user1);
    inMemoryUserDetailsManager.createUser(user2);

    return inMemoryUserDetailsManager;
  }

  // instead of saving passwords directly into the UserDetails we will use 'PasswordEncoder'.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
