<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    response.setStatus(403);
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="error-page">
    <h1>403 - Access Denied</h1>
    <p>Sorry, you do not have permission to open this page.</p>
    <p>Please login with the correct account or return to the home page.</p>
    <div class="form-actions">
        <a class="btn btn-primary" href="<%= ctx %>/home">Go to Home Page</a>
        <a class="btn btn-secondary" href="<%= ctx %>/login">Login</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
