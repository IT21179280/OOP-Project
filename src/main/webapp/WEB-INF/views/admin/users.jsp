<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<User> users = (List<User>) request.getAttribute("users");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Registered Users</h1>
        <p>All user accounts registered in the system.</p>
    </div>
</div>

<div class="result-info"><strong><%= users == null ? 0 : users.size() %></strong> registered user(s)</div>

<% if (users == null || users.isEmpty()) { %>
    <div class="empty-state"><p>No users have registered yet.</p></div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>User ID</th>
                <th>Full Name</th>
                <th>Email Address</th>
                <th>Username</th>
                <th>Contact Number</th>
                <th>Role</th>
                <th>Registered On</th>
            </tr>
        </thead>
        <tbody>
            <% for (User user : users) { %>
                <tr>
                    <td><%= user.getUserId() %></td>
                    <td><%= WebUtil.escape(user.getFullName()) %></td>
                    <td><%= WebUtil.escape(user.getEmail()) %></td>
                    <td><%= WebUtil.escape(user.getUsername()) %></td>
                    <td><%= WebUtil.escape(user.getContactNumber()) %></td>
                    <td><%= WebUtil.escape(user.getRole()) %></td>
                    <td><%= WebUtil.formatDateTime(user.getCreatedAt()) %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
