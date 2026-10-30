<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%
    String ctx = request.getContextPath();
    response.setStatus(500);
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="error-page">
    <h1>500 - Something Went Wrong</h1>
    <p>Sorry, an unexpected error occurred while processing your request.</p>
    <p>Please check that the MySQL database is running and that the database settings
        in <strong>db.properties</strong> are correct. Then try again.</p>
    <div class="form-actions">
        <a class="btn btn-primary" href="<%= ctx %>/home">Go to Home Page</a>
        <a class="btn btn-secondary" href="<%= ctx %>/contact">Contact Us</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
