<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Advertisement" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Advertisement> advertisements = (List<Advertisement>) request.getAttribute("advertisements");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) { keyword = ""; }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Manage Advertisements</h1>
        <p>View and delete advertisements published by registered users.</p>
    </div>
</div>

<form class="filter-form" method="get" action="<%= ctx %>/admin/advertisements">
    <input type="text" name="keyword" value="<%= WebUtil.escape(keyword) %>"
           placeholder="Search advertisements by title, event name or location" maxlength="100">
    <button type="submit" class="btn btn-primary">Search</button>
    <a class="btn btn-secondary" href="<%= ctx %>/admin/advertisements">Reset</a>
</form>

<div class="result-info">
    <strong><%= advertisements == null ? 0 : advertisements.size() %></strong> advertisement(s) found
</div>

<% if (advertisements == null || advertisements.isEmpty()) { %>
    <div class="empty-state"><p>No advertisements were found.</p></div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Event Name</th>
                <th>Event Date</th>
                <th>Location</th>
                <th>Published By</th>
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
                    <td><%= WebUtil.display(advertisement.getOrganizerName()) %></td>
                    <td><%= WebUtil.formatDateTime(advertisement.getCreatedAt()) %></td>
                    <td class="action-cell">
                        <a class="action-link"
                           href="<%= ctx %>/advertisements?id=<%= advertisement.getAdvertisementId() %>">View</a>
                        <form method="post" action="<%= ctx %>/admin/advertisements" class="inline-form confirm-delete"
                              data-confirm="Are you sure you want to delete this advertisement?">
                            <input type="hidden" name="action" value="delete">
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
