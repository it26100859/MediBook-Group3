<%@ page import="java.util.*,java.time.LocalDate,com.clinic.doctor.*,com.clinic.appointment.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Doctor> docs = (List<Doctor>) request.getAttribute("doctors"); String sel = (String) request.getAttribute("selected"); %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3">Book an Appointment</h3>
    <form method="post" action="<%=ctx%>/appointment">
      <input type="hidden" name="action" value="create">
      <div class="mb-3"><label class="form-label">Doctor</label>
        <select name="doctorId" class="form-select" required>
          <option value="">-- choose --</option>
          <% for (Doctor d : docs) { %><option value="<%=d.getId()%>" <%=d.getId().equals(sel) ? "selected" : ""%>><%=Util.esc(d.display())%> - <%=Util.esc(d.getAvailability())%> - <%=Util.money(d.consultationFee())%></option><% } %>
        </select></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Date</label><input type="date" name="date" min="<%=LocalDate.now()%>" class="form-control" required></div>
        <div class="col-md-6 mb-3"><label class="form-label">Time</label>
          <select name="time" class="form-select"><% for (String t : AppointmentService.SLOTS) { %><option><%=t%></option><% } %></select></div>
      </div>
      <div class="mb-3"><label class="form-label">Reason for visit</label><textarea name="reason" class="form-control" rows="2"></textarea></div>
      <button class="btn btn-primary">Book</button> <a class="btn btn-link" href="<%=ctx%>/appointment">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
