<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String fullName = (String) request.getAttribute("fullName");
    String email = (String) request.getAttribute("email");
    String username = (String) request.getAttribute("username");
    String contactNumber = (String) request.getAttribute("contactNumber");
    if (fullName == null) { fullName = ""; }
    if (email == null) { email = ""; }
    if (username == null) { username = ""; }
    if (contactNumber == null) { contactNumber = ""; }
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>Create an Account</h1>
        <p>Register to start planning and advertising your events.</p>
    </div>
</div>

<div class="auth-layout">
    <div class="auth-box auth-box-wide">
        <% if (errors != null && !errors.isEmpty()) { %>
            <div class="alert alert-error">Please correct the highlighted fields and try again.</div>
        <% } %>

        <form method="post" action="<%= ctx %>/register" id="registerForm">
            <div class="form-row">
                <label for="fullName">Full Name <span class="required">*</span></label>
                <input type="text" id="fullName" name="fullName" value="<%= WebUtil.escape(fullName) %>"
                       maxlength="100" required>
                <% if (errors != null && errors.get("fullName") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("fullName")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="email">Email Address <span class="required">*</span></label>
                <input type="email" id="email" name="email" value="<%= WebUtil.escape(email) %>"
                       maxlength="100" required>
                <% if (errors != null && errors.get("email") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("email")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="username">Username <span class="required">*</span></label>
                <input type="text" id="username" name="username" value="<%= WebUtil.escape(username) %>"
                       maxlength="50" required>
                <span class="form-hint">4 to 50 characters. Letters, numbers, dots and underscores only.</span>
                <% if (errors != null && errors.get("username") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("username")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="contactNumber">Contact Number <span class="required">*</span></label>
                <input type="text" id="contactNumber" name="contactNumber"
                       value="<%= WebUtil.escape(contactNumber) %>" maxlength="20" required>
                <% if (errors != null && errors.get("contactNumber") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("contactNumber")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" maxlength="100" required>
                <span class="form-hint">Use at least 6 characters.</span>
                <% if (errors != null && errors.get("password") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("password")) %></span>
                <% } %>
            </div>

            <div class="form-row">
                <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                <input type="password" id="confirmPassword" name="confirmPassword" maxlength="100" required>
                <% if (errors != null && errors.get("confirmPassword") != null) { %>
                    <span class="field-error"><%= WebUtil.escape(errors.get("confirmPassword")) %></span>
                <% } %>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Register</button>
                <a class="btn btn-secondary" href="<%= ctx %>/login">Already Registered? Login</a>
            </div>
        </form>
    </div>

    <div class="auth-side">
        <h2>Registration Information</h2>
        <p>
            Fields marked with <span class="required">*</span> are required. Your email address and
            username must be unique because they are used to login to the system.
        </p>
        <ul class="tick-list">
            <li>Your contact number is shown only to people who view your events.</li>
            <li>You can create any number of events.</li>
            <li>You can publish advertisements for your events.</li>
            <li>You can delete your events and advertisements at any time.</li>
        </ul>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
