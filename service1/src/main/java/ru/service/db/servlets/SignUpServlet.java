package ru.service.db.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.service.db.dao.EmployeesDao;
import ru.service.db.dao.EmployeesDaoJdbcImpl;
import ru.service.db.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    private EmployeesDao employeesDao;

    private static Logger logger = LoggerFactory.getLogger(SignUpServlet.class);

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
        logger.info("Creating User");
        RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/jsp/signUp.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name");
        String secondName = req.getParameter("second_name");
        String positionId = req.getParameter("position");
        Optional<Integer> optionalInteger = employeesDao.findByName(firstName, secondName);
        if (!optionalInteger.isPresent()) {
            User user = User.builder()
                    .firstName(firstName)
                    .secondName(secondName)
                    .positionId(positionId)
                    .build();
            employeesDao.save(user);
            logger.info("User Created");
        }

        doGet(req, resp);
    }
}
