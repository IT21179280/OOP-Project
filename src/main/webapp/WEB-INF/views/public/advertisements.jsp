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

<div class="page-heading">
    <div>
        <h1>Event Advertisements</h1>
        <p>Browse advertisements published by our registered users.</p>
    </div>
    <% if (session.getAttribute("user") != null) { %>
        <a class="btn btn-primary" href="<%= ctx %>/user/advertisements/new">+ Create Advertisement</a>
    <% } %>
</div>

<form class="filter-form" method="get" action="<%= ctx %>/advertisements">
    <input type="text" name="keyword" value="<%= WebUtil.escape(keyword) %>"
           placeholder="Search advertisements by title, event name or location" maxlength="100">
    <button type="submit" class="btn btn-primary">Search</button>
    <a class="btn btn-secondary" href="<%= ctx %>/advertisements">Reset</a>
</form>

<div class="result-info">
    <strong>Published Advertisements</strong>
    &nbsp;|&nbsp; <%= advertisements == null ? 0 : advertisements.size() %> advertisement(s) found
    <% if (!keyword.isEmpty()) { %>
        for &ldquo;<%= WebUtil.escape(keyword) %>&rdquo;
    <% } %>
</div>

<% if (advertisements == null || advertisements.isEmpty()) { %>
    <div class="empty-state">
        <h3>No advertisements found</h3>
        <p>There are no advertisements matching your search at the moment.</p>
        <a class="btn btn-secondary" href="<%= ctx %>/advertisements">Show All Advertisements</a>
    </div>
<% } else { %>
    <div class="event-grid">
        <% for (Advertisement advertisement : advertisements) { %>
            <div class="card event-card">
                <img class="card-image" src="<%= WebUtil.advertisementImageUrl(ctx, advertisement.getImage()) %>"
                     alt="<%= WebUtil.escape(advertisement.getTitle()) %>">
                <div class="card-body">
                    <span class="category-tag">Advertisement</span>
                    <h3><%= WebUtil.escape(advertisement.getTitle()) %></h3>
                    <p class="card-meta"><strong>Event:</strong> <%= WebUtil.escape(advertisement.getEventName()) %></p>
                    <p class="card-meta"><strong>Date:</strong> <%= WebUtil.formatDate(advertisement.getEventDate()) %></p>
                    <p class="card-meta"><strong>Location:</strong> <%= WebUtil.escape(advertisement.getLocation()) %></p>
                    <p class="card-text"><%= WebUtil.escape(WebUtil.truncate(advertisement.getDescription(), 100)) %></p>
                    <a class="btn btn-small btn-primary"
                       href="<%= ctx %>/advertisements?id=<%= advertisement.getAdvertisementId() %>">View Details</a>
                </div>
            </div>
        <% } %>
    </div>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
