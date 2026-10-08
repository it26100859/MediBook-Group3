<%@ page import="com.clinic.admin.*" %>
<%@ include file="/includes/header.jsp" %>
<% Admin ad = (Admin) request.getAttribute("ad"); boolean edit = ad != null; %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3"><%=edit ? "Edit Admin " + ad.getId() : "Register Admin"%></h3>
    <form method="post" action="<%=ctx%>/admin">
      <input type="hidden" name="action" value="<%=edit ? "update" : "create"%>">
      <% if (edit) { %><input type="hidden" name="id" value="<%=ad.getId()%>"><% } %>
      <div class="mb-3"><label class="form-label">Full name</label><input name="fullName" value="<%=edit ? Util.esc(ad.getFullName()) : ""%>" class="form-control" required></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Username</label><input name="username" value="<%=edit ? Util.esc(ad.getUsername()) : ""%>" class="form-control" required></div>
        <div class="col-md-6 mb-3"><label class="form-label">Email</label><input type="email" name="email" value="<%=edit ? Util.esc(ad.getEmail()) : ""%>" class="form-control"></div>
      </div>
      <div class="mb-3"><label class="form-label">Password <%=edit ? "<span class='text-muted'>(blank = keep current)</span>" : "(6+ chars)"%></label><input type="password" name="password" class="form-control" <%=edit ? "" : "required minlength='6'"%>></div>
      <div class="mb-3"><label class="form-label d-block">Permissions</label>
        <% for (String p : Admin.PERMISSIONS) { %><div class="form-check form-check-inline"><input class="form-check-input" type="checkbox" name="permissions" value="<%=p%>" id="p<%=p%>" <%=edit && ad.hasPermission(p) ? "checked" : (!edit ? "checked" : "")%>><label class="form-check-label" for="p<%=p%>"><%=p%></label></div><% } %>
      </div>
      <button class="btn btn-primary">Save</button> <a class="btn btn-link" href="<%=ctx%>/admin?action=list">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
