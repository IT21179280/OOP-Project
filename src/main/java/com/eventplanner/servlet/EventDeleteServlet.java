package com.eventplanner.servlet;

import com.eventplanner.dao.EventDAO;
import com.eventplanner.model.Event;
import com.eventplanner.model.User;
import com.eventplanner.util.FileUploadUtil;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Deletes an event. Only the owner of the event or an administrator is
 * allowed to delete it. The request must be sent with POST.
 */
@WebServlet(urlPatterns = {"/user/events/delete", "/admin/events/delete"})
public class EventDeleteServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer eventId = WebUtil.getIntParameter(request, "id");
        if (eventId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event id is missing.");
            return;
        }

        try {
            Event event = eventDAO.findById(eventId);
            if (event == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The event could not be found.");
                return;
            }

            if (!canManageEvent(request, event)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to delete this event.");
                return;
            }

            eventDAO.delete(eventId);
            File uploadDirectory = FileUploadUtil.getUploadDirectory(getServletContext());
            FileUploadUtil.deleteImage(uploadDirectory, event.getEventImage());

            WebUtil.setSuccessMessage(request.getSession(), "The event was deleted successfully.");
            response.sendRedirect(request.getContextPath()
                    + (isAdminMode(request) ? "/admin/events" : "/user/events"));

        } catch (SQLException e) {
            throw new ServletException("Could not delete the event.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Events can only be deleted using the delete button.");
    }

    private boolean canManageEvent(HttpServletRequest request, Event event) {
        if (isAdminMode(request)) {
            return WebUtil.getLoggedAdmin(request) != null;
        }
        User user = WebUtil.getLoggedUser(request);
        return user != null
                && event.getUserId() != null
                && event.getUserId() == user.getUserId();
    }

    private boolean isAdminMode(HttpServletRequest request) {
        return request.getServletPath().startsWith("/admin");
    }
}
