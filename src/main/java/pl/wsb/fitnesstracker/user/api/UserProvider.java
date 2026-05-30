package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserProvider {

    Optional<User> getUser(Long userId);

    Optional<User> getUserByEmail(String email);

    List<User> findAllUsers();

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findUsersOlderThan(LocalDate date);
}
