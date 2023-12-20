package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT a FROM User a WHERE a.deleteFlag = false ")
    Page<User> findAll(Pageable pageable);

    @Query("SELECT a FROM User a WHERE LOWER(a.lastName) " +
            "LIKE LOWER(concat('%', :lastName, '%')) or LOWER(a.email) LIKE LOWER(concat('%', :lastName, '%'))")
    Page<User> findNameOrEmail (String lastName, Pageable pageable);
}
