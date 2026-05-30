package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.*;

import java.time.LocalDate;
import java.util.List;

/**
 * UserController is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for retrieving and creating users.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;
    private final UserProvider userProvider;
    private final UserMapper userMapper;

    @GetMapping
    List<UserDto> getUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/simple")
    List<SimpleUserDto> getSimpleUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toSimpleUserDto)
                .toList();
    }

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable Long id) {
        return userProvider.getUser(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/email")
    List<UserEmailDto> getUsersByEmail(@RequestParam String email) {
        return userProvider.findByEmailContainingIgnoreCase(email).stream()
                .map(userMapper::toUserEmailDto)
                .toList();
    }

    @GetMapping("/older/{time}")
    List<UserDto> getUsersOlderThan(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate time
    ) {
        return userProvider.findUsersOlderThan(time).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto addUser(@RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User createdUser = userService.createUser(user);

        return userMapper.toUserDto(createdUser);
    }

    @PutMapping("/{userId}")
    UserDto updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto userDto
    ) {
        User updatedUser = userService.updateUser(userId, userMapper.toUser(userDto));

        return userMapper.toUserDto(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}