<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    response.setStatus(404);
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="error-page">
    <h1>404 - Page Not Found</h1>
    <p>Sorry, the page you requested could not be found.</p>
    <p>It may have been removed, or the address may be incorrect.</p>
    <div class="form-actions">
        <a class="btn btn-primary" href="<%= ctx %>/home">Go to Home Page</a>
        <a class="btn btn-secondary" href="<%= ctx %>/events">Browse Events</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
