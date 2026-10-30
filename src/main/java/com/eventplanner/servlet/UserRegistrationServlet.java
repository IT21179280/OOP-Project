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
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles new user registration.
 */
@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (WebUtil.getLoggedUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        request.setAttribute("pageTitle", "Register");
        request.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = WebUtil.trim(request.getParameter("fullName"));
        String email = WebUtil.trim(request.getParameter("email"));
        String username = WebUtil.trim(request.getParameter("username"));
        String password = WebUtil.trim(request.getParameter("password"));
        String confirmPassword = WebUtil.trim(request.getParameter("confirmPassword"));
        String contactNumber = WebUtil.trim(request.getParameter("contactNumber"));

        Map<String, String> errors = new LinkedHashMap<>();

        if (fullName.isEmpty()) {
            errors.put("fullName", "Please enter your full name.");
        } else if (fullName.length() < 3 || fullName.length() > 100) {
            errors.put("fullName", "Full name must be between 3 and 100 characters.");
        }

        if (email.isEmpty()) {
            errors.put("email", "Please enter your email address.");
        } else if (!WebUtil.isValidEmail(email) || email.length() > 100) {
            errors.put("email", "Please enter a valid email address.");
        }

        if (username.isEmpty()) {
            errors.put("username", "Please choose a username.");
        } else if (username.length() < 4 || username.length() > 50) {
            errors.put("username", "Username must be between 4 and 50 characters.");
        } else if (!username.matches("^[A-Za-z0-9_.]+$")) {
            errors.put("username", "Username may only contain letters, numbers, dots and underscores.");
        }

        if (password.isEmpty()) {
            errors.put("password", "Please enter a password.");
        } else if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters long.");
        }

        if (confirmPassword.isEmpty()) {
            errors.put("confirmPassword", "Please confirm your password.");
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "The two passwords do not match.");
        }

        if (contactNumber.isEmpty()) {
            errors.put("contactNumber", "Please enter your contact number.");
        } else if (!WebUtil.isValidPhone(contactNumber) || contactNumber.length() > 20) {
            errors.put("contactNumber", "Please enter a valid contact number (9 to 20 characters).");
        }

        try {
            if (!errors.containsKey("username") && userDAO.isUsernameTaken(username)) {
                errors.put("username", "This username is already registered.");
            }
            if (!errors.containsKey("email") && userDAO.isEmailTaken(email)) {
                errors.put("email", "This email address is already registered.");
            }

            if (errors.isEmpty()) {
                User user = new User();
                user.setFullName(fullName);
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(PasswordUtil.hashPassword(password));
                user.setContactNumber(contactNumber);
                user.setRole("user");
                userDAO.create(user);

                WebUtil.setSuccessMessage(request.getSession(),
                        "Your account was created successfully. Please login to continue.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Registration failed because of a database error.", e);
        }

        request.setAttribute("errors", errors);
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("username", username);
        request.setAttribute("contactNumber", contactNumber);
        request.setAttribute("pageTitle", "Register");
        request.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(request, response);
    }
}
