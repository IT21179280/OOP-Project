<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.eventplanner.model.ContactMessage" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<ContactMessage> messages = (List<ContactMessage>) request.getAttribute("messages");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Contact Messages</h1>
        <p>Read and remove the messages that visitors send through the Contact Us page.</p>
    </div>
</div>

<div class="result-info"><strong><%= messages == null ? 0 : messages.size() %></strong> contact message(s)</div>

<% if (messages == null || messages.isEmpty()) { %>
    <div class="empty-state">
        <h3>No messages yet</h3>
        <p>Messages submitted through the Contact Us form will appear here.</p>
        <a class="btn btn-secondary" href="<%= ctx %>/contact">Open Contact Page</a>
    </div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Sender</th>
                <th>Subject</th>
                <th>Message</th>
                <th>Received On</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (ContactMessage message : messages) { %>
                <tr>
                    <td><%= message.getMessageId() %></td>
                    <td>
                        <strong><%= WebUtil.escape(message.getName()) %></strong><br>
                        <a href="mailto:<%= WebUtil.escape(message.getEmail()) %>"><%= WebUtil.escape(message.getEmail()) %></a>
                    </td>
                    <td><%= WebUtil.escape(message.getSubject()) %></td>
                    <td class="message-text"><%= WebUtil.escape(message.getMessage()) %></td>
                    <td><%= WebUtil.formatDateTime(message.getCreatedAt()) %></td>
                    <td class="action-cell">
                        <a class="action-link"
                           href="mailto:<%= WebUtil.escape(message.getEmail()) %>?subject=Re%3A%20<%= URLEncoder.encode(message.getSubject(), "UTF-8") %>">Reply</a>
                        <form method="post" action="<%= ctx %>/admin/messages" class="inline-form confirm-delete"
                              data-confirm="Are you sure you want to delete this contact message?">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="<%= message.getMessageId() %>">
                            <button type="submit" class="link-button danger-link">Delete</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
    <p class="form-hint">Use the Reply link to open your email program with the sender's address already filled in.</p>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
