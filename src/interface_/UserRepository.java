package interface_;

import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username) throws SQLException;

    User save(User user) throws SQLException;

    User update(User user) throws SQLException;

    List<User> findAll() throws SQLException;
}
