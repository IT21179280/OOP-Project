package com.eventplanner.servlet;

import com.eventplanner.dao.AdminDAO;
import com.eventplanner.model.Admin;
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
 * Administrator login (separate from the normal user login).
 */
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (WebUtil.getLoggedAdmin(request) != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.setAttribute("pageTitle", "Administrator Login");
        request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = WebUtil.trim(request.getParameter("username"));
        String password = request.getParameter("password");

        if (username.isEmpty() || WebUtil.isBlank(password)) {
            showLoginForm(request, response, username, "Please enter the administrator username and password.");
            return;
        }

        try {
            Admin admin = adminDAO.findByUsername(username);

            if (admin == null || !PasswordUtil.checkPassword(password, admin.getPassword())) {
                showLoginForm(request, response, username, "Invalid administrator username or password.");
                return;
            }

            HttpSession session = request.getSession();
            session.removeAttribute("user");
            session.setAttribute("admin", admin);
            try {
                request.changeSessionId();
            } catch (IllegalStateException ignored) {
                // Nothing to do if the session has already ended.
            }

            WebUtil.setSuccessMessage(session, "Welcome to the administrator panel, " + admin.getUsername() + ".");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");

        } catch (SQLException e) {
            throw new ServletException("Administrator login failed because of a database error.", e);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response,
                               String username, String error) throws ServletException, IOException {
        request.setAttribute("loginError", error);
        request.setAttribute("adminUsername", username);
        request.setAttribute("pageTitle", "Administrator Login");
        request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
    }
}
