<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    User dashboardUser = (User) session.getAttribute("user");
    Integer myEventCount = (Integer) request.getAttribute("myEventCount");
    Integer myAdvertisementCount = (Integer) request.getAttribute("myAdvertisementCount");
    Integer upcomingEventCount = (Integer) request.getAttribute("upcomingEventCount");
    List<Event> recentEvents = (List<Event>) request.getAttribute("recentEvents");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>My Dashboard</h1>
        <p>Welcome back, <strong><%= WebUtil.escape(dashboardUser.getFullName()) %></strong>!</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-primary" href="<%= ctx %>/user/events/new">+ Create Event</a>
        <a class="btn btn-secondary" href="<%= ctx %>/user/advertisements/new">+ Create Advertisement</a>
    </div>
</div>

<div class="stat-grid">
    <div class="stat-box">
        <span class="stat-number"><%= myEventCount == null ? 0 : myEventCount %></span>
        <span class="stat-label">My Events</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= myAdvertisementCount == null ? 0 : myAdvertisementCount %></span>
        <span class="stat-label">My Advertisements</span>
    </div>
    <div class="stat-box">
        <span class="stat-number"><%= upcomingEventCount == null ? 0 : upcomingEventCount %></span>
        <span class="stat-label">Upcoming Events (All Users)</span>
    </div>
</div>

<div class="dashboard-columns">
    <div class="dashboard-panel">
        <h2>My Personal Information</h2>
        <table class="details-table">
            <tr>
                <th>Full Name</th>
                <td><%= WebUtil.escape(dashboardUser.getFullName()) %></td>
            </tr>
            <tr>
                <th>Username</th>
                <td><%= WebUtil.escape(dashboardUser.getUsername()) %></td>
            </tr>
            <tr>
                <th>Email Address</th>
                <td><%= WebUtil.escape(dashboardUser.getEmail()) %></td>
            </tr>
            <tr>
                <th>Contact Number</th>
                <td><%= WebUtil.escape(dashboardUser.getContactNumber()) %></td>
            </tr>
            <tr>
                <th>Member Since</th>
                <td><%= WebUtil.formatDateTime(dashboardUser.getCreatedAt()) %></td>
            </tr>
        </table>
    </div>

    <div class="dashboard-panel">
        <h2>Quick Links</h2>
        <ul class="quick-links">
            <li><a href="<%= ctx %>/user/events">Manage My Events</a></li>
            <li><a href="<%= ctx %>/user/advertisements">Manage My Advertisements</a></li>
            <li><a href="<%= ctx %>/events">View All Available Events</a></li>
            <li><a href="<%= ctx %>/advertisements">View Published Advertisements</a></li>
            <li><a href="<%= ctx %>/contact">Contact the Administrator</a></li>
            <li><a href="<%= ctx %>/logout">Logout</a></li>
        </ul>
    </div>
</div>

<div class="dashboard-panel">
    <div class="section-heading">
        <h2>My Recent Events</h2>
        <a href="<%= ctx %>/user/events">View All My Events &raquo;</a>
    </div>

    <% if (recentEvents == null || recentEvents.isEmpty()) { %>
        <div class="empty-state">
            <p>You have not created any events yet.</p>
            <a class="btn btn-primary" href="<%= ctx %>/user/events/new">Create Your First Event</a>
        </div>
    <% } else { %>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Event Title</th>
                    <th>Category</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Event event : recentEvents) { %>
                    <tr>
                        <td><a href="<%= ctx %>/events?id=<%= event.getEventId() %>"><%= WebUtil.escape(event.getEventTitle()) %></a></td>
                        <td><%= WebUtil.escape(event.getCategoryName()) %></td>
                        <td><%= WebUtil.formatDate(event.getEventDate()) %></td>
                        <td><%= WebUtil.escape(event.getLocation()) %></td>
                        <td>
                            <% if (WebUtil.isUpcoming(event.getEventDate())) { %>
                                <span class="status status-upcoming">Upcoming</span>
                            <% } else { %>
                                <span class="status status-past">Past</span>
                            <% } %>
                        </td>
                        <td class="action-cell">
                            <a class="action-link" href="<%= ctx %>/user/events/edit?id=<%= event.getEventId() %>">Edit</a>
                            <form method="post" action="<%= ctx %>/user/events/delete" class="inline-form confirm-delete"
                                  data-confirm="Are you sure you want to delete this event?">
                                <input type="hidden" name="id" value="<%= event.getEventId() %>">
                                <button type="submit" class="link-button danger-link">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
