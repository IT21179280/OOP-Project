<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Event> events = (List<Event>) request.getAttribute("events");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) { keyword = ""; }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Manage Events</h1>
        <p>View, add, edit and delete events in the system.</p>
    </div>
    <a class="btn btn-primary" href="<%= ctx %>/admin/events/new">+ Add New Event</a>
</div>

<form class="filter-form" method="get" action="<%= ctx %>/admin/events">
    <input type="text" name="keyword" value="<%= WebUtil.escape(keyword) %>"
           placeholder="Search events by title, description or location" maxlength="100">
    <button type="submit" class="btn btn-primary">Search</button>
    <a class="btn btn-secondary" href="<%= ctx %>/admin/events">Reset</a>
</form>

<div class="result-info"><strong><%= events == null ? 0 : events.size() %></strong> event(s) found</div>

<% if (events == null || events.isEmpty()) { %>
    <div class="empty-state">
        <p>No events were found.</p>
        <a class="btn btn-primary" href="<%= ctx %>/admin/events/new">Add New Event</a>
    </div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Event Title</th>
                <th>Category</th>
                <th>Date</th>
                <th>Time</th>
                <th>Location</th>
                <th>Added By</th>
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
                    <td><%= event.getUserId() == null ? "Administrator" : WebUtil.display(event.getOrganizerName()) %></td>
                    <td class="action-cell">
                        <a class="action-link" href="<%= ctx %>/events?id=<%= event.getEventId() %>">View</a>
                        <a class="action-link" href="<%= ctx %>/admin/events/edit?id=<%= event.getEventId() %>">Edit</a>
                        <form method="post" action="<%= ctx %>/admin/events/delete" class="inline-form confirm-delete"
                              data-confirm="Are you sure you want to delete this event? Related advertisements will keep their event name.">
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
