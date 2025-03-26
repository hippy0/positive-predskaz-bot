package me.hippy.prdsz.service;

import lombok.RequiredArgsConstructor;
import me.hippy.prdsz.dao.UserRepository;
import me.hippy.prdsz.dao.UserRoleRepository;
import me.hippy.prdsz.dto.InputUserDto;
import me.hippy.prdsz.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис по работе с пользователями.
 *
 * @author hippy
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void create(InputUserDto userDto) {
        User user = new User();

        user.setUserRole(userRoleRepository.findByKey(userDto.getRoleKey()).get());
        user.setTelegramId(userDto.getTelegramId());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId).orElse(null);
    }
}
