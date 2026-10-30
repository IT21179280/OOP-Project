<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Event> events = (List<Event>) request.getAttribute("events");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>My Events</h1>
        <p>Create, edit or delete the events that you have added.</p>
    </div>
    <a class="btn btn-primary" href="<%= ctx %>/user/events/new">+ Create New Event</a>
</div>

<% if (events == null || events.isEmpty()) { %>
    <div class="empty-state">
        <h3>No events yet</h3>
        <p>You have not created any events. Click the button below to add your first event.</p>
        <a class="btn btn-primary" href="<%= ctx %>/user/events/new">Create New Event</a>
    </div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>#</th>
                <th>Event Title</th>
                <th>Category</th>
                <th>Date</th>
                <th>Time</th>
                <th>Location</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (Event event : events) { %>
                <tr>
                    <td><%= event.getEventId() %></td>
                    <td><a href="<%= ctx %>/events?id=<%= event.getEventId() %>"><%= WebUtil.escape(event.getEventTitle()) %></a></td>
                    <td><%= WebUtil.escape(event.getCategoryName()) %></td>
                    <td><%= WebUtil.formatDate(event.getEventDate()) %></td>
                    <td><%= WebUtil.formatTime(event.getStartTime()) %></td>
                    <td><%= WebUtil.escape(event.getLocation()) %></td>
                    <td>
                        <% if (WebUtil.isUpcoming(event.getEventDate())) { %>
                            <span class="status status-upcoming">Upcoming</span>
                        <% } else { %>
                            <span class="status status-past">Past</span>
                        <% } %>
                    </td>
                    <td class="action-cell">
                        <a class="action-link" href="<%= ctx %>/events?id=<%= event.getEventId() %>">View</a>
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

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
