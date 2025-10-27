package database;

import database.repository.UserRepository;
import database.repository.UserRepositoryImpl;
import model.User;

import java.sql.SQLException;

/**
 * Facade for database operations.
 * Provides backward compatibility while delegating to repository pattern.
 */
public class DatabaseUtil {
    private static final UserRepository userRepository = new UserRepositoryImpl();
    
    /**
     * Get user from database by username.
     */
    
    public static User getUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Create a new user in the database.
     */
    
    public static boolean createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update an existing user in the database.
     */
    
    public static boolean updateUser(User user) {
        try {
            return userRepository.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
