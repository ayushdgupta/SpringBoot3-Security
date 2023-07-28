package com.example.demo.controller;

import com.example.demo.entity.UserSecurityInfo;
import com.example.demo.service.UserSecurityService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DbUserHandling")
public class DBUserController {

  @Autowired public UserSecurityService userSecurityService;

  @PostMapping("/createNewUser")
  public ResponseEntity<?> createUser(@RequestBody UserSecurityInfo userSecurityInfo) {
    userSecurityService.createUserInDB(userSecurityInfo);
    return new ResponseEntity<>("user created", HttpStatus.CREATED);
  }

  @GetMapping("/getAllUser")
  public ResponseEntity<?> getUser() {
    List<UserSecurityInfo> userSecurityInfo = userSecurityService.fetchAllUsers();
    return new ResponseEntity<>(userSecurityInfo, HttpStatus.OK);
  }
}
