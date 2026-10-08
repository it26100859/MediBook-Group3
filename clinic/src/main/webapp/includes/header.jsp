<%@ page contentType="text/html;charset=UTF-8" import="com.clinic.common.*" %>
<%
    User cu = (User) session.getAttribute("user");
    String ctx = request.getContextPath();
    boolean isAdmin = cu != null && "ADMIN".equals(cu.getRole());
    boolean isPatient = cu != null && "PATIENT".equals(cu.getRole());
    String flash = (String) session.getAttribute("flash");
    session.removeAttribute("flash");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>MediBook - Medical Appointment Scheduling</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container">
    <a class="navbar-brand" href="<%=ctx%>/index.jsp">MediBook</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav"><span class="navbar-toggler-icon"></span></button>
    <div class="collapse navbar-collapse" id="nav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/doctor">Doctors</a></li>
        <% if (cu != null) { %>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/appointment">Appointments</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/billing">Billing</a></li>
        <% } %>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/review">Reviews</a></li>
        <% if (isAdmin) { %>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin">Dashboard</a></li>
        <% if (cu.hasPermission("PATIENTS")) { %><li class="nav-item"><a class="nav-link" href="<%=ctx%>/patient?action=list">Patients</a></li><% } %>
        <% if (cu.hasPermission("ADMINS")) { %><li class="nav-item"><a class="nav-link" href="<%=ctx%>/admin?action=list">Admins</a></li><% } %>
        <% if (cu.hasPermission("REVIEWS")) { %><li class="nav-item"><a class="nav-link" href="<%=ctx%>/review?action=moderate">Moderation</a></li><% } %>
        <% } %>
      </ul>
      <ul class="navbar-nav">
        <% if (cu == null) { %>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/login">Login</a></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/patient?action=register">Register</a></li>
        <% } else { %>
        <% if (isPatient) { %><li class="nav-item"><a class="nav-link" href="<%=ctx%>/patient">My Profile</a></li><% } %>
        <li class="nav-item"><span class="navbar-text me-2"><%=Util.esc(cu.getDisplayName())%></span></li>
        <li class="nav-item"><a class="nav-link" href="<%=ctx%>/logout">Logout</a></li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>
<div class="container py-4">
<% if (flash != null) { %><div class="alert alert-info alert-dismissible fade show"><%=Util.esc(flash)%><button type="button" class="btn-close" data-bs-dismiss="alert"></button></div><% } %>
