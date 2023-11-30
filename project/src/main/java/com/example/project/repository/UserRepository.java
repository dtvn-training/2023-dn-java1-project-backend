package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT a FROM User a WHERE a.deleteFlag = 0")
    Page<User> findAll(Pageable pageable);
}
