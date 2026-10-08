<%@ page import="com.clinic.patient.*" %>
<%@ include file="/includes/header.jsp" %>
<% Patient p = (Patient) request.getAttribute("p"); boolean adminEdit = request.getAttribute("adminEdit") != null; %>
<% if (p == null) { %><div class="alert alert-warning">Patient not found.</div><% } else { %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-1"><%=adminEdit ? "Edit Patient " + Util.esc(p.getId()) : "My Profile"%></h3>
    <p class="text-muted">Username: <%=Util.esc(p.getUsername())%> &middot; Type: <%=p.getType()%></p>
    <form method="post" action="<%=ctx%>/patient">
      <input type="hidden" name="action" value="update"><input type="hidden" name="id" value="<%=p.getId()%>">
      <div class="mb-3"><label class="form-label">Full name</label><input name="fullName" value="<%=Util.esc(p.getFullName())%>" class="form-control" required></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Email</label><input type="email" name="email" value="<%=Util.esc(p.getEmail())%>" class="form-control"></div>
        <div class="col-md-6 mb-3"><label class="form-label">Phone</label><input name="phone" value="<%=Util.esc(p.getPhone())%>" class="form-control"></div>
      </div>
      <div class="mb-3"><label class="form-label">Insurance number</label><input name="insuranceNo" value="<%=Util.esc(p.getInsuranceNo())%>" class="form-control"></div>
      <% if (adminEdit) { %>
      <div class="mb-3"><label class="form-label">Patient type</label>
        <select name="type" class="form-select"><option value="REGULAR" <%="REGULAR".equals(p.getType())?"selected":""%>>Regular</option><option value="INSURED" <%="INSURED".equals(p.getType())?"selected":""%>>Insured</option></select></div>
      <% } %>
      <div class="mb-3"><label class="form-label">New password <span class="text-muted">(leave blank to keep current)</span></label><input type="password" name="password" class="form-control"></div>
      <button class="btn btn-primary">Save changes</button>
    </form>
  </div></div>
</div></div>
<% } %>
<%@ include file="/includes/footer.jsp" %>
