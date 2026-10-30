<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }
    if (errorMessage != null) {
        session.removeAttribute("errorMessage");
    }
%>
<% if (successMessage != null) { %>
    <div class="alert alert-success"><%= WebUtil.escape(successMessage) %></div>
<% } %>
<% if (errorMessage != null) { %>
    <div class="alert alert-error"><%= WebUtil.escape(errorMessage) %></div>
<% } %>
