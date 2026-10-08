<%@ include file="/includes/header.jsp" %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3">Patient Registration</h3>
    <form method="post" action="<%=ctx%>/patient">
      <input type="hidden" name="action" value="create">
      <div class="mb-3"><label class="form-label">Full name</label><input name="fullName" class="form-control" required></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Username</label><input name="username" class="form-control" required></div>
        <div class="col-md-6 mb-3"><label class="form-label">Password (6+ chars)</label><input type="password" name="password" minlength="6" class="form-control" required></div>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control"></div>
        <div class="col-md-6 mb-3"><label class="form-label">Phone</label><input name="phone" class="form-control"></div>
      </div>
      <div class="mb-3"><label class="form-label">Insurance number <span class="text-muted">(optional - makes you an Insured patient)</span></label><input name="insuranceNo" class="form-control"></div>
      <button class="btn btn-primary">Register</button>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
