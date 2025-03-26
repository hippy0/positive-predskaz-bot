package me.hippy.prdsz.service;

import lombok.RequiredArgsConstructor;
import me.hippy.prdsz.dao.WishRepository;
import me.hippy.prdsz.model.Wish;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис по работе с предсказаниями.
 *
 * @author hippy
 */
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    @Transactional(readOnly = true)
    public Wish findRandom() {
        return wishRepository.findRandom();
    }

    @Transactional
    public void create(Wish wish) {
        wishRepository.save(wish);
    }

    @Transactional
    public List<Wish> linkToTelegram() {
        return wishRepository.findAllByLinkToTelegramPhotoIsNull();
    }

    @Transactional
    public List<Wish> findAll() {
        return wishRepository.findAll();
    }
}
