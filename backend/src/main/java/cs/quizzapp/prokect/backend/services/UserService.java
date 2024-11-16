package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.UserRepository;
import cs.quizzapp.prokect.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user.
     *
     * @param user The user object to register.
     * @return The saved user object.
     * @throws IllegalArgumentException if the username or email already exists.
     */
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        user.setRole("USER");  // Assign default role
        return userRepository.save(user);
    }

    /**
     * Find a user by their ID.
     *
     * @param id The ID of the user.
     * @return Optional containing the user if found.
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update an existing user.
     *
     * @param id   The ID of the user to update.
     * @param user The updated user object.
     * @return Optional containing the updated user if found.
     */
    public Optional<User> updateUser(Long id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(user.getUsername() != null ? user.getUsername() : existingUser.getUsername());
            existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(existingUser);
        });
    }

    /**
     * Delete a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return true if the user was deleted, false if the user was not found.
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
