package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Loads the data shown on the public home page.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();
    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("upcomingEvents", eventDAO.findUpcoming(3));
            request.setAttribute("recentAdvertisements", advertisementDAO.findRecent(3));
            request.setAttribute("categories", categoryDAO.findAll());
            request.setAttribute("totalUpcomingEvents", eventDAO.countUpcoming());
            request.setAttribute("pageTitle", "Home");
            request.getRequestDispatcher("/WEB-INF/views/public/home.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the home page data.", e);
        }
    }
}
