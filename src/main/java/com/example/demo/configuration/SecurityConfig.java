package com.example.demo.configuration;

import com.example.demo.constants.SecurityConstants;
import com.example.demo.service.UserSecurityInfoUserDetailsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @EnableWebSecurity // this is optional now
// @EnableMethodSecurity                 (for method level security)
public class SecurityConfig {

  // URL's
  // http://localhost:9090/security/demo/publicMethod
  // http://localhost:9090/security/demo/adminMethod
  // http://localhost:9090/DbUserHandling/createNewUser
  // http://localhost:9090/DbUserHandling/getAllUser
  // http://localhost:9090/swagger-ui/index.html
  // http://localhost:9090/actuator

  @Value("${user1.name}")
  private String user1Name;

  @Value("${user2.name}")
  private String user2Name;

  @Value("${user3.name}")
  private String user3Name;

  @Value("${user1.pass}")
  private String user1Pass;

  @Value("${user2.pass}")
  private String user2Pass;

  @Value("${user3.pass}")
  private String user3Pass;

  @Value("${user1.role}")
  private String user1Role;

  @Value("${user2.role}")
  private String user2Role;

  @Value("${user3.role}")
  private String user3Role;

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

    //     For In-Memory user's Security config
    //    httpSecurity
    //        .csrf(AbstractHttpConfigurer::disable)
    //        .authorizeHttpRequests(
    //            (authorizeHttpRequests) ->
    //                authorizeHttpRequests
    //                    .requestMatchers("/security/demo/publicMethod")
    //                    .permitAll()
    //                    .requestMatchers("/DbUserHandling/createNewUser")
    //                    .hasRole(SecurityConstants.ROLE_HOKAGE)
    //                    .requestMatchers("/DbUserHandling/getAllUser")
    //                    .hasAnyRole(
    //                        SecurityConstants.ROLE_HOKAGE,
    //                        SecurityConstants.ROLE_JONIN,
    //                        SecurityConstants.ROLE_CHUNIN)
    //                    .requestMatchers("/security/demo/adminMethod")
    //                    .hasRole(SecurityConstants.ROLE_JONIN)
    //                    .anyRequest()
    //                    .authenticated())
    //        .formLogin(Customizer.withDefaults());

    // For DB user's Security config
    // CSRF protection is enabled and it uses 'Synchronizer Token Pattern' technique to resolve the
    // CSRF issue by default. So generally we should not disable it, but here we are learning things
    // so no issues.
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorizeHttpRequests) ->
                authorizeHttpRequests
                    .requestMatchers("/security/demo/publicMethod")
                    .permitAll()
                    .requestMatchers("/DbUserHandling/createNewUser")
                    .hasAuthority(SecurityConstants.ROLE_HOKAGE)
                    .requestMatchers("/DbUserHandling/getAllUser")
                    .hasAnyAuthority(
                        SecurityConstants.ROLE_HOKAGE,
                        SecurityConstants.ROLE_JONIN,
                        SecurityConstants.ROLE_CHUNIN)
                    .requestMatchers("/security/demo/adminMethod")
                    .hasAuthority(SecurityConstants.ROLE_JONIN)
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults());

    //     when we need to provide security on our API's Directly not from configuration.
    //        httpSecurity                                          (for method level security)
    //            .csrf(AbstractHttpConfigurer::disable)
    //            .authorizeHttpRequests(
    //                (authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated())
    //            .formLogin(Customizer.withDefaults());
    return httpSecurity.build();
  }

  //    Below configuration is for In-Memory User

  // let's create some in-memory users with different roles
  // One thing when we will use inMemory users, the users from application.yaml file will not be
  // considered.
  //    @Bean
  //    public UserDetailsService userDetailsService() {
  //      UserDetails user1 =
  //          User.withUsername(user1Name)
  //              .password(passwordEncoder().encode(user1Pass))
  //              .roles(user1Role)
  //              .build();
  //
  //      UserDetails user2 =
  //          User.withUsername(user2Name)
  //              .password(passwordEncoder().encode(user2Pass))
  //              .roles(user2Role)
  //              .build();
  //
  //      UserDetails user3 =
  //          User.withUsername(user3Name)
  //              .password(passwordEncoder().encode(user3Pass))
  //              .roles(user3Role)
  //              .build();
  //
  //      InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
  //      inMemoryUserDetailsManager.createUser(user1);
  //      inMemoryUserDetailsManager.createUser(user2);
  //      inMemoryUserDetailsManager.createUser(user3);
  //
  //      return inMemoryUserDetailsManager;
  //    }

  // Below configuration is for DB-User
  @Bean
  public UserDetailsService userDetailsService() {
    // With the help of 'UserSecurityInfo' entity we will get the details of the user from DB
    // but we need to convert that details into the class which is readable by
    // 'UserDetailsService'
    // interface so we will create one more class which will implement that interface and then
    // we will convert the details fetched from db into that object.
    return new UserSecurityInfoUserDetailsService();
  }

  // instead of saving passwords directly into the UserDetails we will use 'PasswordEncoder'.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }
}
