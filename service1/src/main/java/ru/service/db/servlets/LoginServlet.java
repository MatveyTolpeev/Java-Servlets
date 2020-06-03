package ru.service.db.servlets;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.service.db.dao.EmployeesDaoJdbcImpl;
import ru.service.db.dao.EmployeesDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;



@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private String passwordForAllUsers = "qwerty007";

    private EmployeesDao employeesDao;

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

            employeesDao = new EmployeesDaoJdbcImpl(dataSource);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name");
        String secondName = req.getParameter("second_name");
        String curPassword = req.getParameter("password");
        Optional<Integer> optionalInteger = employeesDao.findByName(firstName, secondName);

        if (optionalInteger.isPresent() && curPassword.equals(passwordForAllUsers)) {
            // создаем для него сессию
            HttpSession session = req.getSession();
            // кладем в атрибуты сессии атрибут user с именем пользователя
            session.setAttribute("user", optionalInteger.get().toString());
            // перенаправляем на страницу home
            req.getServletContext().getRequestDispatcher("/home").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
