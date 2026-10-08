<%@ page import="java.util.*,com.clinic.review.*,com.clinic.doctor.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Doctor> docs = (List<Doctor>) request.getAttribute("doctors"); String did = (String) request.getAttribute("doctorId");
   List<Review> reviews = (List<Review>) request.getAttribute("reviews"); double avg = (Double) request.getAttribute("avg"); %>
<h3 class="mb-3">Doctor Reviews</h3>
<form class="row g-2 mb-3" method="get" action="<%=ctx%>/review">
  <div class="col-auto"><select name="doctorId" class="form-select" onchange="this.form.submit()">
    <option value="">-- choose a doctor --</option>
    <% for (Doctor d : docs) { %><option value="<%=d.getId()%>" <%=d.getId().equals(did) ? "selected" : ""%>><%=Util.esc(d.display())%></option><% } %>
  </select></div>
  <% if (isPatient && !did.isEmpty()) { %><div class="col-auto"><a class="btn btn-primary" href="<%=ctx%>/review?action=submit&doctorId=<%=did%>">Write a review</a></div><% } %>
</form>
<% if (!did.isEmpty()) { %>
  <p class="text-muted"><%=reviews.size()%> review(s)<%=reviews.isEmpty() ? "" : String.format(" - average %.1f / 5", avg)%></p>
  <% for (Review r : reviews) { boolean mine = cu != null && r.getPatientId().equals(cu.getId());
       boolean canDelete = mine || (isAdmin && cu.hasPermission("REVIEWS")); %>
    <div class="card mb-2"><div class="card-body">
      <div class="d-flex justify-content-between">
        <div><span class="text-warning"><%="\u2605".repeat(r.getRating()) + "\u2606".repeat(5 - r.getRating())%></span>
             <span class="badge text-bg-<%="VERIFIED".equals(r.getType()) ? "success" : "secondary"%> ms-2"><%=r.getType()%></span></div>
        <small class="text-muted"><%=Util.esc(r.getDate())%></small></div>
      <p class="mb-2 mt-1"><%=Util.esc(r.displayFor(isAdmin))%></p>
      <% if (mine) { %><a class="btn btn-sm btn-outline-primary" href="<%=ctx%>/review?action=edit&id=<%=r.getId()%>">Edit</a><% } %>
      <% if (canDelete) { %><form method="post" action="<%=ctx%>/review" class="d-inline" onsubmit="return confirm('Delete this review?')">
        <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%=r.getId()%>"><button class="btn btn-sm btn-outline-danger">Delete</button></form><% } %>
    </div></div>
  <% } %>
<% } %>
<%@ include file="/includes/footer.jsp" %>
