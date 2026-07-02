<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Report Management | DevSheets</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body {
	background-color: #f8f9fe;
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.admin-wrapper {
	padding: 40px 20px;
	max-width: 1400px;
	margin: 0 auto;
}

.table-card {
	border: none;
	border-radius: 16px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
	background: #ffffff;
	overflow: hidden;
}

.table-card .card-header {
	background: #f8f9fa;
	border-bottom: 1px solid #e9ecef;
	padding: 16px 24px;
	font-weight: 600;
}

.table th {
	background-color: #f6f9fc;
	color: #8898aa;
	font-size: 0.8rem;
	font-weight: 700;
	text-transform: uppercase;
	padding: 14px 16px;
	letter-spacing: 0.3px;
	border-bottom: 2px solid #e9ecef;
}

.table td {
	vertical-align: middle;
	padding: 14px 16px;
	color: #525f7f;
	font-size: 0.9rem;
}

.table tbody tr:hover {
	background-color: #f8f9fe;
}

.badge-status {
	font-size: 0.7rem;
	font-weight: 700;
	padding: 5px 12px;
	border-radius: 30px;
	letter-spacing: 0.5px;
}

.btn-action {
	border-radius: 20px;
	font-weight: 600;
	font-size: 0.75rem;
	padding: 5px 14px;
	transition: all 0.2s;
}

.btn-action:hover {
	transform: translateY(-1px);
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.sheet-link {
	color: #5e72e4;
	text-decoration: none;
	font-weight: 600;
}

.sheet-link:hover {
	text-decoration: underline;
	color: #3d4db7;
}

.alert-custom {
	border-radius: 16px;
	border: none;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
	animation: slideDown 0.5s ease-out;
}

@
keyframes slideDown {from { opacity:0;
	transform: translateY(-20px);
}

to {
	opacity: 1;
	transform: translateY(0);
}

}

/* Ban Modal Styles */
.ban-modal .modal-content {
	border-radius: 20px;
	border: none;
	box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.ban-modal .modal-header {
	background: linear-gradient(135deg, #dc3545 0%, #c62828 100%);
	color: white;
	border-radius: 20px 20px 0 0;
	padding: 20px 24px;
}

.ban-modal .modal-header .btn-close {
	filter: brightness(0) invert(1);
}

.ban-modal .modal-body {
	padding: 24px;
}

.ban-modal .modal-footer {
	padding: 16px 24px 24px;
	border-top: none;
}

.ban-reason-textarea {
	border-radius: 12px;
	border: 2px solid #e8e8e8;
	padding: 12px 16px;
	transition: all 0.2s;
	resize: vertical;
}

.ban-reason-textarea:focus {
	border-color: #dc3545;
	box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
}

.ban-duration-btn {
	padding: 8px 16px;
	border-radius: 10px;
	border: 2px solid #e8e8e8;
	background: transparent;
	transition: all 0.2s;
	font-weight: 500;
	font-size: 0.85rem;
	cursor: pointer;
}

.ban-duration-btn:hover {
	border-color: #dc3545;
	background: #fff5f5;
	transform: translateY(-1px);
}

.ban-duration-btn.active {
	border-color: #dc3545;
	background: #dc3545;
	color: white;
}

.ban-duration-btn.permanent {
	border-color: #dc3545;
	color: #dc3545;
}

.ban-duration-btn.permanent.active {
	background: #dc3545;
	color: white;
}

.banned-sheet {
	background: #fff5f5;
	border-left: 4px solid #dc3545;
}

.banned-sheet:hover {
	background: #ffebeb !important;
}

.stats-badge {
	font-size: 0.8rem;
	padding: 6px 14px;
	border-radius: 30px;
	font-weight: 600;
}

.truncate-text {
	max-width: 200px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	display: inline-block;
}

@media ( max-width : 768px) {
	.admin-wrapper {
		padding: 20px 10px;
	}
	.table td, .table th {
		padding: 10px 8px;
		font-size: 0.8rem;
	}
	.btn-action {
		font-size: 0.7rem;
		padding: 4px 10px;
	}
}
</style>
</head>
<body>

	<div class="container admin-wrapper">
		<!-- Alert Container for dynamic alerts -->
		<div id="alertContainer"
			style="position: sticky; top: 20px; z-index: 999;"></div>

		<!-- Display success/error messages from session -->
		<c:if test="${not empty successMsg}">
			<div
				class="alert alert-success alert-dismissible fade show shadow-sm mb-4 alert-custom"
				role="alert">
				<div class="d-flex align-items-center gap-2">
					<i class="fa-solid fa-circle-check text-success fs-5"></i> <span
						class="fw-semibold">${successMsg}</span>
				</div>
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
			<%
			session.removeAttribute("successMsg");
			%>
		</c:if>

		<c:if test="${not empty errorMsg}">
			<div
				class="alert alert-danger alert-dismissible fade show shadow-sm mb-4 alert-custom"
				role="alert">
				<div class="d-flex align-items-center gap-2">
					<i class="fa-solid fa-circle-exclamation text-danger fs-5"></i> <span
						class="fw-semibold">${errorMsg}</span>
				</div>
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
			<%
			session.removeAttribute("errorMsg");
			%>
		</c:if>

		<!-- Page Header -->
		<div
			class="d-flex flex-wrap justify-content-between align-items-center mb-4">
			<div>
				<h4 class="fw-bold text-dark mb-1">
					<i class="fa-solid fa-shield-halved text-primary me-2"></i> User
					Reports Portal
				</h4>
				<p class="text-muted small mb-0">
					<i class="fa-regular fa-clock me-1"></i> Manage and review
					user-reported cheat sheets
				</p>
				<div class="mt-2 d-flex flex-wrap gap-2">
					<span class="badge stats-badge bg-warning text-dark"> <i
						class="fa-regular fa-hourglass-half me-1"></i> Pending:
						${pendingCount != null ? pendingCount : 0}
					</span> <span class="badge stats-badge bg-success text-white"> <i
						class="fa-regular fa-circle-check me-1"></i> Resolved:
						${resolvedCount != null ? resolvedCount : 0}
					</span> <span class="badge stats-badge bg-danger text-white"> <i
						class="fa-solid fa-ban me-1"></i> Banned:
						${fn:length(bannedSheets)}
					</span> <span class="badge stats-badge bg-secondary text-white"> <i
						class="fa-regular fa-file-lines me-1"></i> Total:
						${fn:length(reports)}
					</span>
				</div>
			</div>
			<div class="d-flex gap-2 mt-2 mt-md-0">
				<a href="${pageContext.request.contextPath}/dashboard"
					class="btn btn-outline-secondary btn-sm rounded-pill px-3 fw-semibold">
					<i class="fa-solid fa-arrow-left me-1"></i> Dashboard
				</a>
				<button onclick="location.reload()"
					class="btn btn-outline-primary btn-sm rounded-pill px-3 fw-semibold">
					<i class="fa-solid fa-rotate me-1"></i> Refresh
				</button>
			</div>
		</div>

		<!-- Banned Sheets Section -->
		<c:if test="${not empty bannedSheets}">
			<div class="card table-card mb-4 border-danger">
				<div
					class="card-header bg-danger text-white d-flex justify-content-between align-items-center">
					<span> <i class="fa-solid fa-ban me-2"></i> Banned Cheat
						Sheets <span class="badge bg-light text-dark ms-2">${fn:length(bannedSheets)}</span>
					</span> <span class="badge bg-light text-danger"> <i
						class="fa-regular fa-clock me-1"></i> Requires Attention
					</span>
				</div>
				<div class="card-body p-0">
					<div class="table-responsive">
						<table class="table table-hover align-items-center mb-0">
							<thead>
								<tr>
									<th>Sheet Title</th>
									<th>Author</th>
									<th>Ban Reason</th>
									<th>Banned At</th>
									<th>Expires At</th>
									<th class="text-center">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="sheet" items="${bannedSheets}">
									<tr class="banned-sheet">
										<td><a
											href="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}"
											class="sheet-link" target="_blank"> <i
												class="fa-regular fa-file-code me-1"></i> <c:out
													value="${sheet.title}" />
										</a></td>
										<td><i class="fa-regular fa-user text-muted me-1"></i> <c:out
												value="${sheet.user != null ? sheet.user.username : 'Unknown'}" />
										</td>
										<td><span class="truncate-text"
											title="<c:out value='${sheet.banReason}'/>"> <c:out
													value="${sheet.banReason}" />
										</span></td>
										<td><c:if test="${not empty sheet.bannedAt}">
                                                ${sheet.bannedAt}
                                            </c:if> <c:if
												test="${empty sheet.bannedAt}">
												<span class="text-muted">N/A</span>
											</c:if></td>
										<td><c:choose>
												<c:when test="${empty sheet.banExpiresAt}">
													<span class="badge bg-danger">Permanent</span>
												</c:when>
												<c:otherwise>
                                                    ${sheet.banExpiresAt}
                                                    <c:if
														test="${sheet.banExpiresAt < now}">
														<span class="badge bg-success ms-1">Expired</span>
													</c:if>
													<c:if test="${sheet.banExpiresAt >= now}">
														<span class="badge bg-warning text-dark ms-1">Active</span>
													</c:if>
												</c:otherwise>
											</c:choose></td>
										<td class="text-center">
											<form
												action="${pageContext.request.contextPath}/reports/admin/unban/${sheet.id}"
												method="post" style="display: inline;">
												<button type="submit"
													class="btn btn-success btn-action btn-sm"
													onclick="return confirm('Are you sure you want to unban this sheet?')">
													<i class="fa-solid fa-unlock me-1"></i> Unban
												</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</c:if>

		<!-- Reports Table -->
		<div class="card table-card">
			<div
				class="card-header bg-white d-flex justify-content-between align-items-center">
				<span> <i class="fa-regular fa-flag me-2"></i> All Reports <span
					class="badge bg-secondary ms-2">${fn:length(reports)}</span>
				</span>
				<div>
					<select class="form-select form-select-sm" id="statusFilter"
						onchange="filterReports()"
						style="width: auto; display: inline-block; border-radius: 20px;">
						<option value="all">All Status</option>
						<option value="pending">Pending</option>
						<option value="reviewed">Reviewed</option>
						<option value="resolved">Resolved</option>
					</select>
				</div>
			</div>
			<div class="card-body p-0 overflow-hidden">
				<div class="table-responsive">
					<table class="table table-hover align-items-center mb-0"
						id="reportsTable">
						<thead>
							<tr>
								<th style="width: 5%;">ID</th>
								<th style="width: 20%;">Sheet Info</th>
								<th style="width: 12%;">Reported By</th>
								<th style="width: 20%;">Reason</th>
								<th style="width: 10%;">Status</th>
								<th style="width: 13%;">Date</th>
								<th style="width: 20%;" class="text-center">Actions</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="report" items="${reports}">
								<tr class="report-row" data-status="${report.status}">
									<td class="fw-bold">#${report.id}</td>

									<td><c:choose>
											<c:when test="${not empty report.cheatSheet}">
												<a
													href="${pageContext.request.contextPath}/cheatsheets/view/${report.cheatSheet.id}"
													class="sheet-link" target="_blank"> <i
													class="fa-regular fa-file-code me-1"></i> <c:out
														value="${report.cheatSheet.title}" />
												</a>
												<small class="text-muted d-block"> <i
													class="fa-regular fa-folder me-1"></i> <c:out
														value="${report.cheatSheet.category != null ? report.cheatSheet.category.name : 'Uncategorized'}" />
													<c:if test="${report.cheatSheet.status == 'banned'}">
														<span class="badge bg-danger ms-1">BANNED</span>
													</c:if>
												</small>
											</c:when>
											<c:otherwise>
												<span class="text-muted">Sheet not found</span>
											</c:otherwise>
										</c:choose></td>

									<td><i class="fa-regular fa-user text-muted me-1"></i> <c:choose>
											<c:when test="${not empty report.reportedBy}">
												<c:out value="${report.reportedBy.username}" />
											</c:when>
											<c:otherwise>Unknown User</c:otherwise>
										</c:choose></td>

									<td><span class="truncate-text"
										title="<c:out value='${report.reason}'/>"> <c:out
												value="${report.reason}" />
									</span></td>

									<td><c:choose>
											<c:when test="${report.status eq 'pending'}">
												<span class="badge badge-status bg-warning text-dark">
													<i class="fa-regular fa-hourglass me-1"></i> Pending
												</span>
											</c:when>
											<c:when test="${report.status eq 'resolved'}">
												<span class="badge badge-status bg-success text-white">
													<i class="fa-regular fa-circle-check me-1"></i> Resolved
												</span>
											</c:when>
											<c:when test="${report.status eq 'reviewed'}">
												<span class="badge badge-status bg-info text-white">
													<i class="fa-regular fa-eye me-1"></i> Reviewed
												</span>
											</c:when>
											<c:when test="${report.status eq 'rejected'}">
												<span class="badge badge-status bg-secondary text-white">
													<i class="fa-regular fa-circle-xmark me-1"></i> Rejected
												</span>
											</c:when>
											<c:otherwise>
												<span class="badge badge-status bg-secondary text-white">
													<c:out value="${report.status}" />
												</span>
											</c:otherwise>
										</c:choose></td>

									<td><c:if test="${not empty report.createdAt}">
                                            ${report.createdAt}
                                        </c:if> <c:if
											test="${empty report.createdAt}">
											<span class="text-muted">N/A</span>
										</c:if></td>

									<td class="text-center"><c:if
											test="${report.status eq 'pending'}">
											<div class="d-flex justify-content-center gap-1 flex-wrap">
												<a
													href="${pageContext.request.contextPath}/reports/admin/action/${report.id}?status=reviewed"
													class="btn btn-info btn-action btn-sm"> <i
													class="fa-regular fa-eye me-1"></i> Review
												</a> <a
													href="${pageContext.request.contextPath}/reports/admin/action/${report.id}?status=resolved"
													class="btn btn-success btn-action btn-sm"> <i
													class="fa-regular fa-circle-check me-1"></i> Resolve
												</a>
												<button type="button"
													class="btn btn-danger btn-action btn-sm"
													onclick="openBanModal(${report.id}, '${fn:escapeXml(report.cheatSheet.title)}')">
													<i class="fa-solid fa-ban me-1"></i> Ban
												</button>
											</div>
										</c:if> <c:if test="${report.status ne 'pending'}">
											<span class="text-muted small"> <i
												class="fa-regular fa-check-circle text-success me-1"></i>
												Processed
											</span>
											<c:if test="${not empty report.resolvedAt}">
												<br>
												<small class="text-muted"> ${report.resolvedAt} </small>
											</c:if>
										</c:if></td>
								</tr>
							</c:forEach>

							<c:if test="${empty reports}">
								<tr>
									<td colspan="7" class="text-center py-5 text-muted"><i
										class="fa-regular fa-clipboard fa-3x mb-3 d-block text-light"></i>
										<h5 class="fw-semibold">No reports found</h5>
										<p class="small">All reports will appear here once users
											submit them.</p></td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- Ban Modal -->
	<div class="modal fade ban-modal" id="banModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">
						<i class="fa-solid fa-ban me-2"></i> Ban Cheat Sheet
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="banForm" method="post">
					<div class="modal-body">
						<p class="text-muted">
							<i class="fa-regular fa-file-code me-1"></i> Banning: <strong
								id="banSheetTitle" class="text-dark"></strong>
						</p>
						<input type="hidden" id="banReportId" name="reportId" value="" />

						<div class="mb-3">
							<label class="form-label fw-semibold" for="banReason"> <i
								class="fa-regular fa-pen-to-square me-1"></i> Ban Reason
							</label>
							<textarea class="form-control ban-reason-textarea" id="banReason"
								name="banReason" rows="3"
								placeholder="Enter reason for banning this sheet..." required></textarea>
							<div class="form-text">
								<i class="fa-regular fa-circle-info me-1"></i> Provide a clear
								reason for the ban (min 10 characters)
							</div>
						</div>

						<div class="mb-3">
							<label class="form-label fw-semibold"> <i
								class="fa-regular fa-clock me-1"></i> Ban Duration
							</label>
							<div class="d-flex flex-wrap gap-2">
								<button type="button" class="ban-duration-btn"
									data-duration="1day">1 Day</button>
								<button type="button" class="ban-duration-btn"
									data-duration="3days">3 Days</button>
								<button type="button" class="ban-duration-btn"
									data-duration="7days">7 Days</button>
								<button type="button" class="ban-duration-btn"
									data-duration="30days">30 Days</button>
								<button type="button" class="ban-duration-btn permanent active"
									data-duration="permanent">Permanent</button>
							</div>
							<input type="hidden" id="banDuration" name="banDuration"
								value="permanent" />
						</div>

						<div class="alert alert-info small">
							<i class="fa-regular fa-circle-info me-1"></i> <strong>Note:</strong>
							The sheet will be hidden from regular users. Admins can still
							view and unban it at any time.
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">
							<i class="fa-regular fa-xmark me-1"></i> Cancel
						</button>
						<button type="submit" class="btn btn-danger">
							<i class="fa-solid fa-ban me-1"></i> Ban Sheet
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js">
    </script>
	<!-- Fallback if cdnjs fails -->
	<script>
        if (typeof bootstrap === 'undefined') {
            document.write('<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"><\/script>');
        }
    </script>

	<script>
        /**
         * Show a dynamic alert message
         */
        function showAlert(type, message) {
            var container = document.getElementById('alertContainer');
            if (!container) {
                container = document.createElement('div');
                container.id = 'alertContainer';
                container.style.position = 'sticky';
                container.style.top = '20px';
                container.style.zIndex = '999';
                document.querySelector('.container').prepend(container);
            }

            var iconClass = type === 'success' ? 'fa-circle-check text-success' : 'fa-circle-exclamation text-danger';
            var alertClass = type === 'success' ? 'alert-success' : 'alert-danger';

            var alertHtml = '<div class="alert ' + alertClass + ' alert-dismissible fade show border-0 shadow-sm rounded-4 mb-4 alert-custom" role="alert">' +
                '<div class="d-flex align-items-center gap-2">' +
                '<i class="fa-solid ' + iconClass + ' fs-5"></i>' +
                '<span class="fw-semibold">' + message + '</span>' +
                '</div>' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                '</div>';

            container.innerHTML = alertHtml;

            // Auto dismiss after 5 seconds
            setTimeout(function() {
                var alert = container.querySelector('.alert');
                if (alert) {
                    var bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            }, 5000);
        }

        /**
         * Open the ban modal with report details
         */
        function openBanModal(reportId, sheetTitle) {
            // Check if Bootstrap is loaded
            if (typeof bootstrap === 'undefined') {
                console.error('Bootstrap is not loaded. Trying to load it...');
                var script = document.createElement('script');
                script.src = 'https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js';
                script.onload = function() {
                    openBanModal(reportId, sheetTitle);
                };
                document.head.appendChild(script);
                return;
            }

            var form = document.getElementById('banForm');
            form.action = '${pageContext.request.contextPath}/reports/admin/ban/' + reportId;

            document.getElementById('banReportId').value = reportId;
            document.getElementById('banSheetTitle').innerText = sheetTitle || 'Unknown Sheet';
            document.getElementById('banReason').value = '';
            document.getElementById('banDuration').value = 'permanent';

            // Reset duration buttons
            document.querySelectorAll('.ban-duration-btn').forEach(function(btn) {
                btn.classList.remove('active');
            });
            document.querySelector('.ban-duration-btn.permanent').classList.add('active');

            var modal = new bootstrap.Modal(document.getElementById('banModal'));
            modal.show();
        }

        /**
         * Filter reports by status
         */
        function filterReports() {
            var filter = document.getElementById('statusFilter').value;
            var rows = document.querySelectorAll('.report-row');

            rows.forEach(function(row) {
                if (filter === 'all' || row.getAttribute('data-status') === filter) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }

        /**
         * Validate ban form before submission
         */
        document.addEventListener('DOMContentLoaded', function() {
            // Set up duration buttons
            document.querySelectorAll('.ban-duration-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    document.querySelectorAll('.ban-duration-btn').forEach(function(b) {
                        b.classList.remove('active');
                    });
                    this.classList.add('active');
                    document.getElementById('banDuration').value = this.getAttribute('data-duration');
                });
            });

            // Validate ban form
            document.getElementById('banForm').addEventListener('submit', function(e) {
                var reason = document.getElementById('banReason').value.trim();
                if (reason.length < 10) {
                    e.preventDefault();
                    showAlert('danger', 'Please provide a detailed reason (at least 10 characters)');
                    return false;
                }
                if (reason.length > 500) {
                    e.preventDefault();
                    showAlert('danger', 'Reason cannot exceed 500 characters');
                    return false;
                }
                return true;
            });

            // Keyboard shortcut: ESC to close modal
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Escape') {
                    var modal = document.getElementById('banModal');
                    if (modal && modal.classList.contains('show')) {
                        var bsModal = bootstrap.Modal.getInstance(modal);
                        if (bsModal) {
                            bsModal.hide();
                        }
                    }
                }
            });

            console.log('Report management panel initialized successfully');
        });
    </script>
</body>
</html>