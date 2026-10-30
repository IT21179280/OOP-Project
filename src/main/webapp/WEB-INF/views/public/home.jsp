<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.model.Advertisement" %>
<%@ page import="com.eventplanner.model.Category" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Admin" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Event> upcomingEvents = (List<Event>) request.getAttribute("upcomingEvents");
    List<Advertisement> recentAdvertisements = (List<Advertisement>) request.getAttribute("recentAdvertisements");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    Integer totalUpcomingEvents = (Integer) request.getAttribute("totalUpcomingEvents");
    User homeUser = (User) session.getAttribute("user");
    Admin homeAdmin = (Admin) session.getAttribute("admin");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<section class="hero">
    <div class="hero-text">
        <p class="hero-label">Welcome to the Online Event Planning System</p>
        <h1>Plan, Organize and Advertise Your Events in One Place</h1>
        <p>
            Our system helps you to create events, keep all event details in one place and
            publish advertisements so that more people can find out about your event.
            It is simple, free and easy to use.
        </p>
        <div class="hero-buttons">
            <a class="btn btn-primary" href="<%= ctx %>/events">Browse Events</a>
            <% if (homeUser != null) { %>
                <a class="btn btn-outline" href="<%= ctx %>/user/events/new">Create an Event</a>
            <% } else if (homeAdmin == null) { %>
                <a class="btn btn-outline" href="<%= ctx %>/register">Create an Account</a>
            <% } %>
        </div>
        <p class="hero-note">
            <strong><%= totalUpcomingEvents == null ? 0 : totalUpcomingEvents %></strong>
            upcoming event(s) are currently listed on the website.
        </p>
    </div>
    <div class="hero-image">
        <img src="<%= ctx %>/images/banner.png" alt="Event planning banner">
    </div>
</section>

<section class="feature-row">
    <div class="feature-box">
        <h3>Create Events</h3>
        <p>Add your event details, date, time and location in a few minutes.</p>
    </div>
    <div class="feature-box">
        <h3>Advertise Events</h3>
        <p>Publish an advertisement so that visitors can easily find your event.</p>
    </div>
    <div class="feature-box">
        <h3>Stay Organized</h3>
        <p>Edit or delete your events and advertisements whenever you need to.</p>
    </div>
</section>

<section class="section">
    <div class="section-heading">
        <h2>Upcoming Events</h2>
        <a href="<%= ctx %>/events">View All Events &raquo;</a>
    </div>

    <% if (upcomingEvents == null || upcomingEvents.isEmpty()) { %>
        <div class="empty-state">
            <p>There are no upcoming events at the moment. Please check again soon.</p>
        </div>
    <% } else { %>
        <div class="event-grid">
            <% for (Event event : upcomingEvents) { %>
                <div class="card event-card">
                    <img class="card-image" src="<%= WebUtil.eventImageUrl(ctx, event.getEventImage()) %>"
                         alt="<%= WebUtil.escape(event.getEventTitle()) %>">
                    <div class="card-body">
                        <span class="category-tag"><%= WebUtil.escape(event.getCategoryName()) %></span>
                        <h3><%= WebUtil.escape(event.getEventTitle()) %></h3>
                        <p class="card-meta"><strong>Date:</strong> <%= WebUtil.formatDate(event.getEventDate()) %></p>
                        <p class="card-meta"><strong>Location:</strong> <%= WebUtil.escape(event.getLocation()) %></p>
                        <p class="card-text"><%= WebUtil.escape(WebUtil.truncate(event.getDescription(), 105)) %></p>
                        <a class="btn btn-small btn-primary" href="<%= ctx %>/events?id=<%= event.getEventId() %>">View Details</a>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</section>

<section class="section">
    <div class="section-heading">
        <h2>Recently Published Advertisements</h2>
        <a href="<%= ctx %>/advertisements">View All Advertisements &raquo;</a>
    </div>

    <% if (recentAdvertisements == null || recentAdvertisements.isEmpty()) { %>
        <div class="empty-state">
            <p>No advertisements have been published yet.</p>
        </div>
    <% } else { %>
        <div class="event-grid">
            <% for (Advertisement advertisement : recentAdvertisements) { %>
                <div class="card event-card">
                    <img class="card-image" src="<%= WebUtil.advertisementImageUrl(ctx, advertisement.getImage()) %>"
                         alt="<%= WebUtil.escape(advertisement.getTitle()) %>">
                    <div class="card-body">
                        <span class="category-tag">Advertisement</span>
                        <h3><%= WebUtil.escape(advertisement.getTitle()) %></h3>
                        <p class="card-meta"><strong>Event:</strong> <%= WebUtil.escape(advertisement.getEventName()) %></p>
                        <p class="card-meta"><strong>Date:</strong> <%= WebUtil.formatDate(advertisement.getEventDate()) %></p>
                        <p class="card-meta"><strong>Location:</strong> <%= WebUtil.escape(advertisement.getLocation()) %></p>
                        <a class="btn btn-small btn-primary"
                           href="<%= ctx %>/advertisements?id=<%= advertisement.getAdvertisementId() %>">View Details</a>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</section>

<section class="section">
    <div class="section-heading">
        <h2>Event Planning Information</h2>
    </div>
    <div class="two-column">
        <div class="info-box">
            <h3>What is event planning?</h3>
            <p>
                Event planning is the process of organizing an event from the first idea until the
                last guest goes home. It includes selecting a date, booking a location, preparing a
                budget, inviting guests and promoting the event.
            </p>
            <p>
                This system was built to make those tasks easier. Instead of keeping details in
                notebooks or sending messages to everybody, you can store the details in one place
                and share them using a public event page or advertisement.
            </p>
        </div>
        <div class="info-box">
            <h3>Our planning tips</h3>
            <ul class="tick-list">
                <li>Select the event date and time carefully.</li>
                <li>Choose a location that is easy for guests to find.</li>
                <li>Prepare a simple budget before booking anything.</li>
                <li>Publish an advertisement at least two weeks before the event.</li>
                <li>Keep your contact details up to date.</li>
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section-heading">
        <h2>Browse Events by Category</h2>
    </div>
    <div class="category-links">
        <% if (categories != null) {
            for (Category category : categories) { %>
                <a href="<%= ctx %>/events?category=<%= category.getCategoryId() %>"><%= WebUtil.escape(category.getCategoryName()) %></a>
        <%  }
        } %>
    </div>
</section>

<section class="callout">
    <div class="callout-text">
        <h2>Ready to plan your next event?</h2>
        <p>Create a free account to add events and publish your own advertisements.</p>
    </div>
    <div class="callout-action">
        <% if (homeUser != null) { %>
            <a class="btn btn-light" href="<%= ctx %>/user/dashboard">Go to My Dashboard</a>
        <% } else if (homeAdmin != null) { %>
            <a class="btn btn-light" href="<%= ctx %>/admin/dashboard">Go to Admin Panel</a>
        <% } else { %>
            <a class="btn btn-light" href="<%= ctx %>/register">Register Now</a>
        <% } %>
    </div>
</section>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
