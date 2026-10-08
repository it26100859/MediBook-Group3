<%@ page import="java.util.*,com.clinic.review.*,com.clinic.doctor.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Review> reviews = (List<Review>) request.getAttribute("reviews"); %>
<h3 class="mb-3">Review Moderation</h3>
<table class="table table-striped align-middle bg-white">
  <thead><tr><th>ID</th><th>Doctor</th><th>Rating</th><th>Review</th><th>Date</th><th></th></tr></thead>
  <tbody>
  <% for (Review r : reviews) { Doctor d = DoctorDAO.find(r.getDoctorId()); %>
    <tr><td><%=r.getId()%></td><td><%=d == null ? "(removed)" : Util.esc(d.getName())%></td><td><%=r.getRating()%>/5</td>
        <td><%=Util.esc(r.displayFor(true))%></td><td><%=Util.esc(r.getDate())%></td>
        <td class="text-end"><form method="post" action="<%=ctx%>/review" class="d-inline" onsubmit="return confirm('Delete this review?')">
          <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=r.getId()%>"><button class="btn btn-sm btn-outline-danger">Delete</button></form></td></tr>
  <% } if (reviews.isEmpty()) { %><tr><td colspan="6" class="text-center text-muted">No reviews to moderate.</td></tr><% } %>
  </tbody>
</table>
<%@ include file="/includes/footer.jsp" %>
