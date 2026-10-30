package com.eventplanner.servlet;

import com.eventplanner.dao.EventDAO;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Lets the administrator view, search, add, edit and delete events.
 */
@WebServlet("/admin/events")
public class AdminEventsServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = WebUtil.trim(request.getParameter("keyword"));

        try {
            request.setAttribute("events", eventDAO.search(keyword, null, "all"));
            request.setAttribute("keyword", keyword);
            request.setAttribute("pageTitle", "Manage Events");
            request.getRequestDispatcher("/WEB-INF/views/admin/events.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the events.", e);
        }
    }
}
