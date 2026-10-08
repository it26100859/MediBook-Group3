<%@ page import="java.util.*,com.clinic.review.*,com.clinic.doctor.*" %>
<%@ include file="/includes/header.jsp" %>
<% List<Doctor> docs = (List<Doctor>) request.getAttribute("doctors"); String sel = (String) request.getAttribute("selected");
   Review r = (Review) request.getAttribute("r"); boolean edit = r != null; %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3"><%=edit ? "Edit Review" : "Write a Review"%></h3>
    <form method="post" action="<%=ctx%>/review">
      <input type="hidden" name="action" value="<%=edit ? "update" : "create"%>">
      <% if (edit) { %><input type="hidden" name="id" value="<%=r.getId()%>"><% } %>
      <div class="mb-3"><label class="form-label">Doctor</label>
        <select name="doctorId" class="form-select" <%=edit ? "disabled" : "required"%>>
          <option value="">-- choose --</option>
          <% for (Doctor d : docs) { %><option value="<%=d.getId()%>" <%=d.getId().equals(sel) ? "selected" : ""%>><%=Util.esc(d.display())%></option><% } %>
        </select>
        <% if (edit) { %><input type="hidden" name="doctorId" value="<%=r.getDoctorId()%>"><% } %></div>
      <div class="mb-3"><label class="form-label">Rating</label>
        <select name="rating" class="form-select"><% for (int i = 5; i >= 1; i--) { %><option value="<%=i%>" <%=edit && r.getRating() == i ? "selected" : ""%>><%=i%> star<%=i > 1 ? "s" : ""%></option><% } %></select></div>
      <div class="mb-3"><label class="form-label">Comment</label><textarea name="comment" class="form-control" rows="4" required><%=edit ? Util.esc(r.getComment()) : ""%></textarea></div>
      <button class="btn btn-primary">Submit</button> <a class="btn btn-link" href="<%=ctx%>/review">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
