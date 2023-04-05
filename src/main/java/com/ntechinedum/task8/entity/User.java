package com.ntechinedum.task8.entity;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Tasks> tasks;

}
