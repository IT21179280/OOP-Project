<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Integer totalEvents = (Integer) request.getAttribute("totalEvents");
    Integer upcomingEvents = (Integer) request.getAttribute("upcomingEvents");
    Integer totalAdvertisements = (Integer) request.getAttribute("totalAdvertisements");
    Integer totalCategories = (Integer) request.getAttribute("totalCategories");
    Integer totalMessages = (Integer) request.getAttribute("totalMessages");
    List<User> recentUsers = (List<User>) request.getAttribute("recentUsers");
    List<Event> recentEvents = (List<Event>) request.getAttribute("recentEvents");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Administrator Dashboard</h1>
        <p>A summary of the activity in the Online Event Planning System.</p>
    </div>
    <a class="btn btn-primary" href="<%= ctx %>/admin/events/new">+ Add New Event</a>
</div>

<div class="stat-grid stat-grid-admin">
    <div class="stat-box">
        <span class="stat-number"><%= totalUsers == null ? 0 : totalUsers %></span>
        <span class="stat-label">Registered Users</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= totalEvents == null ? 0 : totalEvents %></span>
        <span class="stat-label">Total Events</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= upcomingEvents == null ? 0 : upcomingEvents %></span>
        <span class="stat-label">Upcoming Events</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= totalAdvertisements == null ? 0 : totalAdvertisements %></span>
        <span class="stat-label">Advertisements</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= totalCategories == null ? 0 : totalCategories %></span>
        <span class="stat-label">Event Categories</span>
    </div>
    <a class="stat-box" href="<%= ctx %>/admin/messages">
        <span class="stat-number"><%= totalMessages == null ? 0 : totalMessages %></span>
        <span class="stat-label">Contact Messages</span>
    </a>
</div>

<div class="dashboard-columns">
    <div class="dashboard-panel">
        <div class="section-heading">
            <h2>Recently Registered Users</h2>
            <a href="<%= ctx %>/admin/users">View All Users &raquo;</a>
        </div>
        <% if (recentUsers == null || recentUsers.isEmpty()) { %>
            <div class="empty-state"><p>No users have registered yet.</p></div>
        <% } else { %>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Full Name</th>
                        <th>Username</th>
                        <th>Registered On</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (User user : recentUsers) { %>
                        <tr>
                            <td><%= WebUtil.escape(user.getFullName()) %></td>
                            <td><%= WebUtil.escape(user.getUsername()) %></td>
                            <td><%= WebUtil.formatDate(new java.sql.Date(user.getCreatedAt().getTime())) %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>

    <div class="dashboard-panel">
        <h2>Administrator Quick Links</h2>
        <ul class="quick-links">
            <li><a href="<%= ctx %>/admin/users">View Registered Users</a></li>
            <li><a href="<%= ctx %>/admin/events">Manage Events</a></li>
            <li><a href="<%= ctx %>/admin/advertisements">Manage Advertisements</a></li>
            <li><a href="<%= ctx %>/admin/categories">Manage Event Categories</a></li>
            <li><a href="<%= ctx %>/admin/messages">Read Contact Messages</a></li>
            <li><a href="<%= ctx %>/home">Open Public Website</a></li>
            <li><a href="<%= ctx %>/admin/logout">Logout</a></li>
        </ul>
    </div>
</div>

<div class="dashboard-panel">
    <div class="section-heading">
        <h2>Recently Added Events</h2>
        <a href="<%= ctx %>/admin/events">Manage All Events &raquo;</a>
    </div>

    <% if (recentEvents == null || recentEvents.isEmpty()) { %>
        <div class="empty-state"><p>No events have been added yet.</p></div>
    <% } else { %>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Event Title</th>
                    <th>Category</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Added By</th>
                    <th>View</th>
                </tr>
            </thead>
            <tbody>
                <% for (Event event : recentEvents) { %>
                    <tr>
                        <td><%= WebUtil.escape(event.getEventTitle()) %></td>
                        <td><%= WebUtil.escape(event.getCategoryName()) %></td>
                        <td><%= WebUtil.formatDate(event.getEventDate()) %></td>
                        <td><%= WebUtil.escape(event.getLocation()) %></td>
                        <td><%= event.getUserId() == null ? "Administrator" : WebUtil.display(event.getOrganizerName()) %></td>
                        <td><a class="action-link" href="<%= ctx %>/events?id=<%= event.getEventId() %>">View</a></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
