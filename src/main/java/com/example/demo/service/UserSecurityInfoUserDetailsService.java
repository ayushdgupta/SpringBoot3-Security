package com.example.demo.service;

import com.example.demo.entity.UserSecurityInfo;
import com.example.demo.repository.UserInfoRepo;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

// this class is a mediator Bw UserDetailsService And UserSecurityInfo
@Component
public class UserSecurityInfoUserDetailsService implements UserDetailsService {

  Logger LOG = LogManager.getLogger(UserSecurityInfoUserDetailsService.class);

  @Autowired private UserInfoRepo userInfoRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserSecurityInfo> userSecurityInfoOptional = userInfoRepo.findByUserName(username);

    LOG.info("Fetched the userSecurityInfo from DB {}", userSecurityInfoOptional.get());

    // Now we need to convert that userInfo fetched from DB into UserDetails object because
    // return type of this method is UserDetails.
    return userSecurityInfoOptional
        .map(UserSecurityInfoUserDetailsConverter::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not exist in the system"));
  }
}
