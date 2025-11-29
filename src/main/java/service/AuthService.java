package service;

import model.User;
import repository.UserHistoryRepository;
import repository.UserRepository;
import util.FileLogger;

/**
 * Handles authentication logic.
 */
public class AuthService {
    private final UserRepository userRepository;
    private final UserHistoryRepository historyRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
        this.historyRepository = new UserHistoryRepository();
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            historyRepository.logAction(user.getId(), "LOGIN");
            FileLogger.log("User logged in: " + username);
            return user;
        }
        return null;
    }

    public boolean register(String username, String email, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }
        // Additional email check could be added here
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.createUser(user);
        historyRepository.logAction(user.getId(), "REGISTER");
        FileLogger.log("User registered: " + username);
        return true;
    }
}
