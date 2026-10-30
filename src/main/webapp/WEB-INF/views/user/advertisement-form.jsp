<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.eventplanner.model.Advertisement" %>
<%@ page import="com.eventplanner.model.Event" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Advertisement advertisement = (Advertisement) request.getAttribute("advertisement");
    List<Event> myEvents = (List<Event>) request.getAttribute("myEvents");
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String formMode = (String) request.getAttribute("formMode");
    String formAction = (String) request.getAttribute("formAction");
    String cancelUrl = (String) request.getAttribute("cancelUrl");

    boolean editMode = "edit".equals(formMode);
    String heading = editMode ? "Edit Advertisement" : "Create New Advertisement";

    String dateValue = (String) request.getAttribute("advertisementDateValue");
    if (dateValue == null) {
        dateValue = advertisement.getEventDate() == null ? "" : WebUtil.formatDateForInput(advertisement.getEventDate());
    }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<p class="breadcrumb">
    <a href="<%= ctx %><%= cancelUrl %>">&laquo; Back to Advertisements</a>
</p>

<div class="page-heading">
    <div>
        <h1><%= heading %></h1>
        <p>Publish an advertisement to promote your event.</p>
    </div>
</div>

<% if (errors != null && !errors.isEmpty()) { %>
    <div class="alert alert-error">The advertisement could not be saved. Please correct the highlighted fields.</div>
<% } %>

<div class="form-panel">
    <form method="post" action="<%= ctx %><%= formAction %>" enctype="multipart/form-data" id="advertisementForm">

        <% if (editMode) { %>
            <input type="hidden" name="advertisementId" value="<%= advertisement.getAdvertisementId() %>">
        <% } %>

        <div class="form-row">
            <label for="title">Advertisement Title <span class="required">*</span></label>
            <input type="text" id="title" name="title"
                   value="<%= WebUtil.escape(advertisement.getTitle()) %>" maxlength="150" required>
            <% if (errors != null && errors.get("title") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("title")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="relatedEvent">Related Event (Optional)</label>
            <select id="relatedEvent" name="eventId">
                <option value="">-- No related event --</option>
                <% if (myEvents != null) {
                    for (Event myEvent : myEvents) {
                        boolean selected = advertisement.getEventId() != null
                                && advertisement.getEventId().intValue() == myEvent.getEventId();
                %>
                    <option value="<%= myEvent.getEventId() %>"
                            data-name="<%= WebUtil.escape(myEvent.getEventTitle()) %>"
                            data-date="<%= WebUtil.formatDateForInput(myEvent.getEventDate()) %>"
                            data-location="<%= WebUtil.escape(myEvent.getLocation()) %>"
                            <%= selected ? "selected" : "" %>>
                        <%= WebUtil.escape(myEvent.getEventTitle()) %>
                    </option>
                <%  }
                } %>
            </select>
            <span class="form-hint">Select one of your events to fill in the event details automatically.</span>
            <% if (errors != null && errors.get("eventId") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("eventId")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="eventName">Event Name <span class="required">*</span></label>
            <input type="text" id="eventName" name="eventName"
                   value="<%= WebUtil.escape(advertisement.getEventName()) %>" maxlength="150" required>
            <% if (errors != null && errors.get("eventName") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("eventName")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="description">Advertisement Description <span class="required">*</span></label>
            <textarea id="description" name="description" rows="6" maxlength="5000" required><%= WebUtil.escape(advertisement.getDescription()) %></textarea>
            <span class="form-hint">Explain why people should attend the event and what they can expect.</span>
            <% if (errors != null && errors.get("description") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("description")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="eventDate">Event Date <span class="required">*</span></label>
            <input type="date" id="eventDate" name="eventDate" value="<%= WebUtil.escape(dateValue) %>" required>
            <% if (errors != null && errors.get("eventDate") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("eventDate")) %></span>
            <% } %>
        </div>

        <div class="form-row">
            <label for="location">Event Location <span class="required">*</span></label>
            <input type="text" id="location" name="location"
                   value="<%= WebUtil.escape(advertisement.getLocation()) %>" maxlength="150" required>
            <% if (errors != null && errors.get("location") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("location")) %></span>
            <% } %>
        </div>

        <div class="form-two">
            <div class="form-row">
                <label for="contactNumber">Contact Number <span class="required">*</span></label>
                <input type="text" id="contactNumber" name="contactNumber"
                       value="<%= WebUtil.escape(advertisement.getContactNumber()) %>" maxlength="20" required>
                <% if (errors != null && errors.get("contactNumber") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("contactNumber")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="email">Email Address <span class="required">*</span></label>
                <input type="email" id="email" name="email"
                       value="<%= WebUtil.escape(advertisement.getEmail()) %>" maxlength="100" required>
                <% if (errors != null && errors.get("email") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("email")) %></span>
                <% } %>
            </div>
        </div>

        <div class="form-row">
            <label for="image">Advertisement Image</label>
            <input type="file" id="image" name="image" accept=".jpg,.jpeg,.png,.gif">
            <span class="form-hint">Optional. JPG, PNG or GIF image up to 5 MB.</span>
            <% if (editMode && advertisement.getImage() != null) { %>
                <div class="current-image">
                    <span>Current image:</span>
                    <img src="<%= WebUtil.advertisementImageUrl(ctx, advertisement.getImage()) %>" alt="Current advertisement image">
                </div>
            <% } %>
            <% if (errors != null && errors.get("image") != null) { %>
                <span class="field-error"><%= WebUtil.escape(errors.get("image")) %></span>
            <% } %>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><%= editMode ? "Update Advertisement" : "Publish Advertisement" %></button>
            <a class="btn btn-secondary" href="<%= ctx %><%= cancelUrl %>">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
