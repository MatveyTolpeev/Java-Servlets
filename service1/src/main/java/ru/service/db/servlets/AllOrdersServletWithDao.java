package ru.service.db.servlets;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.service.db.dao.OrdersRepository;
import ru.service.db.dao.OrdersRepositoryImpl;
import ru.service.db.models.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@WebServlet("/allorders")
public class AllOrdersServletWithDao extends HttpServlet {
    private OrdersRepository ordersRepository;

    @Override
    public void init() throws ServletException {
        Properties properties = new Properties();
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        try {
            properties.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
            String dbUrl = properties.getProperty("db.url");
            String dbUsername = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");

            dataSource.setUsername(dbUsername);
            dataSource.setPassword(dbPassword);
            dataSource.setUrl(dbUrl);

            ordersRepository = new OrdersRepositoryImpl(dataSource);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Order> allOrders = ordersRepository.getAllOrders();
        req.setAttribute("ordersFromServer", allOrders);
        req.getServletContext().getRequestDispatcher("/jsp/allorders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // вытащили данные регистрации
        Integer transportId = Integer.parseInt(req.getParameter("transport_id"));
        Integer clientId = Integer.parseInt(req.getParameter("client_id"));
        Integer employeeId = Integer.parseInt(req.getParameter("employee_id"));
        Integer serviceId = Integer.parseInt(req.getParameter("service_id"));

        Order order = Order.builder()
                .transportId(transportId)
                .clientId(clientId)
                .employeeId(employeeId)
                .serviceId(serviceId)
                .startDate(LocalDateTime.now())
                .planningEndDate(LocalDateTime.now().plusHours(5))
                .build();

        ordersRepository.save(order);

        doGet(req, resp);
    }
}
