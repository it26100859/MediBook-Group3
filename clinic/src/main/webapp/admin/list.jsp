<%@ page import="java.util.*,com.clinic.admin.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Admin> admins = (List<Admin>) request.getAttribute("admins"); %>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3 class="mb-0">Admin Management</h3>
  <div><a class="btn btn-outline-secondary me-2" href="<%=ctx%>/admin?action=logs">Activity log</a><a class="btn btn-primary" href="<%=ctx%>/admin?action=form">+ Register Admin</a></div>
</div>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>ID</th><th>Name</th><th>Username</th><th>Email</th><th>Permissions</th><th></th></tr></thead>
  <tbody>
  <% for (Admin a : admins) { %>
    <tr><td><%=a.getId()%></td><td><%=Util.esc(a.getFullName())%></td><td><%=Util.esc(a.getUsername())%></td><td><%=Util.esc(a.getEmail())%></td>
        <td><%=a.getPermissions().isEmpty() ? "<span class='text-danger'>disabled</span>" : Util.esc(a.getPermissions())%></td>
        <td class="text-end"><a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/admin?action=form&id=<%=a.getId()%>">Edit</a>
          <form method="post" action="<%=ctx%>/admin" class="d-inline" onsubmit="return confirm('Delete this admin?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=a.getId()%>"><button class="btn btn-sm btn-outline-danger">Delete</button></form></td></tr>
  <% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
