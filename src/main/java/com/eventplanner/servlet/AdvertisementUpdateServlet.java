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
import java.util.Objects;

/**
 * Edits an advertisement. Users may only edit their own advertisements.
 */
@WebServlet("/user/advertisements/edit")
@MultipartConfig(maxFileSize = 6 * 1024 * 1024, maxRequestSize = 8 * 1024 * 1024)
public class AdvertisementUpdateServlet extends HttpServlet {

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
            Advertisement advertisement = loadAdvertisement(request, response);
            if (advertisement == null) {
                return;
            }
            if (advertisement.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "You are not allowed to edit this advertisement.");
                return;
            }
            prepareForm(request, user, advertisement);
            request.getRequestDispatcher("/WEB-INF/views/user/advertisement-form.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the advertisement.", e);
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

        try {
            Integer advertisementId = WebUtil.getIntParameter(request, "advertisementId");
            if (advertisementId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Advertisement id is missing.");
                return;
            }

            Advertisement existing = advertisementDAO.findById(advertisementId);
            if (existing == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The advertisement could not be found.");
                return;
            }
            if (existing.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "You are not allowed to edit this advertisement.");
                return;
            }

            Map<String, String> errors = new LinkedHashMap<>();
            Advertisement advertisement = AdvertisementFormUtil.readAdvertisement(request, errors);
            advertisement.setAdvertisementId(existing.getAdvertisementId());
            advertisement.setUserId(existing.getUserId());
            advertisement.setImage(existing.getImage());

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
            String newImage = existing.getImage();

            if (errors.isEmpty()) {
                Part imagePart = getImagePart(request, errors);
                if (errors.isEmpty()) {
                    try {
                        newImage = FileUploadUtil.saveImage(imagePart, uploadDirectory, existing.getImage());
                        advertisement.setImage(newImage);
                    } catch (IllegalArgumentException e) {
                        errors.put("image", e.getMessage());
                    }
                }
            }

            if (errors.isEmpty()) {
                try {
                    advertisementDAO.update(advertisement);
                } catch (SQLException e) {
                    if (!Objects.equals(newImage, existing.getImage())) {
                        FileUploadUtil.deleteImage(uploadDirectory, newImage);
                    }
                    throw e;
                }

                if (!Objects.equals(newImage, existing.getImage())) {
                    FileUploadUtil.deleteImage(uploadDirectory, existing.getImage());
                }

                WebUtil.setSuccessMessage(request.getSession(), "The advertisement was updated successfully.");
                response.sendRedirect(request.getContextPath() + "/user/advertisements");
                return;
            }

            prepareForm(request, user, advertisement);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/views/user/advertisement-form.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not update the advertisement.", e);
        }
    }

    private Advertisement loadAdvertisement(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Integer advertisementId = WebUtil.getIntParameter(request, "id");
        if (advertisementId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Advertisement id is missing.");
            return null;
        }
        Advertisement advertisement = advertisementDAO.findById(advertisementId);
        if (advertisement == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The advertisement could not be found.");
        }
        return advertisement;
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
        request.setAttribute("formMode", "edit");
        request.setAttribute("formAction", "/user/advertisements/edit");
        request.setAttribute("cancelUrl", "/user/advertisements");
        request.setAttribute("pageTitle", "Edit Advertisement");
    }
}
