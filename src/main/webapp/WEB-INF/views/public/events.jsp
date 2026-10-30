<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.model.Category" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Event> events = (List<Event>) request.getAttribute("events");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String keyword = (String) request.getAttribute("keyword");
    Integer selectedCategory = (Integer) request.getAttribute("selectedCategory");
    String timeframe = (String) request.getAttribute("timeframe");
    if (keyword == null) { keyword = ""; }
    if (timeframe == null) { timeframe = "upcoming"; }

    String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
    String filterQuery = "keyword=" + encodedKeyword;
    if (selectedCategory != null) {
        filterQuery += "&category=" + selectedCategory;
    }
    String timeframeLabel = "Upcoming".equalsIgnoreCase(timeframe) ? "Upcoming Events"
            : ("past".equals(timeframe) ? "Past Events" : "All Events");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>Events</h1>
        <p>Browse the events that have been added to the system.</p>
    </div>
    <% if (session.getAttribute("user") != null) { %>
        <a class="btn btn-primary" href="<%= ctx %>/user/events/new">+ Create New Event</a>
    <% } %>
</div>

<div class="tab-links">
    <a href="<%= ctx %>/events?<%= filterQuery %>&time=upcoming"
       class="<%= "upcoming".equals(timeframe) ? "active" : "" %>">Upcoming Events</a>
    <a href="<%= ctx %>/events?<%= filterQuery %>&time=past"
       class="<%= "past".equals(timeframe) ? "active" : "" %>">Past Events</a>
    <a href="<%= ctx %>/events?<%= filterQuery %>&time=all"
       class="<%= "all".equals(timeframe) ? "active" : "" %>">All Events</a>
</div>

<form class="filter-form" method="get" action="<%= ctx %>/events">
    <input type="text" name="keyword" value="<%= WebUtil.escape(keyword) %>"
           placeholder="Search by event name, description or location" maxlength="100">
    <select name="category">
        <option value="">All Categories</option>
        <% if (categories != null) {
            for (Category category : categories) {
                boolean selected = selectedCategory != null
                        && selectedCategory.intValue() == category.getCategoryId();
        %>
            <option value="<%= category.getCategoryId() %>" <%= selected ? "selected" : "" %>>
                <%= WebUtil.escape(category.getCategoryName()) %>
            </option>
        <%  }
        } %>
    </select>
    <input type="hidden" name="time" value="<%= WebUtil.escape(timeframe) %>">
    <button type="submit" class="btn btn-primary">Search</button>
    <a class="btn btn-secondary" href="<%= ctx %>/events">Reset</a>
</form>

<div class="result-info">
    <strong><%= timeframeLabel %></strong>
    &nbsp;|&nbsp; <%= events == null ? 0 : events.size() %> event(s) found
    <% if (!keyword.isEmpty()) { %>
        for &ldquo;<%= WebUtil.escape(keyword) %>&rdquo;
    <% } %>
</div>

<% if (events == null || events.isEmpty()) { %>
    <div class="empty-state">
        <h3>No events found</h3>
        <p>Please try a different search word or category.</p>
        <a class="btn btn-secondary" href="<%= ctx %>/events">Show All Upcoming Events</a>
    </div>
<% } else { %>
    <div class="event-grid">
        <% for (Event event : events) { %>
            <div class="card event-card">
                <img class="card-image" src="<%= WebUtil.eventImageUrl(ctx, event.getEventImage()) %>"
                     alt="<%= WebUtil.escape(event.getEventTitle()) %>">
                <div class="card-body">
                    <span class="category-tag"><%= WebUtil.escape(event.getCategoryName()) %></span>
                    <h3><%= WebUtil.escape(event.getEventTitle()) %></h3>
                    <p class="card-meta"><strong>Date:</strong> <%= WebUtil.formatDate(event.getEventDate()) %></p>
                    <p class="card-meta"><strong>Time:</strong>
                        <%= WebUtil.formatTime(event.getStartTime()) %> -
                        <%= WebUtil.formatTime(event.getEndTime()) %></p>
                    <p class="card-meta"><strong>Location:</strong> <%= WebUtil.escape(event.getLocation()) %></p>
                    <p class="card-text"><%= WebUtil.escape(WebUtil.truncate(event.getDescription(), 100)) %></p>
                    <a class="btn btn-small btn-primary" href="<%= ctx %>/events?id=<%= event.getEventId() %>">View Details</a>
                </div>
            </div>
        <% } %>
    </div>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
