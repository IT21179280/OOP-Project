package com.eventplanner.servlet;

import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.dao.EventDAO;
import com.eventplanner.model.Admin;
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
 * Creates a new event. The page is used by registered users and by the
 * administrator (through /admin/events/new).
 */
@WebServlet(urlPatterns = {"/user/events/new", "/admin/events/new"})
@MultipartConfig(maxFileSize = 6 * 1024 * 1024, maxRequestSize = 8 * 1024 * 1024)
public class EventCreateServlet extends HttpServlet {

    private final EventDAO eventDAO = new EventDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAllowed(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            prepareForm(request, new Event());
            request.getRequestDispatcher("/WEB-INF/views/user/event-form.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the event form.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAllowed(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean adminMode = isAdminMode(request);
        Map<String, String> errors = new LinkedHashMap<>();
        Event event = EventFormUtil.readEvent(request, errors);

        try {
            if (!errors.containsKey("categoryId")) {
                Category category = categoryDAO.findById(event.getCategoryId());
                if (category == null) {
                    errors.put("categoryId", "Please select a valid event category.");
                }
            }

            File uploadDirectory = FileUploadUtil.getUploadDirectory(getServletContext());
            String savedImage = null;

            if (errors.isEmpty()) {
                Part imagePart = getImagePart(request, errors);
                if (errors.isEmpty()) {
                    try {
                        savedImage = FileUploadUtil.saveImage(imagePart, uploadDirectory, null);
                    } catch (IllegalArgumentException e) {
                        errors.put("eventImage", e.getMessage());
                    }
                }
            }

            if (errors.isEmpty()) {
                event.setEventImage(savedImage);
                if (adminMode) {
                    event.setUserId(null);
                } else {
                    User user = WebUtil.getLoggedUser(request);
                    event.setUserId(user.getUserId());
                }

                try {
                    eventDAO.create(event);
                } catch (SQLException e) {
                    FileUploadUtil.deleteImage(uploadDirectory, savedImage);
                    throw e;
                }

                WebUtil.setSuccessMessage(request.getSession(), "The event was created successfully.");
                response.sendRedirect(request.getContextPath()
                        + (adminMode ? "/admin/events" : "/user/events"));
                return;
            }

            prepareForm(request, event);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/views/user/event-form.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not save the event.", e);
        }
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
        request.setAttribute("formMode", "create");
        request.setAttribute("formAction", adminMode ? "/admin/events/new" : "/user/events/new");
        request.setAttribute("cancelUrl", adminMode ? "/admin/events" : "/user/events");
        request.setAttribute("adminMode", adminMode);
        request.setAttribute("pageTitle", "Create Event");
    }

    private boolean isAllowed(HttpServletRequest request) {
        if (isAdminMode(request)) {
            return WebUtil.getLoggedAdmin(request) != null;
        }
        return WebUtil.getLoggedUser(request) != null;
    }

    private boolean isAdminMode(HttpServletRequest request) {
        return request.getServletPath().startsWith("/admin");
    }
}
