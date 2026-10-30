package com.eventplanner.servlet;

import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.dao.EventDAO;
import com.eventplanner.model.Event;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Shows the list of events (with search and category filter) and the
 * details of a single event.
 *
 * Examples:
 *   /events
 *   /events?keyword=music&category=3&time=upcoming
 *   /events?id=5
 */
@WebServlet("/events")
public class EventServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        try {
            if (!WebUtil.isBlank(idParameter)) {
                showEventDetails(request, response, idParameter);
            } else {
                showEventList(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Could not load events from the database.", e);
        }
    }

    private void showEventDetails(HttpServletRequest request, HttpServletResponse response, String idParameter)
            throws SQLException, ServletException, IOException {

        Integer eventId = WebUtil.getIntParameter(request, "id");
        if (eventId == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found.");
            return;
        }

        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested event could not be found.");
            return;
        }

        request.setAttribute("event", event);
        request.setAttribute("pageTitle", "Event Details");
        request.getRequestDispatcher("/WEB-INF/views/public/event-details.jsp").forward(request, response);
    }

    private void showEventList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String keyword = WebUtil.trim(request.getParameter("keyword"));
        Integer categoryId = WebUtil.getIntParameter(request, "category");
        String timeframe = WebUtil.trim(request.getParameter("time"));

        if (!"upcoming".equals(timeframe) && !"past".equals(timeframe) && !"all".equals(timeframe)) {
            timeframe = "upcoming";
        }

        List<Event> events = eventDAO.search(keyword, categoryId, timeframe);

        request.setAttribute("events", events);
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("timeframe", timeframe);
        request.setAttribute("pageTitle", "Events");
        request.getRequestDispatcher("/WEB-INF/views/public/events.jsp").forward(request, response);
    }
}
