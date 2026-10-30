<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String name = (String) request.getAttribute("name");
    String senderEmail = (String) request.getAttribute("senderEmail");
    String subject = (String) request.getAttribute("subject");
    String message = (String) request.getAttribute("message");
    if (name == null) { name = ""; }
    if (senderEmail == null) { senderEmail = ""; }
    if (subject == null) { subject = ""; }
    if (message == null) { message = ""; }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>Contact Us</h1>
        <p>If you have a question or a suggestion, please send us a message.</p>
    </div>
</div>

<div class="contact-layout">
    <div class="contact-info-panel">
        <h2>Our Contact Details</h2>
        <table class="details-table">
            <tr>
                <th>Address</th>
                <td>No. 25, Temple Road,<br>Colombo 03, Sri Lanka.</td>
            </tr>
            <tr>
                <th>Phone</th>
                <td>+94 11 234 5678</td>
            </tr>
            <tr>
                <th>Email</th>
                <td>info@eventplanner.lk</td>
            </tr>
            <tr>
                <th>Office Hours</th>
                <td>Monday - Friday<br>9.00 a.m. - 5.00 p.m.</td>
            </tr>
        </table>
        <p class="form-hint">
            We usually reply to messages within one or two working days.
        </p>
    </div>

    <div class="contact-form-panel">
        <h2>Send Us a Message</h2>

        <% if (errors != null && !errors.isEmpty()) { %>
            <div class="alert alert-error">Please correct the highlighted fields and try again.</div>
        <% } %>

        <form method="post" action="<%= ctx %>/contact" id="contactForm">
            <div class="form-row">
                <label for="name">Your Name <span class="required">*</span></label>
                <input type="text" id="name" name="name" value="<%= WebUtil.escape(name) %>"
                       maxlength="100" required>
                <% if (errors != null && errors.get("name") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("name")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="email">Email Address <span class="required">*</span></label>
                <input type="email" id="email" name="email" value="<%= WebUtil.escape(senderEmail) %>"
                       maxlength="100" required>
                <% if (errors != null && errors.get("email") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("email")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="subject">Subject <span class="required">*</span></label>
                <input type="text" id="subject" name="subject" value="<%= WebUtil.escape(subject) %>"
                       maxlength="150" required>
                <% if (errors != null && errors.get("subject") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("subject")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="message">Message <span class="required">*</span></label>
                <textarea id="message" name="message" rows="7" maxlength="2000" required><%= WebUtil.escape(message) %></textarea>
                <% if (errors != null && errors.get("message") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("message")) %></span>
                <% } %>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Submit Message</button>
                <button type="reset" class="btn btn-secondary">Clear Form</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
