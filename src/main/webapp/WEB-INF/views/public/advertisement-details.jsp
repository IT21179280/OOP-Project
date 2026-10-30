<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.model.Advertisement" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Admin" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Advertisement advertisement = (Advertisement) request.getAttribute("advertisement");
    User adUser = (User) session.getAttribute("user");
    Admin adAdmin = (Admin) session.getAttribute("admin");

    boolean owner = adUser != null && advertisement.getUserId() == adUser.getUserId();
    boolean isUpcoming = WebUtil.isUpcoming(advertisement.getEventDate());
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<p class="breadcrumb"><a href="<%= ctx %>/advertisements">&laquo; Back to Advertisements</a></p>

<div class="details-header">
    <div>
        <span class="category-tag">Advertisement</span>
        <% if (isUpcoming) { %>
            <span class="status status-upcoming">Upcoming Event</span>
        <% } else { %>
            <span class="status status-past">Past Event</span>
        <% } %>
        <h1><%= WebUtil.escape(advertisement.getTitle()) %></h1>
        <p class="details-subtitle">
            Published by <%= WebUtil.display(advertisement.getOrganizerName()) %>
            on <%= WebUtil.formatDateTime(advertisement.getCreatedAt()) %>
        </p>
    </div>
</div>

<div class="details-layout">
    <div class="details-image">
        <img src="<%= WebUtil.advertisementImageUrl(ctx, advertisement.getImage()) %>"
             alt="<%= WebUtil.escape(advertisement.getTitle()) %>">
    </div>

    <div class="details-info">
        <h2>Advertisement Information</h2>
        <table class="details-table">
            <tr>
                <th>Event Name</th>
                <td><%= WebUtil.escape(advertisement.getEventName()) %></td>
            </tr>
            <tr>
                <th>Event Date</th>
                <td><%= WebUtil.formatDate(advertisement.getEventDate()) %></td>
            </tr>
            <tr>
                <th>Location</th>
                <td><%= WebUtil.escape(advertisement.getLocation()) %></td>
            </tr>
            <tr>
                <th>Contact Number</th>
                <td><%= WebUtil.escape(advertisement.getContactNumber()) %></td>
            </tr>
            <tr>
                <th>Email</th>
                <td><a href="mailto:<%= WebUtil.escape(advertisement.getEmail()) %>"><%= WebUtil.escape(advertisement.getEmail()) %></a></td>
            </tr>
            <% if (advertisement.getEventId() != null) { %>
                <tr>
                    <th>Related Event</th>
                    <td><a href="<%= ctx %>/events?id=<%= advertisement.getEventId() %>">View event details</a></td>
                </tr>
            <% } %>
        </table>

        <% if (owner) { %>
            <div class="details-actions">
                <a class="btn btn-primary"
                   href="<%= ctx %>/user/advertisements/edit?id=<%= advertisement.getAdvertisementId() %>">Edit Advertisement</a>
                <form method="post" action="<%= ctx %>/user/advertisements/delete" class="inline-form confirm-delete"
                      data-confirm="Are you sure you want to delete this advertisement?">
                    <input type="hidden" name="id" value="<%= advertisement.getAdvertisementId() %>">
                    <button type="submit" class="btn btn-danger">Delete Advertisement</button>
                </form>
            </div>
        <% } else if (adAdmin != null) { %>
            <p class="form-hint">
                Advertisements can be deleted from the
                <a href="<%= ctx %>/admin/advertisements">administrator advertisement page</a>.
            </p>
        <% } %>
    </div>
</div>

<div class="details-description">
    <h2>Advertisement Description</h2>
    <p><%= WebUtil.escape(advertisement.getDescription()).replace("\n", "<br>") %></p>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
