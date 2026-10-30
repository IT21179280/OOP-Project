package com.eventplanner.servlet;

import com.eventplanner.dao.AdvertisementDAO;
import com.eventplanner.model.Advertisement;
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
 * Shows all published advertisements and the details of one advertisement.
 */
@WebServlet("/advertisements")
public class AdvertisementServlet extends HttpServlet {

    private final AdvertisementDAO advertisementDAO = new AdvertisementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        try {
            if (!WebUtil.isBlank(idParameter)) {
                showAdvertisementDetails(request, response);
            } else {
                showAdvertisementList(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Could not load advertisements from the database.", e);
        }
    }

    private void showAdvertisementDetails(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Integer advertisementId = WebUtil.getIntParameter(request, "id");
        if (advertisementId == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Advertisement not found.");
            return;
        }

        Advertisement advertisement = advertisementDAO.findById(advertisementId);
        if (advertisement == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested advertisement could not be found.");
            return;
        }

        request.setAttribute("advertisement", advertisement);
        request.setAttribute("pageTitle", "Advertisement Details");
        request.getRequestDispatcher("/WEB-INF/views/public/advertisement-details.jsp").forward(request, response);
    }

    private void showAdvertisementList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String keyword = WebUtil.trim(request.getParameter("keyword"));
        List<Advertisement> advertisements;

        if (keyword.isEmpty()) {
            advertisements = advertisementDAO.findAll();
        } else {
            advertisements = advertisementDAO.search(keyword);
        }

        request.setAttribute("advertisements", advertisements);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageTitle", "Advertisements");
        request.getRequestDispatcher("/WEB-INF/views/public/advertisements.jsp").forward(request, response);
    }
}
