package com.example.demo.repository;

import com.example.demo.entity.UserSecurityInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends JpaRepository<UserSecurityInfo, String> {
  Optional<UserSecurityInfo> findByUserName(String username);
}
