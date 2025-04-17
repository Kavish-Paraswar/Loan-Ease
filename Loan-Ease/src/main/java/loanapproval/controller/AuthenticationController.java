package loanapproval.controller;

import loanapproval.dao.UserDao;
import loanapproval.model.User;

/**
 * Controller for handling user authentication and registration.
 */
public class AuthenticationController {
    private UserDao userDao;
    
    /**
     * Create a new AuthenticationController.
     */
    public AuthenticationController() {
        this.userDao = new UserDao();
    }
    
    /**
     * Authenticate a user with username and password.
     * @param username The username to authenticate
     * @param password The password to verify
     * @return User object if authentication is successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        return userDao.authenticateUser(username, password);
    }
    
    /**
     * Register a new user.
     * @param user The user to register
     * @return true if registration is successful, false otherwise
     */
    public boolean registerUser(User user) {
        // Check if username already exists
        if (userDao.usernameExists(user.getUsername())) {
            return false;
        }
        
        // Insert the new user
        return userDao.insertUser(user);
    }
    
    /**
     * Update an existing user's profile.
     * @param user The user to update
     * @return true if update is successful, false otherwise
     */
    public boolean updateUserProfile(User user) {
        return userDao.updateUser(user);
    }
    
    /**
     * Check if a username already exists.
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return userDao.usernameExists(username);
    }
} 