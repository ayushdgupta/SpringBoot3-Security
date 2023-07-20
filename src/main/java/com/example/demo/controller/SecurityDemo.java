package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security/demo")
public class SecurityDemo {

  @GetMapping("/publicMethod")
  public ResponseEntity<?> publicUser() {
    return new ResponseEntity<>("Method for Normal User", HttpStatus.OK);
  }

  @GetMapping("/adminMethod")
  //  @PreAuthorize("hasRole('Hokage')")      (for method level security)
  public ResponseEntity<?> adminUser() {
    return new ResponseEntity<>("Method for Admin User", HttpStatus.OK);
  }
}
