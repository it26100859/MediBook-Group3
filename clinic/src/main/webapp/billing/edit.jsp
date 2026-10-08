<%@ page import="com.clinic.billing.*" %>
<%@ include file="/includes/header.jsp" %>
<% Payment b = (Payment) request.getAttribute("b"); %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3">Adjust Discount - Bill <%=b.getId()%></h3>
    <p>Bill amount: <%=Util.money(b.getBaseAmount())%></p>
    <form method="post" action="<%=ctx%>/billing">
      <input type="hidden" name="action" value="discount"><input type="hidden" name="id" value="<%=b.getId()%>">
      <div class="mb-3"><label class="form-label">Discount (Rs.)</label><input type="number" step="0.01" min="0" max="<%=b.getBaseAmount()%>" name="discount" value="<%=b.getDiscount()%>" class="form-control" required></div>
      <button class="btn btn-primary">Save</button> <a class="btn btn-link" href="<%=ctx%>/billing">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
