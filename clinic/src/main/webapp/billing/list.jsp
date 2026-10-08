<%@ page import="java.util.*,com.clinic.billing.*,com.clinic.patient.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Payment> bills = (List<Payment>) request.getAttribute("bills");
   boolean canManage = isAdmin && cu.hasPermission("BILLING"); %>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3 class="mb-0"><%=isAdmin ? "All Bills & Payments" : "My Bills"%></h3>
  <% if (canManage) { %><a class="btn btn-primary" href="<%=ctx%>/billing?action=generate">+ Generate Bill</a><% } %>
</div>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>Bill</th><th>Appt</th><% if (isAdmin) { %><th>Patient</th><% } %><th>Amount</th><th>Discount</th><th>Method</th><th>Total</th><th>Status</th><th>Date</th><th></th></tr></thead>
  <tbody>
  <% for (Payment b : bills) { Patient p = isAdmin ? PatientDAO.find(b.getPatientId()) : null; boolean pending = "PENDING".equals(b.getStatus()); %>
    <tr><td><%=b.getId()%></td><td><%=b.getAppointmentId()%></td>
        <% if (isAdmin) { %><td><%=p == null ? "(removed)" : Util.esc(p.getFullName())%></td><% } %>
        <td><%=Util.money(b.getBaseAmount())%></td><td><%=Util.money(b.getDiscount())%></td><td><%=b.getMethod()%></td>
        <td><strong><%=Util.money(b.getTotal())%></strong></td>
        <td><span class="badge text-bg-<%=pending ? "warning" : "success"%>"><%=b.getStatus()%></span></td><td><%=Util.esc(b.getDate())%></td>
        <td class="text-end">
          <% if (pending && isPatient) { %><a class="btn btn-sm btn-primary" href="<%=ctx%>/billing?action=pay&id=<%=b.getId()%>">Pay</a><% } %>
          <% if (pending && canManage) { %><a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/billing?action=edit&id=<%=b.getId()%>">Discount</a><% } %>
          <% if (!pending && canManage) { %>
          <form method="post" action="<%=ctx%>/billing" class="d-inline" onsubmit="return confirm('Remove this settled bill?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=b.getId()%>"><button class="btn btn-sm btn-outline-danger">Delete</button></form>
          <% } %></td></tr>
  <% } if (bills.isEmpty()) { %><tr><td colspan="10" class="text-center text-muted">No bills yet.</td></tr><% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
