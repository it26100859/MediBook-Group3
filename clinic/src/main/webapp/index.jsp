<%@ include file="/includes/header.jsp" %>
<div class="p-5 mb-4 bg-white rounded-3 shadow-sm">
  <h1 class="display-6">Medical Appointment Scheduling</h1>
  <p class="lead">Find a doctor, book a time slot, pay your bill and leave feedback - all in one place.</p>
  <% if (cu == null) { %>
    <a class="btn btn-primary me-2" href="<%=ctx%>/patient?action=register">Register as a patient</a>
    <a class="btn btn-outline-primary" href="<%=ctx%>/login">Login</a>
  <% } else if (isPatient) { %>
    <a class="btn btn-primary me-2" href="<%=ctx%>/appointment?action=book">Book an appointment</a>
    <a class="btn btn-outline-primary me-2" href="<%=ctx%>/appointment">My appointments</a>
    <a class="btn btn-outline-primary" href="<%=ctx%>/billing">My bills</a>
  <% } else { %>
    <a class="btn btn-primary" href="<%=ctx%>/admin">Go to admin dashboard</a>
  <% } %>
</div>
<%@ include file="/includes/footer.jsp" %>
