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
    public static User getUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // create a new user record
    public static boolean createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // update an existing user record
    public static boolean updateUser(User user) {
        try {
            return userRepository.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
