<%@ include file="/includes/header.jsp" %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3">Login</h3>
    <form method="post" action="<%=ctx%>/login">
      <div class="mb-3"><label class="form-label">Username</label><input name="username" class="form-control" required></div>
      <div class="mb-3"><label class="form-label">Password</label><input type="password" name="password" class="form-control" required></div>
      <button class="btn btn-primary w-100">Login</button>
    </form>
    <p class="mt-3 mb-0 small">New patient? <a href="<%=ctx%>/patient?action=register">Register here</a></p>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
