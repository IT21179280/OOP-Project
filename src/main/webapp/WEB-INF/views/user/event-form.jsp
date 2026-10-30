<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.model.Category" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Event event = (Event) request.getAttribute("event");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String formMode = (String) request.getAttribute("formMode");
    String formAction = (String) request.getAttribute("formAction");
    String cancelUrl = (String) request.getAttribute("cancelUrl");
    Boolean adminMode = (Boolean) request.getAttribute("adminMode");

    boolean editMode = "edit".equals(formMode);
    String heading = editMode ? "Edit Event" : "Create New Event";

    String dateValue = (String) request.getAttribute("eventDateValue");
    if (dateValue == null) {
        dateValue = event.getEventDate() == null ? "" : WebUtil.formatDateForInput(event.getEventDate());
    }
    String startValue = (String) request.getAttribute("startTimeValue");
    if (startValue == null) {
        startValue = event.getStartTime() == null ? "" : WebUtil.formatTimeForInput(event.getStartTime());
    }
    String endValue = (String) request.getAttribute("endTimeValue");
    if (endValue == null) {
        endValue = event.getEndTime() == null ? "" : WebUtil.formatTimeForInput(event.getEndTime());
    }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<p class="breadcrumb">
    <a href="<%= ctx %><%= cancelUrl %>">&laquo; Back to Events</a>
</p>

<div class="page-heading">
    <div>
        <h1><%= heading %></h1>
        <p>Fields marked with <span class="required">*</span> are required.</p>
    </div>
</div>

<% if (errors != null && !errors.isEmpty()) { %>
    <div class="alert alert-error">The event could not be saved. Please correct the highlighted fields.</div>
<% } %>

<div class="form-panel">
    <form method="post" action="<%= ctx %><%= formAction %>" enctype="multipart/form-data" id="eventForm">

        <% if (editMode) { %>
            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
        <% } %>

        <div class="form-row">
            <label for="eventTitle">Event Title <span class="required">*</span></label>
            <input type="text" id="eventTitle" name="eventTitle"
                   value="<%= WebUtil.escape(event.getEventTitle()) %>" maxlength="150" required>
            <% if (errors != null && errors.get("eventTitle") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("eventTitle")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="categoryId">Event Category <span class="required">*</span></label>
            <select id="categoryId" name="categoryId" required>
                <option value="">-- Select a Category --</option>
                <% if (categories != null) {
                    for (Category category : categories) {
                        boolean selected = event.getCategoryId() == category.getCategoryId();
                %>
                    <option value="<%= category.getCategoryId() %>" <%= selected ? "selected" : "" %>>
                        <%= WebUtil.escape(category.getCategoryName()) %>
                    </option>
                <%  }
                } %>
            </select>
            <% if (errors != null && errors.get("categoryId") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("categoryId")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="description">Event Description <span class="required">*</span></label>
            <textarea id="description" name="description" rows="6" maxlength="5000" required><%= WebUtil.escape(event.getDescription()) %></textarea>
            <span class="form-hint">Describe what will happen at the event and who should attend.</span>
            <% if (errors != null && errors.get("description") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("description")) %></span>
            <% } %>
        </div>

        <div class="form-three">
            <div class="form-row">
                <label for="eventDate">Event Date <span class="required">*</span></label>
                <input type="date" id="eventDate" name="eventDate" value="<%= WebUtil.escape(dateValue) %>" required>
                <% if (errors != null && errors.get("eventDate") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("eventDate")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="startTime">Start Time <span class="required">*</span></label>
                <input type="time" id="startTime" name="startTime" value="<%= WebUtil.escape(startValue) %>" required>
                <% if (errors != null && errors.get("startTime") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("startTime")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="endTime">End Time <span class="required">*</span></label>
                <input type="time" id="endTime" name="endTime" value="<%= WebUtil.escape(endValue) %>" required>
                <% if (errors != null && errors.get("endTime") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("endTime")) %></span>
                <% } %>
            </div>
        </div>

        <div class="form-row">
            <label for="location">Event Location <span class="required">*</span></label>
            <input type="text" id="location" name="location"
                   value="<%= WebUtil.escape(event.getLocation()) %>" maxlength="150" required>
            <% if (errors != null && errors.get("location") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("location")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="contactInformation">Contact Information <span class="required">*</span></label>
            <input type="text" id="contactInformation" name="contactInformation"
                   value="<%= WebUtil.escape(event.getContactInformation()) %>" maxlength="100" required>
            <span class="form-hint">Phone number or email address that visitors can use to contact you.</span>
            <% if (errors != null && errors.get("contactInformation") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("contactInformation")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="eventImage">Event Image</label>
            <input type="file" id="eventImage" name="eventImage" accept=".jpg,.jpeg,.png,.gif">
            <span class="form-hint">Optional. JPG, PNG or GIF image up to 5 MB.</span>
            <% if (editMode && event.getEventImage() != null) { %>
                <div class="current-image">
                    <span>Current image:</span>
                    <img src="<%= WebUtil.eventImageUrl(ctx, event.getEventImage()) %>" alt="Current event image">
                </div>
            <% } %>
            <% if (errors != null && errors.get("eventImage") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("eventImage")) %></span>
            <% } %>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><%= editMode ? "Update Event" : "Create Event" %></button>
            <a class="btn btn-secondary" href="<%= ctx %><%= cancelUrl %>">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
