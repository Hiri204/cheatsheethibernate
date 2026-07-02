<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Report Management | DevSheets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    
    <style>
        body { background-color: #f8f9fe; font-family: 'Segoe UI', sans-serif; }
        .admin-wrapper { padding: 40px 20px; }
        .table-card { border: none; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); background: #ffffff; }
        .table th { background-color: #f6f9fc; color: #8898aa; font-size: 0.85rem; font-weight: 600; text-transform: uppercase; padding: 15px; }
        .table td { vertical-align: middle; padding: 15px; color: #525f7f; font-size: 0.9rem; }
        .badge-status { font-size: 0.75rem; font-weight: 700; padding: 6px 12px; border-radius: 30px; }
        .btn-action { border-radius: 20px; font-weight: 600; font-size: 0.8rem; padding: 5px 12px; }
        .sheet-link { color: #5e72e4; text-decoration: none; font-weight: 600; }
        .sheet-link:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="container admin-wrapper">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h4 class="fw-bold text-dark mb-1"><i class="fa-solid fa-shield-halved text-primary me-2"></i>User Reports Portal</h4>
            <p class="text-muted small mb-0">User များမှ တိုင်ကြားထားသော Report များကို စီမံခန့်ခွဲသည့် နေရာဖြစ်ပါသည်။</p>
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary btn-sm rounded-pill px-3 fw-semibold">
            <i class="fa-solid fa-arrow-left me-1"></i> Back to Dashboard
        </a>
    </div>

    <div class="card table-card">
        <div class="card-body p-0 overflow-hidden">
            <div class="table-responsive">
                <table class="table table-hover align-items-center mb-0">
                    <thead>
                        <tr>
                            <th>Report ID</th>
                            <th>Sheet Info (Click to View Category)</th>
                            <th>Reporter Name</th>
                            <th>Reason</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="report" items="${reports}">
                            <tr>
                                <td class="fw-bold">#${report.reportId}</td>
                                
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty report.cheatsheet}">
                                            <a href="${pageContext.request.contextPath}/cheatsheets/category/${report.cheatsheet.category.name}" class="sheet-link" target="_blank">
                                                <i class="fa-regular fa-file-code me-1"></i> <c:out value="${report.cheatsheet.title}"/>
                                            </a>
                                            <small class="text-muted d-block">
                                                ID: #${report.cheatsheetId} | Category: <span class="badge bg-light text-dark border"><c:out value="${report.cheatsheet.category.name}"/></span>
                                            </small>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Sheet ID: #${report.cheatsheetId}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                
                                <td class="fw-semibold">
                                    <i class="fa-regular fa-user text-muted me-1"></i>
                                    <c:choose>
                                        <c:when test="${not empty report.user}">
                                            <c:out value="${report.user.username}"/>
                                        </c:when>
                                        <c:otherwise>
                                            User ID: #${report.reporterId}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                
                                <td style="max-width: 250px; white-space: normal; word-break: break-all;">
                                    <c:out value="${report.reason}"/>
                                </td>
                                
                                <td>
                                    <c:choose>
                                        <c:when test="${report.status eq 'pending'}">
                                            <span class="badge badge-status bg-warning-subtle text-warning text-uppercase">Pending</span>
                                        </c:when>
                                        <c:when test="${report.status eq 'resolved' or report.status eq 'approved'}">
                                            <span class="badge badge-status bg-danger-subtle text-danger text-uppercase">Banned</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-status bg-secondary-subtle text-secondary text-uppercase"><c:out value="${report.status}"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                
                                <td class="text-center">
                                    <c:if test="${report.status eq 'pending'}">
                                        <div class="d-flex justify-content-center gap-2">
                                            <a href="${pageContext.request.contextPath}/reports/admin/action/${report.reportId}?status=resolved" 
                                               class="btn btn-danger btn-action btn-sm">
                                                <i class="fa-solid fa-ban me-1"></i> Ban Post
                                            </a>
                                            <a href="${pageContext.request.contextPath}/reports/admin/action/${report.reportId}?status=cancelled" 
                                               class="btn btn-outline-secondary btn-action btn-sm">
                                                <i class="fa-solid fa-xmark me-1"></i> Dismiss
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${report.status ne 'pending'}">
                                        <span class="text-muted small"><i class="fa-solid fa-lock me-1"></i> Processed</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty reports}">
                            <tr>
                                <td colspan="6" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-clipboard-check fa-2x mb-3 text-light"></i>
                                    <h5>မည်သည့် တိုင်ကြားချက်မျှ မရှိသေးပါဗျာ။</h5>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>