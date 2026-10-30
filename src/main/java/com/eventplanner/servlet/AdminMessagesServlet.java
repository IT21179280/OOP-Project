package com.eventplanner.servlet;

import com.eventplanner.dao.ContactMessageDAO;
import com.eventplanner.model.ContactMessage;
import com.eventplanner.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Lets the administrator read and delete the messages that visitors send
 * through the Contact Us page.
 */
@WebServlet("/admin/messages")
public class AdminMessagesServlet extends HttpServlet {

    private final ContactMessageDAO contactMessageDAO = new ContactMessageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute("messages", contactMessageDAO.findAll());
            request.setAttribute("pageTitle", "Contact Messages");
            request.getRequestDispatcher("/WEB-INF/views/admin/messages.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load the contact messages.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"delete".equals(request.getParameter("action"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action.");
            return;
        }

        Integer messageId = WebUtil.getIntParameter(request, "id");
        if (messageId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Message id is missing.");
            return;
        }

        try {
            ContactMessage message = contactMessageDAO.findById(messageId);
            if (message == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The message could not be found.");
                return;
            }

            if (contactMessageDAO.delete(messageId)) {
                WebUtil.setSuccessMessage(request.getSession(), "The contact message was deleted successfully.");
            } else {
                WebUtil.setErrorMessage(request.getSession(), "The contact message could not be deleted.");
            }
            response.sendRedirect(request.getContextPath() + "/admin/messages");

        } catch (SQLException e) {
            throw new ServletException("Could not delete the contact message.", e);
        }
    }
}
