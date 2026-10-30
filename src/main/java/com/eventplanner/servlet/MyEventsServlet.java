package com.eventplanner.servlet;

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
 * Lists the events created by the logged in user.
 */
@WebServlet("/user/events")
public class MyEventsServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = WebUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            request.setAttribute("events", eventDAO.findByUser(user.getUserId()));
            request.setAttribute("pageTitle", "My Events");
            request.getRequestDispatcher("/WEB-INF/views/user/my-events.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load your events.", e);
        }
    }
}
