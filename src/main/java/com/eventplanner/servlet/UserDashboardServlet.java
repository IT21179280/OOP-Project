package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.dao.EventDAO;
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
 * The main dashboard shown after a user logs in.
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();
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
            request.setAttribute("myEventCount", eventDAO.countByUser(user.getUserId()));
            request.setAttribute("myAdvertisementCount", advertisementDAO.countByUser(user.getUserId()));
            request.setAttribute("upcomingEventCount", eventDAO.countUpcoming());
            request.setAttribute("recentEvents", eventDAO.findRecentByUser(user.getUserId(), 5));
            request.setAttribute("pageTitle", "My Dashboard");
            request.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the dashboard.", e);
        }
    }
}
