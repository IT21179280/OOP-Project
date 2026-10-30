<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Advertisement" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Advertisement> advertisements = (List<Advertisement>) request.getAttribute("advertisements");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>My Advertisements</h1>
        <p>Publish and manage advertisements for your events.</p>
    </div>
    <a class="btn btn-primary" href="<%= ctx %>/user/advertisements/new">+ Create New Advertisement</a>
</div>

<% if (advertisements == null || advertisements.isEmpty()) { %>
    <div class="empty-state">
        <h3>No advertisements yet</h3>
        <p>You have not published any advertisements. Create one to promote your event.</p>
        <a class="btn btn-primary" href="<%= ctx %>/user/advertisements/new">Create New Advertisement</a>
    </div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>#</th>
                <th>Advertisement Title</th>
                <th>Event Name</th>
                <th>Event Date</th>
                <th>Location</th>
                <th>Published On</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (Advertisement advertisement : advertisements) { %>
                <tr>
                    <td><%= advertisement.getAdvertisementId() %></td>
                    <td>
                        <a href="<%= ctx %>/advertisements?id=<%= advertisement.getAdvertisementId() %>">
                            <%= WebUtil.escape(advertisement.getTitle()) %>
                        </a>
                    </td>
                    <td><%= WebUtil.escape(advertisement.getEventName()) %></td>
                    <td><%= WebUtil.formatDate(advertisement.getEventDate()) %></td>
                    <td><%= WebUtil.escape(advertisement.getLocation()) %></td>
                    <td><%= WebUtil.formatDate(new java.sql.Date(advertisement.getCreatedAt().getTime())) %></td>
                    <td class="action-cell">
                        <a class="action-link"
                           href="<%= ctx %>/advertisements?id=<%= advertisement.getAdvertisementId() %>">View</a>
                        <a class="action-link"
                           href="<%= ctx %>/user/advertisements/edit?id=<%= advertisement.getAdvertisementId() %>">Edit</a>
                        <form method="post" action="<%= ctx %>/user/advertisements/delete"
                              class="inline-form confirm-delete"
                              data-confirm="Are you sure you want to delete this advertisement?">
                            <input type="hidden" name="id" value="<%= advertisement.getAdvertisementId() %>">
                            <button type="submit" class="link-button danger-link">Delete</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
