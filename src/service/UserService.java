package service;

import abstract_.BaseService;
import exception.BusinessException;
import exception.ValidationException;
import interface_.UserRepository;
import model.Role;
import model.User;
import repository.UserRepositoryImpl;
import util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService extends BaseService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public User register(String username, String password) {
        validateBeforeCreate(username);
        try {
            ensureUsernameUnique(username);
            User user = new User();
            user.setUsername(username);
            user.setPassword(PasswordUtil.hash(password));
            user.setRole(Role.USER);
            return userRepository.save(user);
        } catch (SQLException e) {
            throw new BusinessException("Unable to register user");
        }
    }

    public User login(String username, String password) {
        validateBeforeUpdate(username);
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                throw new ValidationException("Invalid username or password");
            }
            User user = userOpt.get();
            if (!PasswordUtil.matches(password, user.getPassword())) {
                throw new ValidationException("Invalid username or password");
            }
            return user;
        } catch (SQLException e) {
            throw new BusinessException("Unable to login");
        }
    }

    public User updateProfile(int userId, String newUsername, String oldPassword, String newPassword) {
        ensureStringNotEmpty(newUsername, "Username cannot be empty");
        ensureStringNotEmpty(oldPassword, "Old password cannot be empty");
        try {
            Optional<User> existing = userRepository.findByUsername(newUsername);
            if (existing.isPresent() && existing.get().getId() != userId) {
                throw new ValidationException("Username already exists");
            }
            User current = findById(userId);
            if (!PasswordUtil.matches(oldPassword, current.getPassword())) {
                throw new ValidationException("Old password is incorrect");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new ValidationException("New password cannot be empty");
            }
            current.setUsername(newUsername);
            current.setPassword(PasswordUtil.hash(newPassword));
            return userRepository.update(current);
        } catch (SQLException e) {
            throw new BusinessException("Unable to update profile");
        }
    }

    public List<User> listUsers() {
        try {
            return userRepository.findAll();
        } catch (SQLException e) {
            throw new BusinessException("Unable to list users");
        }
    }

    private User findById(int id) throws SQLException {
        return userRepository.findAll().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("User not found"));
    }

    private void ensureUsernameUnique(String username) throws SQLException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new ValidationException("Username already exists");
        }
    }

    @Override
    protected void validateBeforeCreate(Object value) {
        ensureStringNotEmpty((String) value, "Username is required");
    }

    @Override
    protected void validateBeforeUpdate(Object value) {
        ensureStringNotEmpty((String) value, "Username is required");
    }
}
