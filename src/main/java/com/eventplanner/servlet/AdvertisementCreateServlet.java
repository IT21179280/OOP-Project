package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.dao.EventDAO;
import com.eventplanner.model.Advertisement;
import com.eventplanner.model.Event;
import com.eventplanner.model.User;
import com.eventplanner.util.AdvertisementFormUtil;
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
 * Creates a new event advertisement for the logged in user.
 */
@WebServlet("/user/advertisements/new")
@MultipartConfig(maxFileSize = 6 * 1024 * 1024, maxRequestSize = 8 * 1024 * 1024)
public class AdvertisementCreateServlet extends HttpServlet {

    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();
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
            prepareForm(request, user, new Advertisement());
            request.getRequestDispatcher("/WEB-INF/views/user/advertisement-form.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the advertisement form.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = WebUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Map<String, String> errors = new LinkedHashMap<>();
        Advertisement advertisement = AdvertisementFormUtil.readAdvertisement(request, errors);
        advertisement.setUserId(user.getUserId());

        try {
            if (advertisement.getEventId() != null) {
                Event relatedEvent = eventDAO.findById(advertisement.getEventId());
                if (relatedEvent == null
                        || relatedEvent.getUserId() == null
                        || relatedEvent.getUserId() != user.getUserId()) {
                    advertisement.setEventId(null);
                    errors.put("eventId", "The selected event does not belong to you.");
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
                        errors.put("image", e.getMessage());
                    }
                }
            }

            if (errors.isEmpty()) {
                advertisement.setImage(savedImage);
                try {
                    advertisementDAO.create(advertisement);
                } catch (SQLException e) {
                    FileUploadUtil.deleteImage(uploadDirectory, savedImage);
                    throw e;
                }

                WebUtil.setSuccessMessage(request.getSession(), "The advertisement was published successfully.");
                response.sendRedirect(request.getContextPath() + "/user/advertisements");
                return;
            }

            prepareForm(request, user, advertisement);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/views/user/advertisement-form.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not save the advertisement.", e);
        }
    }

    private Part getImagePart(HttpServletRequest request, Map<String, String> errors) {
        try {
            return request.getPart("image");
        } catch (IllegalStateException e) {
            errors.put("image", "The image is too large. Please select an image smaller than 5 MB.");
        } catch (IOException | ServletException e) {
            errors.put("image", "The image could not be uploaded. Please try again.");
        }
        return null;
    }

    private void prepareForm(HttpServletRequest request, User user, Advertisement advertisement)
            throws SQLException {
        request.setAttribute("advertisement", advertisement);
        request.setAttribute("myEvents", eventDAO.findByUser(user.getUserId()));
        request.setAttribute("formMode", "create");
        request.setAttribute("formAction", "/user/advertisements/new");
        request.setAttribute("cancelUrl", "/user/advertisements");
        request.setAttribute("pageTitle", "Create Advertisement");
    }
}
