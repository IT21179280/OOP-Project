<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eventplanner.model.User" %>
<%@ page import="com.eventplanner.model.Admin" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctxPath = request.getContextPath();
    User headerUser = (User) session.getAttribute("user");
    Admin headerAdmin = (Admin) session.getAttribute("admin");
    String titleValue = (String) request.getAttribute("pageTitle");
    if (titleValue == null) {
        titleValue = "Online Event Planning System";
    }

    String uri = request.getRequestURI();
    String activePage = "";
    if (uri.endsWith("/home") || uri.endsWith("/") || uri.endsWith("/index.jsp")) {
        activePage = "home";
    } else if (uri.contains("/events")) {
        activePage = "events";
    } else if (uri.contains("/advertisements")) {
        activePage = "advertisements";
    } else if (uri.contains("/about")) {
        activePage = "about";
    } else if (uri.contains("/contact")) {
        activePage = "contact";
    } else if (uri.contains("/dashboard")) {
        activePage = "dashboard";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#5b3cc4">
    <title><%= WebUtil.escape(titleValue) %> | Online Event Planning System</title>
    <link rel="icon" type="image/png" href="<%= ctxPath %>/images/logo.png">
    <link rel="stylesheet" href="<%= ctxPath %>/css/style.css">
</head>
<body>

<div class="top-strip">
    <div class="container top-strip-inner">
        <span>Online Event Planning System &ndash; 2nd Year Group Project (2022)</span>
        <span>Hotline: +94 11 234 5678</span>
    </div>
</div>

<header class="site-header">
    <div class="container header-inner">
        <a class="logo" href="<%= ctxPath %>/home">
            <img src="<%= ctxPath %>/images/logo.png" alt="Online Event Planning System logo" width="46" height="46">
            <span class="logo-text">Online Event<br><span class="logo-small">Planning System</span></span>
        </a>

        <button type="button" class="nav-toggle" aria-expanded="false" aria-controls="mainNav">
            <span aria-hidden="true">&#9776;</span> Menu
        </button>

        <nav class="main-nav" id="mainNav">
            <a href="<%= ctxPath %>/home" class="<%= "home".equals(activePage) ? "active" : "" %>">Home</a>
            <a href="<%= ctxPath %>/events" class="<%= "events".equals(activePage) ? "active" : "" %>">Events</a>
            <a href="<%= ctxPath %>/advertisements" class="<%= "advertisements".equals(activePage) ? "active" : "" %>">Advertisements</a>
            <a href="<%= ctxPath %>/about" class="<%= "about".equals(activePage) ? "active" : "" %>">About Us</a>
            <a href="<%= ctxPath %>/contact" class="<%= "contact".equals(activePage) ? "active" : "" %>">Contact Us</a>

            <% if (headerUser != null) { %>
                <a href="<%= ctxPath %>/user/dashboard" class="<%= "dashboard".equals(activePage) ? "active" : "" %>">My Dashboard</a>
                <a href="<%= ctxPath %>/logout" class="nav-button nav-logout">Logout</a>
            <% } else if (headerAdmin != null) { %>
                <a href="<%= ctxPath %>/admin/dashboard" class="<%= "dashboard".equals(activePage) ? "active" : "" %>">Admin Panel</a>
                <a href="<%= ctxPath %>/admin/logout" class="nav-button nav-logout">Logout</a>
            <% } else { %>
                <a href="<%= ctxPath %>/login" class="nav-button nav-login">Login</a>
                <a href="<%= ctxPath %>/register" class="nav-button nav-register">Register</a>
            <% } %>
        </nav>
    </div>
</header>

<% if (headerUser != null || headerAdmin != null) { %>
<div class="account-bar">
    <div class="container account-bar-inner">
        <% if (headerUser != null) { %>
            <span>Logged in as <strong><%= WebUtil.escape(headerUser.getFullName()) %></strong> (Registered User)</span>
        <% } else { %>
            <span>Logged in as <strong><%= WebUtil.escape(headerAdmin.getUsername()) %></strong> (Administrator)</span>
        <% } %>
        <span><a href="<%= ctxPath %>/contact">Need help? Contact us</a></span>
    </div>
</div>
<% } %>

<main class="container main-content">
    <jsp:include page="/WEB-INF/views/includes/messages.jsp" />
