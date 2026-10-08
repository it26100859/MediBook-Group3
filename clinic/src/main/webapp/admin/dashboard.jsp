<%@ include file="/includes/header.jsp" %>
<h3 class="mb-3">Admin Dashboard</h3>
<div class="row g-3 mb-4">
  <% String[][] cards = {
      {"Patients", String.valueOf(request.getAttribute("nPatients")), "/patient?action=list"},
      {"Doctors", String.valueOf(request.getAttribute("nDoctors")), "/doctor"},
      {"Appointments", String.valueOf(request.getAttribute("nAppointments")), "/appointment"},
      {"Pending bills", String.valueOf(request.getAttribute("nPending")), "/billing"},
      {"Reviews", String.valueOf(request.getAttribute("nReviews")), "/review?action=moderate"}}; %>
  <% for (String[] c : cards) { %>
  <div class="col-6 col-md-4 col-lg"><a href="<%=ctx + c[2]%>" class="text-decoration-none">
    <div class="card shadow-sm text-center"><div class="card-body"><div class="display-6"><%=c[1]%></div><div class="text-muted"><%=c[0]%></div></div></div></a></div>
  <% } %>
</div>
<% if (cu.hasPermission("ADMINS")) { %>
<a class="btn btn-outline-primary me-2" href="<%=ctx%>/admin?action=list">Manage admins</a>
<a class="btn btn-outline-secondary" href="<%=ctx%>/admin?action=logs">Activity log</a>
<% } %>
<%@ include file="/includes/footer.jsp" %>
