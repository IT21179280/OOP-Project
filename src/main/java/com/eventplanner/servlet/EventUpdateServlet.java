package com.eventplanner.servlet;

import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.dao.EventDAO;
import com.eventplanner.model.Category;
import com.eventplanner.model.Event;
import com.eventplanner.model.User;
import com.eventplanner.util.EventFormUtil;
import com.eventplanner.util.FileUploadUtil;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Edits an existing event. Users may only edit their own events; the
 * administrator may edit every event.
 */
@WebServlet(urlPatterns = {"/user/events/edit", "/admin/events/edit"})
@MultipartConfig(maxFileSize = 6 * 1024 * 1024, maxRequestSize = 8 * 1024 * 1024)
public class EventUpdateServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Event event = loadEvent(request, response);
            if (event == null) {
                return;
            }
            if (!canManageEvent(request, event)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to edit this event.");
                return;
            }
            prepareForm(request, event);
            request.getRequestDispatcher("/WEB-INF/views/user/event-form.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the event.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean adminMode = isAdminMode(request);

        try {
            Integer eventId = WebUtil.getIntParameter(request, "eventId");
            if (eventId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event id is missing.");
                return;
            }

            Event existingEvent = eventDAO.findById(eventId);
            if (existingEvent == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The event could not be found.");
                return;
            }
            if (!canManageEvent(request, existingEvent)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to edit this event.");
                return;
            }

            Map<String, String> errors = new LinkedHashMap<>();
            Event event = EventFormUtil.readEvent(request, errors);
            event.setEventId(existingEvent.getEventId());
            event.setUserId(existingEvent.getUserId());
            event.setEventImage(existingEvent.getEventImage());

            if (!errors.containsKey("categoryId")) {
                Category category = categoryDAO.findById(event.getCategoryId());
                if (category == null) {
                    errors.put("categoryId", "Please select a valid event category.");
                }
            }

            File uploadDirectory = FileUploadUtil.getUploadDirectory(getServletContext());
            String newImage = existingEvent.getEventImage();

            if (errors.isEmpty()) {
                Part imagePart = getImagePart(request, errors);
                if (errors.isEmpty()) {
                    try {
                        newImage = FileUploadUtil.saveImage(imagePart, uploadDirectory, existingEvent.getEventImage());
                        event.setEventImage(newImage);
                    } catch (IllegalArgumentException e) {
                        errors.put("eventImage", e.getMessage());
                    }
                }
            }

            if (errors.isEmpty()) {
                try {
                    eventDAO.update(event);
                } catch (SQLException e) {
                    if (!java.util.Objects.equals(newImage, existingEvent.getEventImage())) {
                        FileUploadUtil.deleteImage(uploadDirectory, newImage);
                    }
                    throw e;
                }

                if (!java.util.Objects.equals(newImage, existingEvent.getEventImage())) {
                    FileUploadUtil.deleteImage(uploadDirectory, existingEvent.getEventImage());
                }

                WebUtil.setSuccessMessage(request.getSession(), "The event was updated successfully.");
                response.sendRedirect(request.getContextPath()
                        + (adminMode ? "/admin/events" : "/user/events"));
                return;
            }

            prepareForm(request, event);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/views/user/event-form.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not update the event.", e);
        }
    }

    private Event loadEvent(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Integer eventId = WebUtil.getIntParameter(request, "id");
        if (eventId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event id is missing.");
            return null;
        }
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The event could not be found.");
        }
        return event;
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

    private Part getImagePart(HttpServletRequest request, Map<String, String> errors) {
        try {
            return request.getPart("eventImage");
        } catch (IllegalStateException e) {
            errors.put("eventImage", "The image is too large. Please select an image smaller than 5 MB.");
        } catch (IOException | ServletException e) {
            errors.put("eventImage", "The image could not be uploaded. Please try again.");
        }
        return null;
    }

    private void prepareForm(HttpServletRequest request, Event event) throws SQLException {
        boolean adminMode = isAdminMode(request);
        request.setAttribute("event", event);
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("formMode", "edit");
        request.setAttribute("formAction", adminMode ? "/admin/events/edit" : "/user/events/edit");
        request.setAttribute("cancelUrl", adminMode ? "/admin/events" : "/user/events");
        request.setAttribute("adminMode", adminMode);
        request.setAttribute("pageTitle", "Edit Event");
    }

    private boolean isAdminMode(HttpServletRequest request) {
        return request.getServletPath().startsWith("/admin");
    }
}
