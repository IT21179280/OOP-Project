<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventplanner.model.Category" %>
<%@ page import="com.eventplanner.util.WebUtil" %>
<%
    String ctx = request.getContextPath();
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/admin-nav.jsp" />

<div class="page-heading">
    <div>
        <h1>Manage Event Categories</h1>
        <p>Add new categories, rename existing categories or delete unused categories.</p>
    </div>
</div>

<div class="dashboard-panel">
    <h2>Add New Category</h2>
    <form class="inline-add-form" method="post" action="<%= ctx %>/admin/categories">
        <input type="hidden" name="action" value="add">
        <input type="text" name="categoryName" maxlength="50" placeholder="Category name" required>
        <button type="submit" class="btn btn-primary">Add Category</button>
    </form>
    <p class="form-hint">Category names must be unique and cannot be longer than 50 characters.</p>
</div>

<div class="result-info"><strong><%= categories == null ? 0 : categories.size() %></strong> event category(ies)</div>

<% if (categories == null || categories.isEmpty()) { %>
    <div class="empty-state"><p>No event categories have been created.</p></div>
<% } else { %>
    <table class="data-table">
        <thead>
            <tr>
                <th>Category ID</th>
                <th>Category Name</th>
                <th>Events Using This Category</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (Category category : categories) { %>
                <tr>
                    <td><%= category.getCategoryId() %></td>
                    <td><%= WebUtil.escape(category.getCategoryName()) %></td>
                    <td><%= category.getEventCount() %></td>
                    <td class="action-cell">
                        <form method="post" action="<%= ctx %>/admin/categories" class="inline-form">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="categoryId" value="<%= category.getCategoryId() %>">
                            <input type="text" name="categoryName"
                                   value="<%= WebUtil.escape(category.getCategoryName()) %>"
                                   maxlength="50" required class="inline-input">
                            <button type="submit" class="link-button">Update</button>
                        </form>

                        <form method="post" action="<%= ctx %>/admin/categories" class="inline-form confirm-delete"
                              data-confirm="Are you sure you want to delete this category?">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryId" value="<%= category.getCategoryId() %>">
                            <% if (category.getEventCount() > 0) { %>
                                <button type="button" class="link-button disabled-link"
                                        title="This category is used by existing events.">Delete</button>
                            <% } else { %>
                                <button type="submit" class="link-button danger-link">Delete</button>
                            <% } %>
                        </form>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
    <p class="form-hint">A category can only be deleted when no events are using it.</p>
<% } %>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
