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
     * @param username The username to search for
     * @return The User object, or null if not found
     * @throws SQLException if a database error occurs
     */
    User findByUsername(String username) throws SQLException;
    
    /**
     * Save a new user to the database.
     * @param user The user to save
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean save(User user) throws SQLException;
    
    /**
     * Update an existing user in the database.
     * @param user The user with updated information
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean update(User user) throws SQLException;
    
    /**
     * Delete a user from the database.
     * @param username The username of the user to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean delete(String username) throws SQLException;
}
