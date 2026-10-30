package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.dao.ContactMessageDAO;
import com.eventplanner.dao.EventDAO;
import com.eventplanner.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrator dashboard with basic system statistics.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final EventDAO eventDAO = new EventDAO();
    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ContactMessageDAO contactMessageDAO = new ContactMessageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("totalUsers", userDAO.countAll());
            request.setAttribute("totalEvents", eventDAO.countAll());
            request.setAttribute("upcomingEvents", eventDAO.countUpcoming());
            request.setAttribute("totalAdvertisements", advertisementDAO.countAll());
            request.setAttribute("totalCategories", categoryDAO.countAll());
            request.setAttribute("totalMessages", contactMessageDAO.countAll());
            request.setAttribute("recentUsers", userDAO.findRecent(5));
            request.setAttribute("recentEvents", eventDAO.findRecent(5));
            request.setAttribute("pageTitle", "Admin Dashboard");
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the administrator dashboard.", e);
        }
    }
}
