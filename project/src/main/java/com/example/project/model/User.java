package com.example.project.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id") // Tên cột chứa khóa ngoại
    private Role role;

}
