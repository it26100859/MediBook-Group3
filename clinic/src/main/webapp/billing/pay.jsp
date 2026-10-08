<%@ page import="com.clinic.billing.*" %>
<%@ include file="/includes/header.jsp" %>
<% Payment b = (Payment) request.getAttribute("b"); double net = b.getBaseAmount() - b.getDiscount(); %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card shadow-sm"><div class="card-body">
    <h3 class="mb-3">Pay Bill <%=b.getId()%></h3>
    <p class="mb-1">Amount: <%=Util.money(b.getBaseAmount())%></p>
    <p class="mb-1">Discount: <%=Util.money(b.getDiscount())%></p>
    <p class="mb-3"><strong>Due (cash): <%=Util.money(net)%></strong> &middot; Card adds a 2.5% fee (<%=Util.money(net * 1.025)%>)</p>
    <form method="post" action="<%=ctx%>/billing">
      <input type="hidden" name="action" value="pay"><input type="hidden" name="id" value="<%=b.getId()%>">
      <div class="mb-3"><label class="form-label">Payment method</label>
        <select name="method" class="form-select"><option value="CASH">Cash (pay at clinic)</option><option value="CARD">Card</option></select></div>
      <button class="btn btn-success">Confirm payment</button> <a class="btn btn-link" href="<%=ctx%>/billing">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="/includes/footer.jsp" %>
