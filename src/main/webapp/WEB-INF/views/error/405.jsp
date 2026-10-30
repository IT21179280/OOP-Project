<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    response.setStatus(405);
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="error-page">
    <h1>405 - Action Not Allowed</h1>
    <p>The action you tried to perform is not allowed from this page.</p>
    <p>Please use the buttons provided in the website.</p>
    <div class="form-actions">
        <a class="btn btn-primary" href="<%= ctx %>/home">Go to Home Page</a>
        <a class="btn btn-secondary" href="<%= ctx %>/events">Browse Events</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
