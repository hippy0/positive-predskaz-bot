package me.hippy.prdsz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

/**
 * Сущность "Пользователь"
 *
 * @author hippy
 */
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private UserRole userRole;

    @Column(nullable = false, unique = true, updatable = false)
    private Long telegramId;
}
