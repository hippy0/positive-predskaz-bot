package me.hippy.prdsz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import me.hippy.prdsz.enums.WishPicture;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность "Предсказание"
 *
 * @author hippy
 */
@Setter
@Getter
@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true)
    private String content;

    @Column(unique = true)
    private String linkToTelegramPhoto;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'SLEEPY_FROG'")
    private WishPicture wishPicture = WishPicture.SLEEPY_FROG;
}
