<%@ page import="java.util.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<String[]> logs = (List<String[]>) request.getAttribute("logs"); %>
<h3 class="mb-3">Admin Activity Log</h3>
<table class="table table-sm table-striped bg-white">
  <thead><tr><th>Time</th><th>Admin</th><th>Action</th></tr></thead>
  <tbody>
  <% for (String[] l : logs) { %><tr><td><%=Util.esc(l[0])%></td><td><%=Util.esc(l[1])%></td><td><%=Util.esc(l.length > 2 ? l[2] : "")%></td></tr><% }
     if (logs.isEmpty()) { %><tr><td colspan="3" class="text-center text-muted">No activity recorded yet.</td></tr><% } %>
  </tbody>
</table>
<a class="btn btn-link" href="<%=ctx%>/admin?action=list">Back</a>
<%@ include file="/includes/footer.jsp" %>
