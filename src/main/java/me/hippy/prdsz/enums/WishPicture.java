package me.hippy.prdsz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление изображения для предсказания.
 *
 * @author hippy
 */
@Getter
@RequiredArgsConstructor
public enum WishPicture {

    BROKEN_HEART_FROG("broken-frog.jpg"),
    RAINY_FROG("rainy-frog.jpg"),
    SLEEPY_FROG("sleepy-frog.jpg"),
    LUCKY_FROG("lucky-frog.jpg");

    private final String path;
}
