package loanapproval.controller;

import loanapproval.dao.UserDao;
import loanapproval.model.User;
import loanapproval.model.User.UserType;

import java.util.List;

/**
 * Controller for handling user-related operations.
 */
public class UserController {
    private UserDao userDao;
    
    /**
     * Create a new UserController.
     */
    public UserController() {
        this.userDao = new UserDao();
    }
    
    /**
     * Get a user by ID.
     * @param userId The user ID to look up
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }
    
    /**
     * Get all users of a specific type.
     * @param userType The type of users to retrieve
     * @return List of users of the specified type
     */
    public List<User> getUsersByType(UserType userType) {
        return userDao.getUsersByType(userType);
    }
    
    /**
     * Update a user's profile.
     * @param user The user to update
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
    
    /**
     * Delete a user account.
     * @param userId The ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return userDao.deleteUser(userId);
    }
} 