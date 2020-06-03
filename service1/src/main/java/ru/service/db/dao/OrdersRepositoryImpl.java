package ru.service.db.dao;

import ru.service.db.models.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrdersRepositoryImpl implements OrdersRepository {
    private final String SQL_FIND_ALL_ORDERS =
            "SELECT * FROM orders";

    private final String SQL_FIND_ALL_ORDERS_FOR_EMPLOYEE =
            "SELECT * FROM orders WHERE employee_id = ?";

    private final String SQL_INSERT_ORDER =
            "INSERT INTO orders (transport_id, client_id, employee_id, service_id, start_date, planning_end_date) \n" +
                    "VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'))";

    private Connection connection;

    public OrdersRepositoryImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Order> findForEmployee(Integer employeeId) {
        try {
            List<Order> orders = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ORDERS_FOR_EMPLOYEE);
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer orderId = resultSet.getInt("order_id");
                Integer transportId = resultSet.getInt("transport_id");
                Integer clientId = resultSet.getInt("client_id");
                Integer serviceId = resultSet.getInt("service_id");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startDate = LocalDateTime.parse(resultSet.getString("start_date"), formatter);
                LocalDateTime planningEndDate = LocalDateTime.parse(resultSet.getString("planning_end_date"), formatter);

                Order order = Order.builder()
                        .orderId(orderId)
                        .transportId(transportId)
                        .clientId(clientId)
                        .employeeId(employeeId)
                        .serviceId(serviceId)
                        .startDate(startDate)
                        .planningEndDate(planningEndDate)
                        .build();

                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ORDER);
            statement.setInt(1,order.getTransportId());
            statement.setInt(2,order.getClientId());
            statement.setInt(3,order.getEmployeeId());
            statement.setInt(4,order.getServiceId());
            statement.setString(5,order.getStartDate().toLocalDate().toString());
            statement.setString(6,order.getStartDate().toLocalDate().toString());
            statement.executeQuery();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public List<Order> getAllOrders() {
        try {
            List<Order> orders = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_ORDERS);
            while (resultSet.next()) {
                Integer orderId = resultSet.getInt("order_id");
                Integer transportId = resultSet.getInt("transport_id");
                Integer employeeId = resultSet.getInt("employee_id");
                Integer serviceId = resultSet.getInt("service_id");
                Integer clientId = resultSet.getInt("client_id");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(resultSet.getString("start_date"));
                LocalDateTime startDate = LocalDateTime.parse(resultSet.getString("start_date"), formatter);
                LocalDateTime planningEndDate = LocalDateTime.parse(resultSet.getString("planning_end_date"), formatter);

                Order order = Order.builder()
                        .orderId(orderId)
                        .transportId(transportId)
                        .clientId(clientId)
                        .employeeId(employeeId)
                        .serviceId(serviceId)
                        .startDate(startDate)
                        .planningEndDate(planningEndDate)
                        .build();

                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
