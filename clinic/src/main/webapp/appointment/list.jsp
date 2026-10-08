<%@ page import="java.util.*,com.clinic.appointment.*,com.clinic.doctor.*,com.clinic.patient.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Appointment> list = (List<Appointment>) request.getAttribute("appointments");
   boolean canManage = isAdmin && cu.hasPermission("APPOINTMENTS"); %>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3 class="mb-0"><%=isAdmin ? "All Appointments" : "My Appointments"%></h3>
  <% if (isPatient) { %><a class="btn btn-primary" href="<%=ctx%>/appointment?action=book">+ Book Appointment</a><% } %>
</div>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>ID</th><% if (isAdmin) { %><th>Patient</th><% } %><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th><th>Cancel fee</th><th></th></tr></thead>
  <tbody>
  <% for (Appointment a : list) {
       Doctor d = DoctorDAO.find(a.getDoctorId()); Patient p = PatientDAO.find(a.getPatientId());
       String badge = "BOOKED".equals(a.getStatus()) ? "primary" : "COMPLETED".equals(a.getStatus()) ? "success" : "secondary"; %>
    <tr><td><%=a.getId()%></td>
        <% if (isAdmin) { %><td><%=p == null ? "(removed)" : Util.esc(p.getFullName())%></td><% } %>
        <td><%=d == null ? "(removed)" : Util.esc(d.getName())%></td><td><%=a.getDate()%></td><td><%=a.getTime()%></td>
        <td><span class="badge text-bg-<%=badge%>"><%=a.getStatus()%></span></td>
        <td><%=a.getCancelFee() > 0 ? Util.money(a.getCancelFee()) : "-"%></td>
        <td class="text-end">
          <% if ("BOOKED".equals(a.getStatus())) { %>
            <% if (isPatient) { %><a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/appointment?action=reschedule&id=<%=a.getId()%>">Reschedule</a><% } %>
            <form method="post" action="<%=ctx%>/appointment" class="d-inline" onsubmit="return confirm('Cancel this appointment? A cancellation fee may apply.')">
              <input type="hidden" name="action" value="cancel"><input type="hidden" name="id" value="<%=a.getId()%>"><button class="btn btn-sm btn-outline-warning">Cancel</button></form>
            <% if (canManage) { %>
            <form method="post" action="<%=ctx%>/appointment" class="d-inline">
              <input type="hidden" name="action" value="complete"><input type="hidden" name="id" value="<%=a.getId()%>"><button class="btn btn-sm btn-outline-success">Complete</button></form>
            <% } %>
          <% } else if (canManage) { %>
            <form method="post" action="<%=ctx%>/appointment" class="d-inline" onsubmit="return confirm('Remove this record?')">
              <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=a.getId()%>"><button class="btn btn-sm btn-outline-danger">Delete</button></form>
          <% } %></td></tr>
  <% } if (list.isEmpty()) { %><tr><td colspan="8" class="text-center text-muted">No appointments yet.</td></tr><% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
