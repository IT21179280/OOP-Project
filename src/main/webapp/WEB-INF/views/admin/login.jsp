<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    String adminUsername = (String) request.getAttribute("adminUsername");
    String loginError = (String) request.getAttribute("loginError");
    if (adminUsername == null) { adminUsername = ""; }
    String loginRequired = request.getParameter("loginRequired");
    String logout = request.getParameter("logout");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="auth-layout auth-layout-centered">
    <div class="auth-box">
        <h1>Administrator Login</h1>
        <p class="auth-intro">This area is only for the system administrator.</p>

        <% if ("1".equals(loginRequired)) { %>
            <div class="alert alert-info">Please login as an administrator before opening that page.</div>
        <% } %>
        <% if ("1".equals(logout)) { %>
            <div class="alert alert-info">You have been logged out of the administrator panel.</div>
        <% } %>
        <% if (loginError != null) { %>
            <div class="alert alert-error"><%= WebUtil.escape(loginError) %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/admin/login">
            <div class="form-row">
                <label for="username">Administrator Username <span class="required">*</span></label>
                <input type="text" id="username" name="username"
                       value="<%= WebUtil.escape(adminUsername) %>" maxlength="50" required autofocus>
            </div>

            <div class="form-row">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" maxlength="100" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-block">Login as Administrator</button>
            </div>
        </form>

        <div class="auth-links">
            <p><a href="<%= ctx %>/login">Back to User Login</a></p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
