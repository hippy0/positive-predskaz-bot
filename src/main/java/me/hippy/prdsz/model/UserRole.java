package me.hippy.prdsz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.hippy.prdsz.enums.UserRoleKey;

import java.util.List;

@Entity
@Getter
@Setter
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UserRoleKey key;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private List<User> users;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String instrumentalCaseName;
}
