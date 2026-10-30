<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Admin" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Event event = (Event) request.getAttribute("event");
    User detailsUser = (User) session.getAttribute("user");
    Admin detailsAdmin = (Admin) session.getAttribute("admin");

    boolean adminMode = detailsAdmin != null;
    boolean owner = detailsUser != null
            && event.getUserId() != null
            && event.getUserId().intValue() == detailsUser.getUserId();
    boolean canManage = adminMode || owner;
    String manageBase = adminMode ? ctx + "/admin/events" : ctx + "/user/events";
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<p class="breadcrumb"><a href="<%= ctx %>/events">&laquo; Back to Events</a></p>

<div class="details-header">
    <div>
        <span class="category-tag"><%= WebUtil.escape(event.getCategoryName()) %></span>
        <% if (WebUtil.isUpcoming(event.getEventDate())) { %>
            <span class="status status-upcoming">Upcoming Event</span>
        <% } else { %>
            <span class="status status-past">Past Event</span>
        <% } %>
        <h1><%= WebUtil.escape(event.getEventTitle()) %></h1>
        <p class="details-subtitle">
            Organized by
            <%= event.getUserId() == null ? "System Administrator"
                    : WebUtil.display(event.getOrganizerName()) %>
        </p>
    </div>
</div>

<div class="details-layout">
    <div class="details-image">
        <img src="<%= WebUtil.eventImageUrl(ctx, event.getEventImage()) %>"
             alt="<%= WebUtil.escape(event.getEventTitle()) %>">
    </div>

    <div class="details-info">
        <h2>Event Information</h2>
        <table class="details-table">
            <tr>
                <th>Event Date</th>
                <td><%= WebUtil.formatDate(event.getEventDate()) %></td>
            </tr>
            <tr>
                <th>Time</th>
                <td><%= WebUtil.formatTime(event.getStartTime()) %> - <%= WebUtil.formatTime(event.getEndTime()) %></td>
            </tr>
            <tr>
                <th>Location</th>
                <td><%= WebUtil.escape(event.getLocation()) %></td>
            </tr>
            <tr>
                <th>Category</th>
                <td><%= WebUtil.escape(event.getCategoryName()) %></td>
            </tr>
            <tr>
                <th>Contact</th>
                <td><%= WebUtil.escape(event.getContactInformation()) %></td>
            </tr>
            <tr>
                <th>Added On</th>
                <td><%= WebUtil.formatDateTime(event.getCreatedAt()) %></td>
            </tr>
        </table>

        <% if (canManage) { %>
            <div class="details-actions">
                <a class="btn btn-primary" href="<%= manageBase %>/edit?id=<%= event.getEventId() %>">Edit Event</a>
                <form method="post" action="<%= manageBase %>/delete" class="inline-form confirm-delete"
                      data-confirm="Are you sure you want to delete this event?">
                    <input type="hidden" name="id" value="<%= event.getEventId() %>">
                    <button type="submit" class="btn btn-danger">Delete Event</button>
                </form>
            </div>
        <% } %>
    </div>
</div>

<div class="details-description">
    <h2>About This Event</h2>
    <p><%= WebUtil.escape(event.getDescription()).replace("\n", "<br>") %></p>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
