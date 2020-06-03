package ru.service.db.dao;


import ru.service.db.models.Order;

import java.util.List;

public interface OrdersRepository {
    List<Order> getAllOrders();

    List<Order> findForEmployee(Integer employeeId);

    void save(Order order);
}
