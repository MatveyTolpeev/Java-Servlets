package ru.service.db.dao;

import ru.service.db.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EmployeesDaoJdbcImpl implements EmployeesDao {

    //language=SQL
    private final String SQL_SELECT_ALL =
            "SELECT * FROM employees";

    //language=SQL
    private final String SQL_SELECT_BY_ID =
            "SELECT * FROM employees WHERE id = ?";

    //language=SQL
    private final String SQL_SELECT_BY_NAME =
            "SELECT * FROM employees WHERE first_name = ? and second_name = ?";

    private final String SQL_INSERT_NEW_USER =
            "INSERT INTO employees (first_name, second_name, position_id) VALUES (?, ?, ?)";

    private Connection connection;

    public EmployeesDaoJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Integer> findByName(String firstName, String secondName) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NAME);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer employeeId = resultSet.getInt("employee_id");
                return Optional.of(employeeId);

            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(User model) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_NEW_USER);
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getSecondName());
            statement.setString(3, model.getPositionId());
            statement.executeQuery();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                Integer employeeId = resultSet.getInt("employee_id");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                String positionId = resultSet.getString("position_id");

                User user = User.builder()
                        .id(employeeId)
                        .firstName(firstName)
                        .secondName(secondName)
                        .positionId(positionId)
                        .build();

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
