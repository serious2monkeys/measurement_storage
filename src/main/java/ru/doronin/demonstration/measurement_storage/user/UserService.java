package ru.doronin.demonstration.measurement_storage.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Служба работы с пользователями
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Поиск пользователя по логину
     *
     * @param login - логин
     * @return - результат поиска, завернутый в Optional
     */
    @Transactional
    public Optional<? extends User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Получение текущего пользователя
     *
     * @return
     */
    @Transactional
    public User getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return findByLogin(authentication.getName()).orElseThrow(() -> new NoSuchElementException("Not found"));
        }
        return null;
    }

    /**
     * Сохранение пользователя в базу
     *
     * @param user - пользователь
     * @return - сохраненный экземпляр пользователя
     */
    @Transactional
    public User save(User user) {
        user.setPassword(encodePassword(user));
        return (User) userRepository.save(user);
    }

    /**
     * Шифрование пароля пользователя
     *
     * @param user - пользователь
     * @return
     */
    private String encodePassword(User user) {
        if (user.getId() == null) {
            return user.getPassword() == null ? null : passwordEncoder.encode(user.getPassword());
        }
        User savedUser = (User) userRepository.findById(user.getId()).get();
        if (user.getPassword() == null) {
            return savedUser.getPassword();
        }
        if (!savedUser.getPassword().equals(user.getPassword())) {
            return passwordEncoder.encode(user.getPassword());
        }
        return user.getPassword();
    }
}
