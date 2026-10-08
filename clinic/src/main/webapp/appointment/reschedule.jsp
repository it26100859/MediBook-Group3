<%@ page import="java.time.LocalDate,com.clinic.doctor.*,com.clinic.appointment.*" %>
<%@ include file="/includes/header.jsp" %>
<% Appointment a = (Appointment) request.getAttribute("a"); Doctor d = (Doctor) request.getAttribute("doctor"); %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-1">Reschedule <%=a.getId()%></h3>
    <p class="text-muted"><%=d == null ? "(doctor removed)" : Util.esc(d.display()) + " - works " + Util.esc(d.getAvailability())%></p>
    <form method="post" action="<%=ctx%>/appointment">
      <input type="hidden" name="action" value="reschedule"><input type="hidden" name="id" value="<%=a.getId()%>">
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">New date</label><input type="date" name="date" value="<%=a.getDate()%>" min="<%=LocalDate.now()%>" class="form-control" required></div>
        <div class="col-md-6 mb-3"><label class="form-label">New time</label>
          <select name="time" class="form-select"><% for (String t : AppointmentService.SLOTS) { %><option <%=t.equals(a.getTime()) ? "selected" : ""%>><%=t%></option><% } %></select></div>
      </div>
      <button class="btn btn-primary">Save</button> <a class="btn btn-link" href="<%=ctx%>/appointment">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
