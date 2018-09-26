package ru.doronin.demonstration.measurement_storage.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация службы определения пользователя для Spring Security
 */
@Component
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загрузка пользователя для контекста безопасности по имени
     *
     * @param username - имя пользователя
     * @return - экземпляр UserDetails
     * @throws UsernameNotFoundException - при отсутствии пользователя в базе
     */
    @SuppressWarnings("unchecked")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByLogin(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), getGrantedAuthorities(user));
    }


    /**
     * Получение полномочий пользователя
     *
     * @param user - пользователь
     * @return - список GrantedAuthority
     */
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (user.getRole()) {
            case ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case PLAIN_CUSTOMER:
                authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
                break;
            default:
                throw new IllegalArgumentException("Unknown user role");
        }
        return authorities;
    }
}
