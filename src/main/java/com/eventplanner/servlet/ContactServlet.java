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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Displays the Contact Us page and saves messages in the database.
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private final ContactMessageDAO contactMessageDAO = new ContactMessageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Contact Us");
        request.getRequestDispatcher("/WEB-INF/views/public/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = WebUtil.trim(request.getParameter("name"));
        String email = WebUtil.trim(request.getParameter("email"));
        String subject = WebUtil.trim(request.getParameter("subject"));
        String messageText = WebUtil.trim(request.getParameter("message"));

        Map<String, String> errors = new LinkedHashMap<>();

        if (name.isEmpty()) {
            errors.put("name", "Please enter your name.");
        } else if (name.length() < 3 || name.length() > 100) {
            errors.put("name", "Name must be between 3 and 100 characters.");
        }

        if (email.isEmpty()) {
            errors.put("email", "Please enter your email address.");
        } else if (!WebUtil.isValidEmail(email) || email.length() > 100) {
            errors.put("email", "Please enter a valid email address.");
        }

        if (subject.isEmpty()) {
            errors.put("subject", "Please enter a subject.");
        } else if (subject.length() > 150) {
            errors.put("subject", "Subject cannot be longer than 150 characters.");
        }

        if (messageText.isEmpty()) {
            errors.put("message", "Please enter your message.");
        } else if (messageText.length() < 10) {
            errors.put("message", "Message should be at least 10 characters long.");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("name", name);
            request.setAttribute("senderEmail", email);
            request.setAttribute("subject", subject);
            request.setAttribute("message", messageText);
            request.setAttribute("pageTitle", "Contact Us");
            request.getRequestDispatcher("/WEB-INF/views/public/contact.jsp").forward(request, response);
            return;
        }

        try {
            ContactMessage contactMessage = new ContactMessage();
            contactMessage.setName(name);
            contactMessage.setEmail(email);
            contactMessage.setSubject(subject);
            contactMessage.setMessage(messageText);
            contactMessageDAO.create(contactMessage);

            WebUtil.setSuccessMessage(request.getSession(),
                    "Thank you for contacting us. We will reply to your email as soon as possible.");
            response.sendRedirect(request.getContextPath() + "/contact");

        } catch (SQLException e) {
            throw new ServletException("Could not save your contact message.", e);
        }
    }
}
