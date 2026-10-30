package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.model.User;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Lists the advertisements published by the logged in user.
 */
@WebServlet("/user/advertisements")
public class MyAdvertisementsServlet extends HttpServlet {

    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = WebUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            request.setAttribute("advertisements", advertisementDAO.findByUser(user.getUserId()));
            request.setAttribute("pageTitle", "My Advertisements");
            request.getRequestDispatcher("/WEB-INF/views/user/my-advertisements.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load your advertisements.", e);
        }
    }
}
