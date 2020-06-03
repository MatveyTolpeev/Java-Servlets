package ru.service.db.servlets;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.service.db.dao.OrdersRepository;
import ru.service.db.dao.OrdersRepositoryImpl;
import ru.service.db.models.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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

    // в случае GET-запроса следует просто отдать страницу home
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("user");

        if (userId == null) {
            throw new RuntimeException("User not found in session");
        }

        List<Order> forEmployee = ordersRepository.findForEmployee(Integer.parseInt(userId));

        req.setAttribute("ordersFromServer", forEmployee);

        req.getServletContext().getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
