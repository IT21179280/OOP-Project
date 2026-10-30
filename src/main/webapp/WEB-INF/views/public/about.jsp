<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="page-heading">
    <div>
        <h1>About Us</h1>
        <p>Learn more about the Online Event Planning System.</p>
    </div>
</div>

<div class="about-layout">
    <div class="about-main">
        <h2>What is the Online Event Planning System?</h2>
        <p>
            The Online Event Planning System is a web based application that helps people to plan,
            organize and advertise events. Registered users can create events, update their details,
            delete events that are no longer needed and publish advertisements for their events.
            Visitors can browse all published events and advertisements without creating an account.
        </p>

        <h2>Why was this system developed?</h2>
        <p>
            We noticed that many people organize small events such as birthday parties, weddings and
            music shows by using notebooks, phone messages and social media posts. Because of this,
            important details are often lost or shared with the wrong people. We developed this system
            to provide one simple place where event details can be stored and shared.
        </p>

        <h2>Main Objectives</h2>
        <ul class="tick-list">
            <li>Provide a simple way to create and manage events.</li>
            <li>Allow users to advertise their events to a wider audience.</li>
            <li>Keep event information organized in a MySQL database.</li>
            <li>Give visitors an easy way to search and filter events.</li>
            <li>Provide an administrator panel to manage the system.</li>
            <li>Apply the web development knowledge gained during our second year.</li>
        </ul>

        <h2>Benefits for Users</h2>
        <ul class="tick-list">
            <li>Free registration and free use of the main features.</li>
            <li>All event details are stored safely in one place.</li>
            <li>Users can edit their event details at any time.</li>
            <li>Advertisements help to attract more visitors to an event.</li>
            <li>Events can be searched by name, category and date.</li>
        </ul>

        <h2>How the System Helps</h2>
        <p>
            Instead of preparing separate posters and messages for every event, a user can add the
            event once and publish an advertisement from the same account. Visitors can then find
            the event using the search and category filters, read the full details and contact the
            organizer using the contact information shown on the event page.
        </p>

        <div class="timeline">
            <div class="timeline-step">
                <span class="step-number">1</span>
                <div>
                    <h3>Create an account</h3>
                    <p>Register with your name, email address and contact number.</p>
                </div>
            </div>
            <div class="timeline-step">
                <span class="step-number">2</span>
                <div>
                    <h3>Add your event</h3>
                    <p>Enter the event date, time, location and other important details.</p>
                </div>
            </div>
            <div class="timeline-step">
                <span class="step-number">3</span>
                <div>
                    <h3>Publish an advertisement</h3>
                    <p>Promote the event so that visitors can find it quickly.</p>
                </div>
            </div>
            <div class="timeline-step">
                <span class="step-number">4</span>
                <div>
                    <h3>Manage your events</h3>
                    <p>Update or delete your events and advertisements from your dashboard.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="about-side">
        <div class="info-box">
            <h3>Project Information</h3>
            <table class="details-table">
                <tr><th>Project</th><td>Online Event Planning System</td></tr>
                <tr><th>Year</th><td>2022</td></tr>
                <tr><th>Module</th><td>2nd Year Group Project</td></tr>
                <tr><th>Technologies</th><td>Java Servlet, JSP, JDBC, MySQL, HTML, CSS, JavaScript</td></tr>
                <tr><th>Server</th><td>Apache Tomcat</td></tr>
            </table>
        </div>

        <div class="info-box">
            <h3>Our Mission</h3>
            <p>
                To make event planning easier for everyone by providing a simple, reliable and
                affordable online system.
            </p>
        </div>

        <div class="info-box contact-summary">
            <h3>Contact Us</h3>
            <p>
                No. 25, Temple Road,<br>
                Colombo 03, Sri Lanka.<br><br>
                Phone: +94 11 234 5678<br>
                Email: info@eventplanner.lk
            </p>
            <a class="btn btn-primary btn-small" href="<%= ctx %>/contact">Send a Message</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
