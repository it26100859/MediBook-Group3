<%@ page import="java.util.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<String[]> rows = (List<String[]>) request.getAttribute("billable"); %>
<h3 class="mb-1">Generate Bill</h3>
<p class="text-muted">Completed appointments (consultation fee) and cancelled appointments with a cancellation fee that have no bill yet. Insured-patient discounts are applied automatically.</p>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>Appt</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Status</th><th>Amount</th><th></th></tr></thead>
  <tbody>
  <% for (String[] r : rows) { %>
    <tr><td><%=r[0]%></td><td><%=Util.esc(r[1])%></td><td><%=Util.esc(r[2])%></td><td><%=r[3]%></td><td><%=r[4]%></td><td><%=r[5]%></td>
        <td class="text-end"><form method="post" action="<%=ctx%>/billing" class="d-inline">
          <input type="hidden" name="action" value="generate"><input type="hidden" name="appointmentId" value="<%=r[0]%>"><button class="btn btn-sm btn-primary">Generate</button></form></td></tr>
  <% } if (rows.isEmpty()) { %><tr><td colspan="7" class="text-center text-muted">Nothing to bill right now.</td></tr><% } %>
  </tbody>
</table>
<a class="btn btn-link" href="<%=ctx%>/billing">Back to bills</a>
<%@ include file="/includes/footer.jsp" %>
