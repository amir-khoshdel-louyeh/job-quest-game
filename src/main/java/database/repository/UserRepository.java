package database.repository;

import model.User;
import java.sql.SQLException;

/**
 * Repository interface for User data access.
 * Follows Repository Pattern and Dependency Inversion Principle.
 */
public interface UserRepository {
    // find user by username
    User findByUsername(String username) throws SQLException;

    // save a new user
    boolean save(User user) throws SQLException;

    // update an existing user
    boolean update(User user) throws SQLException;

    // delete a user by username
    boolean delete(String username) throws SQLException;
}
