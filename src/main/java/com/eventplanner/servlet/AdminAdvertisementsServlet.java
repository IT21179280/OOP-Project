package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.model.Advertisement;
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
 * Lets the administrator view and delete advertisements.
 */
@WebServlet("/admin/advertisements")
public class AdminAdvertisementsServlet extends HttpServlet {

    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = WebUtil.trim(request.getParameter("keyword"));

        try {
            if (keyword.isEmpty()) {
                request.setAttribute("advertisements", advertisementDAO.findAll());
            } else {
                request.setAttribute("advertisements", advertisementDAO.search(keyword));
            }
            request.setAttribute("keyword", keyword);
            request.setAttribute("pageTitle", "Manage Advertisements");
            request.getRequestDispatcher("/WEB-INF/views/admin/advertisements.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the advertisements.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"delete".equals(request.getParameter("action"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
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

            advertisementDAO.delete(advertisementId);
            File uploadDirectory = FileUploadUtil.getUploadDirectory(getServletContext());
            FileUploadUtil.deleteImage(uploadDirectory, advertisement.getImage());

            WebUtil.setSuccessMessage(request.getSession(), "The advertisement was deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/advertisements");

        } catch (SQLException e) {
            throw new ServletException("Could not delete the advertisement.", e);
        }
    }
}
