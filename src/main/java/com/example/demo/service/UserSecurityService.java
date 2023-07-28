package com.example.demo.service;

import com.example.demo.entity.UserSecurityInfo;
import com.example.demo.repository.UserInfoRepo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

  Logger LOG = LogManager.getLogger(UserSecurityService.class);

  @Autowired public UserInfoRepo userInfoRepo;

  @Autowired public PasswordEncoder passwordEncoder;

  public void createUserInDB(UserSecurityInfo userSecurityInfo) {
    userSecurityInfo.setPassword(passwordEncoder.encode(userSecurityInfo.getPassword()));
    LOG.info("password encoded {}", userSecurityInfo.getPassword());
    userInfoRepo.save(userSecurityInfo);
  }

  public List<UserSecurityInfo> fetchAllUsers() {
    LOG.info("Fetching all users from DB");
    return userInfoRepo.findAll();
  }
}
