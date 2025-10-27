package database.repository;

import model.User;
import java.sql.SQLException;

/**
 * Repository interface for User data access.
 * Follows Repository Pattern and Dependency Inversion Principle.
 */
public interface UserRepository {
    /**
     * Find a user by username.
     */
    User findByUsername(String username) throws SQLException;
    
    /**
     * Save a new user to the database.
     */
    boolean save(User user) throws SQLException;
    
    /**
     * Update an existing user in the database.
     */
    boolean update(User user) throws SQLException;
    
    /**
     * Delete a user from the database.
     */
    boolean delete(String username) throws SQLException;
}
