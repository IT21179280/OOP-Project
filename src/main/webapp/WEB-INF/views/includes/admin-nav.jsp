<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String adminCtx = request.getContextPath();
    String adminUri = request.getRequestURI();
%>
<div class="admin-nav">
    <a href="<%= adminCtx %>/admin/dashboard"
       class="<%= adminUri.endsWith("/admin/dashboard") ? "active" : "" %>">Dashboard</a>
    <a href="<%= adminCtx %>/admin/users"
       class="<%= adminUri.endsWith("/admin/users") ? "active" : "" %>">Users</a>
    <a href="<%= adminCtx %>/admin/events"
       class="<%= adminUri.endsWith("/admin/events") ? "active" : "" %>">Events</a>
    <a href="<%= adminCtx %>/admin/advertisements"
       class="<%= adminUri.endsWith("/admin/advertisements") ? "active" : "" %>">Advertisements</a>
    <a href="<%= adminCtx %>/admin/categories"
       class="<%= adminUri.endsWith("/admin/categories") ? "active" : "" %>">Categories</a>
    <a href="<%= adminCtx %>/home">View Website</a>
</div>
