<%@ page import="java.util.*,com.clinic.doctor.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Doctor> docs = (List<Doctor>) request.getAttribute("doctors"); String q = (String) request.getAttribute("q"); %>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3 class="mb-0">Doctors</h3>
  <% if (isAdmin && cu.hasPermission("DOCTORS")) { %><a class="btn btn-primary" href="<%=ctx%>/doctor?action=form">+ Add Doctor</a><% } %>
</div>
<form class="row g-2 mb-3" method="get" action="<%=ctx%>/doctor">
  <div class="col-auto"><input name="q" value="<%=Util.esc(q)%>" class="form-control" placeholder="Name or specialization"></div>
  <div class="col-auto"><button class="btn btn-outline-secondary">Search</button></div>
</form>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>Doctor</th><th>Specialization</th><th>Type</th><th>Available</th><th>Fee</th><th></th></tr></thead>
  <tbody>
  <% for (Doctor d : docs) { %>
    <tr><td><%=Util.esc(d.display())%></td><td><%=Util.esc(d.getSpecialization())%></td><td><%=d.getType()%></td>
        <td><%=Util.esc(d.getAvailability())%></td><td><%=Util.money(d.consultationFee())%></td>
        <td class="text-end">
          <% if (isPatient) { %><a class="btn btn-sm btn-primary" href="<%=ctx%>/appointment?action=book&doctorId=<%=d.getId()%>">Book</a><% } %>
          <a class="btn btn-sm btn-outline-secondary" href="<%=ctx%>/review?doctorId=<%=d.getId()%>">Reviews</a>
          <% if (isAdmin && cu.hasPermission("DOCTORS")) { %>
          <a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/doctor?action=form&id=<%=d.getId()%>">Edit</a>
          <form method="post" action="<%=ctx%>/doctor" class="d-inline" onsubmit="return confirm('Delete this doctor?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=d.getId()%>">
            <button class="btn btn-sm btn-outline-danger">Delete</button></form>
          <% } %></td></tr>
  <% } if (docs.isEmpty()) { %><tr><td colspan="6" class="text-center text-muted">No doctors found.</td></tr><% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
