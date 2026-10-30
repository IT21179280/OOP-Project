<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    String loginValue = (String) request.getAttribute("login");
    String loginError = (String) request.getAttribute("loginError");
    if (loginValue == null) { loginValue = ""; }
    String loginRequired = request.getParameter("loginRequired");
    String logout = request.getParameter("logout");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="auth-layout">
    <div class="auth-box">
        <h1>User Login</h1>
        <p class="auth-intro">Login to create and manage your events and advertisements.</p>

        <% if ("1".equals(loginRequired)) { %>
            <div class="alert alert-info">Please login before opening that page.</div>
        <% } %>
        <% if ("1".equals(logout)) { %>
            <div class="alert alert-info">You have been logged out successfully.</div>
        <% } %>
        <% if (loginError != null) { %>
            <div class="alert alert-error"><%= WebUtil.escape(loginError) %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/login">
            <div class="form-row">
                <label for="login">Username or Email <span class="required">*</span></label>
                <input type="text" id="login" name="login" value="<%= WebUtil.escape(loginValue) %>"
                       maxlength="100" required autofocus>
            </div>

            <div class="form-row">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" maxlength="100" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </div>
        </form>

        <div class="auth-links">
            <p>Don't have an account? <a href="<%= ctx %>/register">Register here</a></p>
            <p><a href="<%= ctx %>/admin/login">Administrator Login</a></p>
        </div>
    </div>

    <div class="auth-side">
        <h2>Why create an account?</h2>
        <ul class="tick-list">
            <li>Create and manage your own events.</li>
            <li>Publish advertisements for your events.</li>
            <li>Edit or delete your details at any time.</li>
            <li>Keep all your event information in one place.</li>
        </ul>
        <p class="form-hint">
            Registration is free and only takes a few minutes.
        </p>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
