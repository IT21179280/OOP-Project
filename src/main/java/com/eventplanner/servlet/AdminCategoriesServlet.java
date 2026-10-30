package com.eventplanner.servlet;

import com.eventplanner.dao.CategoryDAO;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Lets the administrator add, rename and delete event categories.
 */
@WebServlet("/admin/categories")
public class AdminCategoriesServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("categories", categoryDAO.findAll());
            request.setAttribute("pageTitle", "Manage Categories");
            request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the event categories.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = WebUtil.trim(request.getParameter("action"));
        String categoryName = WebUtil.trim(request.getParameter("categoryName"));

        try {
            if ("add".equals(action)) {
                if (categoryName.isEmpty()) {
                    WebUtil.setErrorMessage(request.getSession(), "Category name is required.");
                } else if (categoryName.length() > 50) {
                    WebUtil.setErrorMessage(request.getSession(), "Category name cannot be longer than 50 characters.");
                } else if (categoryDAO.isNameTaken(categoryName, null)) {
                    WebUtil.setErrorMessage(request.getSession(), "A category with that name already exists.");
                } else {
                    categoryDAO.create(categoryName);
                    WebUtil.setSuccessMessage(request.getSession(), "The category was added successfully.");
                }

            } else if ("update".equals(action)) {
                Integer categoryId = WebUtil.getIntParameter(request, "categoryId");
                if (categoryId == null) {
                    WebUtil.setErrorMessage(request.getSession(), "Category id is missing.");
                } else if (categoryName.isEmpty()) {
                    WebUtil.setErrorMessage(request.getSession(), "Category name is required.");
                } else if (categoryName.length() > 50) {
                    WebUtil.setErrorMessage(request.getSession(), "Category name cannot be longer than 50 characters.");
                } else if (categoryDAO.isNameTaken(categoryName, categoryId)) {
                    WebUtil.setErrorMessage(request.getSession(), "Another category already uses that name.");
                } else if (categoryDAO.update(categoryId, categoryName)) {
                    WebUtil.setSuccessMessage(request.getSession(), "The category was updated successfully.");
                } else {
                    WebUtil.setErrorMessage(request.getSession(), "The category could not be found.");
                }

            } else if ("delete".equals(action)) {
                Integer categoryId = WebUtil.getIntParameter(request, "categoryId");
                if (categoryId == null) {
                    WebUtil.setErrorMessage(request.getSession(), "Category id is missing.");
                } else {
                    int eventCount = categoryDAO.countEvents(categoryId);
                    if (eventCount > 0) {
                        WebUtil.setErrorMessage(request.getSession(),
                                "This category cannot be deleted because " + eventCount
                                        + " event(s) are using it.");
                    } else if (categoryDAO.delete(categoryId)) {
                        WebUtil.setSuccessMessage(request.getSession(), "The category was deleted successfully.");
                    } else {
                        WebUtil.setErrorMessage(request.getSession(), "The category could not be found.");
                    }
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/categories");

        } catch (SQLException e) {
            throw new ServletException("Could not update the event categories.", e);
        }
    }
}
