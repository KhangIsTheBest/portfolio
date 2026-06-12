package com.khangdt.portfolio.auth.repository;

import com.khangdt.portfolio.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
