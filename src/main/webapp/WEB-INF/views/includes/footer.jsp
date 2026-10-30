<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String footerCtx = request.getContextPath();
%>
</main>

<footer class="site-footer">
    <div class="container footer-grid">
        <div class="footer-column">
            <h4>Online Event Planning System</h4>
            <p>A simple web based system for planning, managing and advertising events.
                Created as a 2nd year group project in 2022.</p>
        </div>
        <div class="footer-column">
            <h4>Quick Links</h4>
            <ul>
                <li><a href="<%= footerCtx %>/home">Home</a></li>
                <li><a href="<%= footerCtx %>/events">Events</a></li>
                <li><a href="<%= footerCtx %>/advertisements">Advertisements</a></li>
                <li><a href="<%= footerCtx %>/about">About Us</a></li>
                <li><a href="<%= footerCtx %>/contact">Contact Us</a></li>
            </ul>
        </div>
        <div class="footer-column">
            <h4>Contact Information</h4>
            <p>
                No. 25, Temple Road,<br>
                Colombo 03, Sri Lanka.<br>
                Phone: +94 11 234 5678<br>
                Email: info@eventplanner.lk
            </p>
        </div>
    </div>
    <div class="footer-bottom">
        <div class="container">
            <p>&copy; 2022 Online Event Planning System. All rights reserved. |
                University Group Project</p>
        </div>
    </div>
</footer>

<script src="<%= footerCtx %>/js/main.js"></script>
</body>
</html>
