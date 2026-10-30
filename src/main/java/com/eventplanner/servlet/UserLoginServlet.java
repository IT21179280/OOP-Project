package com.eventplanner.servlet;

import com.eventplanner.dao.UserDAO;
import com.eventplanner.model.User;
import com.eventplanner.util.PasswordUtil;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles user login using either the username or the email address.
 */
@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (WebUtil.getLoggedUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }
        if (WebUtil.getLoggedAdmin(request) != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.setAttribute("pageTitle", "Login");
        request.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = WebUtil.trim(request.getParameter("login"));
        String password = request.getParameter("password");

        if (login.isEmpty() || WebUtil.isBlank(password)) {
            showLoginForm(request, response, login, "Please enter your username/email and password.");
            return;
        }

        try {
            User user = userDAO.findByLogin(login);

            if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
                showLoginForm(request, response, login, "Invalid username/email or password. Please try again.");
                return;
            }

            HttpSession session = request.getSession();
            session.removeAttribute("admin");
            session.setAttribute("user", user);
            try {
                request.changeSessionId();
            } catch (IllegalStateException ignored) {
                // The session was already invalidated; nothing to do.
            }

            WebUtil.setSuccessMessage(session, "Welcome back, " + user.getFullName() + "!");
            response.sendRedirect(request.getContextPath() + "/user/dashboard");

        } catch (SQLException e) {
            throw new ServletException("Login failed because of a database error.", e);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response,
                               String login, String error) throws ServletException, IOException {
        request.setAttribute("loginError", error);
        request.setAttribute("login", login);
        request.setAttribute("pageTitle", "Login");
        request.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(request, response);
    }
}
