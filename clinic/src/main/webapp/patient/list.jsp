<%@ page import="java.util.*,com.clinic.patient.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Patient> ps = (List<Patient>) request.getAttribute("patients"); String q = (String) request.getAttribute("q"); %>
<h3>Patients</h3>
<form class="row g-2 mb-3" method="get" action="<%=ctx%>/patient">
  <input type="hidden" name="action" value="list">
  <div class="col-auto"><input name="q" value="<%=Util.esc(q)%>" class="form-control" placeholder="Search by ID, username or name"></div>
  <div class="col-auto"><button class="btn btn-outline-secondary">Search</button></div>
</form>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>ID</th><th>Name</th><th>Username</th><th>Email</th><th>Phone</th><th>Type</th><th></th></tr></thead>
  <tbody>
  <% for (Patient p : ps) { %>
    <tr><td><%=p.getId()%></td><td><%=Util.esc(p.getFullName())%></td><td><%=Util.esc(p.getUsername())%></td>
        <td><%=Util.esc(p.getEmail())%></td><td><%=Util.esc(p.getPhone())%></td><td><%=p.getType()%></td>
        <td class="text-end">
          <a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/patient?action=edit&id=<%=p.getId()%>">Edit</a>
          <form method="post" action="<%=ctx%>/patient" class="d-inline" onsubmit="return confirm('Delete this patient?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=p.getId()%>">
            <button class="btn btn-sm btn-outline-danger">Delete</button></form></td></tr>
  <% } if (ps.isEmpty()) { %><tr><td colspan="7" class="text-center text-muted">No patients found.</td></tr><% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
