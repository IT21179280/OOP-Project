package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.model.Advertisement;
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
 * Deletes an advertisement. Only the owner can delete it and the request
 * must be sent with POST.
 */
@WebServlet("/user/advertisements/delete")
public class AdvertisementDeleteServlet extends HttpServlet {

    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = WebUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer advertisementId = WebUtil.getIntParameter(request, "id");
        if (advertisementId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Advertisement id is missing.");
            return;
        }

        try {
            Advertisement advertisement = advertisementDAO.findById(advertisementId);
            if (advertisement == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The advertisement could not be found.");
                return;
            }
            if (advertisement.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "You are not allowed to delete this advertisement.");
                return;
            }

            advertisementDAO.delete(advertisementId);
            File uploadDirectory = FileUploadUtil.getUploadDirectory(getServletContext());
            FileUploadUtil.deleteImage(uploadDirectory, advertisement.getImage());

            WebUtil.setSuccessMessage(request.getSession(), "The advertisement was deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/user/advertisements");

        } catch (SQLException e) {
            throw new ServletException("Could not delete the advertisement.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Advertisements can only be deleted using the delete button.");
    }
}
