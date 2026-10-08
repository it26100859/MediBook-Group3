<%@ page import="com.clinic.doctor.*" %>
<%@ include file="/includes/header.jsp" %>
<% Doctor d = (Doctor) request.getAttribute("d"); boolean edit = d != null;
   String[] days = {"MON","TUE","WED","THU","FRI","SAT","SUN"}; %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3"><%=edit ? "Edit Doctor" : "Add Doctor"%></h3>
    <form method="post" action="<%=ctx%>/doctor">
      <input type="hidden" name="action" value="<%=edit ? "update" : "create"%>">
      <% if (edit) { %><input type="hidden" name="id" value="<%=d.getId()%>"><% } %>
      <div class="mb-3"><label class="form-label">Name</label><input name="name" value="<%=edit ? Util.esc(d.getName()) : ""%>" class="form-control" required></div>
      <div class="mb-3"><label class="form-label">Specialization</label><input name="specialization" value="<%=edit ? Util.esc(d.getSpecialization()) : ""%>" class="form-control" required></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Doctor type</label>
          <select name="type" class="form-select"><option value="GP" <%=edit && "GP".equals(d.getType()) ? "selected" : ""%>>General Practitioner</option><option value="SPECIALIST" <%=edit && "SPECIALIST".equals(d.getType()) ? "selected" : ""%>>Specialist (fee x1.5)</option></select></div>
        <div class="col-md-6 mb-3"><label class="form-label">Base fee (Rs.)</label><input type="number" step="0.01" min="1" name="baseFee" value="<%=edit ? d.getBaseFee() : ""%>" class="form-control" required></div>
      </div>
      <div class="mb-3"><label class="form-label d-block">Available days</label>
        <% for (String day : days) { %><div class="form-check form-check-inline"><input class="form-check-input" type="checkbox" name="days" value="<%=day%>" id="d<%=day%>" <%=edit && d.getAvailability().contains(day) ? "checked" : ""%>><label class="form-check-label" for="d<%=day%>"><%=day%></label></div><% } %>
      </div>
      <button class="btn btn-primary">Save</button> <a class="btn btn-link" href="<%=ctx%>/doctor">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
