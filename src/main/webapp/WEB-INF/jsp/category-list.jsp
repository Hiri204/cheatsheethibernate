<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes" />
<title>${categoryName}·DevSheets</title>

<!-- Bootstrap 5 CSS - Use multiple CDN sources for reliability -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<!-- Fallback CSS if cdnjs fails -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />

<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
/* Reset and base styles */
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	background: #f0f2f5;
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
		Helvetica, Arial, sans-serif;
	color: #1a1a1a;
	min-height: 100vh;
}

/* Main Layout */
.main-content {
	margin-left: 260px;
	padding: 40px 50px;
	min-height: 100vh;
	background: #f0f2f5;
}

/* Page Header */
.page-title {
	font-size: 1.8rem;
	font-weight: 700;
	letter-spacing: -0.5px;
	color: #1a1a1a;
}

/* ─── Modern Card Design ─── */
.sheet-card {
	background: #ffffff;
	border-radius: 20px;
	border: none;
	box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
	transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
	height: 100%;
	display: flex;
	flex-direction: column;
	overflow: hidden;
	position: relative;
}

.sheet-card:hover {
	transform: translateY(-6px);
	box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
}

.sheet-card.banned {
	background: #fff5f5;
	border: 1px solid #fcc;
}

.sheet-card.banned .card-title {
	color: #dc3545;
}

/* Card Image */
.sheet-card .card-img-top {
	height: 200px;
	object-fit: cover;
	background: #f5f5f5;
}

/* Card Body */
.card-body {
	padding: 20px 24px 16px;
	flex: 1;
	display: flex;
	flex-direction: column;
}

/* Card Footer */
.card-footer-custom {
	padding: 12px 24px 20px;
	background: transparent;
	border-top: 1px solid #f0f0f0;
	display: flex;
	justify-content: space-between;
	align-items: center;
	flex-wrap: wrap;
	gap: 8px;
}

/* Badge Styles */
.status-badge {
	font-size: 0.6rem;
	font-weight: 600;
	text-transform: uppercase;
	letter-spacing: 0.5px;
	padding: 3px 12px;
	border-radius: 20px;
}

.status-badge.published {
	background: #e8f5e9;
	color: #2e7d32;
}

.status-badge.draft {
	background: #f5f5f5;
	color: #757575;
}

.status-badge.archived {
	background: #fbe9e7;
	color: #c62828;
}

.status-badge.banned {
	background: #ffebee;
	color: #c62828;
}

.category-tag {
	font-size: 0.7rem;
	font-weight: 500;
	color: #555555;
	background: #f5f5f5;
	padding: 4px 14px;
	border-radius: 20px;
}

/* Title */
.card-title {
	font-size: 1.1rem;
	font-weight: 700;
	color: #1a1a1a;
	margin-bottom: 8px;
	line-height: 1.4;
}

/* Content Preview */
.content-preview {
	font-size: 0.9rem;
	color: #757575;
	line-height: 1.6;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
	margin-bottom: 12px;
}

/* Meta Info */
.meta-info {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	font-size: 0.75rem;
	color: #9c9c9c;
	margin-bottom: 12px;
}

.meta-info .author {
	color: #1a1a1a;
	font-weight: 600;
	cursor: pointer;
	text-decoration: none;
	transition: color 0.2s;
}

.meta-info .author:hover {
	color: #0d6efd;
	text-decoration: underline;
}

.meta-info i {
	width: 14px;
	color: #adadad;
}

/* Tags */
.tags-container {
	display: flex;
	flex-wrap: wrap;
	gap: 4px;
	margin-bottom: 12px;
}

.tag-badge {
	font-size: 0.65rem;
	font-weight: 500;
	color: #555555;
	background: #f0f0f0;
	padding: 2px 10px;
	border-radius: 12px;
	transition: all 0.2s;
}

.tag-badge:hover {
	background: #e0e0e0;
	color: #1a1a1a;
}

/* Interaction Buttons */
.interaction-buttons {
	display: flex;
	align-items: center;
	gap: 4px;
}

.btn-interaction {
	background: transparent;
	border: none;
	color: #9c9c9c;
	font-size: 0.85rem;
	padding: 6px 10px;
	border-radius: 8px;
	transition: all 0.2s;
	display: flex;
	align-items: center;
	gap: 4px;
}

.btn-interaction:hover {
	background: #f5f5f5;
	color: #1a1a1a;
}

.btn-interaction.liked {
	color: #e74c3c;
}

.btn-interaction.liked:hover {
	background: #fdf2f2;
}

.btn-interaction.bookmarked {
	color: #f39c12;
}

.btn-interaction.bookmarked:hover {
	background: #fdf8f0;
}

.btn-interaction .count {
	font-size: 0.7rem;
	font-weight: 500;
}

/* Action Buttons */
.btn-action-sm {
	padding: 6px 14px;
	border-radius: 20px;
	font-weight: 600;
	font-size: 0.75rem;
	transition: all 0.2s;
}

.btn-outline-custom {
	background: transparent;
	color: #555555;
	border: 1px solid #e8e8e8;
}

.btn-outline-custom:hover {
	background: #f5f5f5;
	color: #1a1a1a;
}

.btn-report-sm {
	background: transparent;
	color: #dc3545;
	border: 1px solid #fcc;
	font-size: 0.7rem;
	padding: 4px 12px;
	border-radius: 20px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-report-sm:hover {
	background: #fdf2f2;
	border-color: #dc3545;
}

.btn-primary-custom {
	background: #1a1a1a;
	color: #ffffff;
	border: none;
	border-radius: 20px;
	padding: 10px 24px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-primary-custom:hover {
	background: #333333;
	color: #ffffff;
}

/* Dropdown */
.dropdown-toggle-custom {
	background: transparent;
	border: none;
	color: #9c9c9c;
	padding: 4px 8px;
	border-radius: 6px;
	transition: all 0.2s;
}

.dropdown-toggle-custom:hover {
	background: #f5f5f5;
	color: #1a1a1a;
}

.dropdown-menu-custom {
	border-radius: 12px;
	border: 1px solid #f0f0f0;
	box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
	padding: 6px 0;
	min-width: 180px;
}

.dropdown-menu-custom .dropdown-item {
	padding: 8px 20px;
	font-size: 0.85rem;
	transition: all 0.2s;
	cursor: pointer;
}

.dropdown-menu-custom .dropdown-item:hover {
	background: #f5f5f5;
}

.dropdown-menu-custom .dropdown-item.text-danger:hover {
	background: #fdf2f2;
}

.dropdown-menu-custom .dropdown-divider {
	margin: 4px 0;
}

/* Report button in dropdown */
.dropdown-item-report {
	color: #dc3545 !important;
	font-weight: 600;
}

.dropdown-item-report:hover {
	background: #fdf2f2 !important;
	color: #b02a37 !important;
}

.dropdown-item-report i {
	color: #dc3545;
}

/* Line clamp */
.line-clamp-1 {
	display: -webkit-box;
	-webkit-line-clamp: 1;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

/* Empty state */
.empty-state {
	padding: 80px 20px;
	text-align: center;
}

.empty-state i {
	font-size: 4rem;
	color: #d0d0d0;
	margin-bottom: 20px;
}

.empty-state h5 {
	color: #757575;
	font-weight: 600;
}

/* Report Modal */
.report-reason-btn {
	padding: 8px 16px;
	border-radius: 10px;
	border: 1px solid #e8e8e8;
	background: transparent;
	transition: all 0.2s;
	width: 100%;
	text-align: left;
	font-size: 0.85rem;
}

.report-reason-btn:hover {
	background: #f5f5f5;
	border-color: #d0d0d0;
}

.report-reason-btn.active {
	background: #fdf2f2;
	border-color: #ff4d4f;
	color: #ff4d4f;
}

/* Alert animation */
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
.alert-custom .btn-close {
	padding: 1rem;
}

/* Responsive */
@media ( max-width : 992px) {
	.main-content {
		margin-left: 0;
		padding: 20px;
	}
}

@media ( max-width : 768px) {
	.main-content {
		padding: 15px;
	}
	.page-title {
		font-size: 1.2rem;
	}
	.card-title {
		font-size: 1rem;
	}
	.sheet-card .card-img-top {
		height: 150px;
	}
}

/* Loading spinner */
.loading-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(255, 255, 255, 0.8);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 9999;
	opacity: 0;
	pointer-events: none;
	transition: opacity 0.3s;
}

.loading-overlay.show {
	opacity: 1;
	pointer-events: all;
}

.spinner-custom {
	width: 50px;
	height: 50px;
	border: 4px solid #f0f0f0;
	border-top: 4px solid #1a1a1a;
	border-radius: 50%;
	animation: spin 0.8s linear infinite;
}

@
keyframes spin { 0% {
	transform: rotate(0deg);
}

100
%
{
transform
:
rotate(
360deg
);
}
}

/* Pagination styles */
.pagination .page-link {
	color: #1a1a1a;
	border-radius: 8px;
	margin: 0 3px;
	border: 1px solid #e8e8e8;
}

.pagination .page-link:hover {
	background-color: #f5f5f5;
	border-color: #d0d0d0;
}

.pagination .page-item.active .page-link {
	background: #1a1a1a;
	border-color: #1a1a1a;
	color: white;
}

.pagination .page-item.disabled .page-link {
	color: #ccc;
	pointer-events: none;
}
</style>
</head>
<body>

	<!-- Loading Overlay -->
	<div class="loading-overlay" id="loadingOverlay">
		<div class="spinner-custom"></div>
	</div>

	<!-- Include sidebar -->
	<jsp:include page="sidebar.jsp">
		<jsp:param name="activePage" value="home" />
	</jsp:include>

	<!-- Main Content -->
	<div class="main-content">

		<!-- Alert Container -->
		<div id="alertContainer"
			style="position: sticky; top: 20px; z-index: 999;"></div>

		<!-- Page Header -->
		<div
			class="d-flex flex-wrap justify-content-between align-items-center mb-4">
			<div>
				<h3 class="page-title">
					<i class="fa-regular fa-folder-open me-2"></i>${categoryName}
				</h3>
				<p class="text-muted small mt-1">
					<i class="fa-regular fa-file-lines me-1"></i> စုစုပေါင်း Cheat
					Sheet <span
						class="badge bg-secondary-subtle text-dark fw-bold px-2 rounded-pill">${totalItems}</span>
					စောင် ရှာဖွေတွေ့ရှိပါတယ်။
				</p>
			</div>
			<div class="d-flex gap-2 mt-2 mt-md-0">
				<a
					href="${pageContext.request.contextPath}/cheatsheets/new?prefCat=${categoryName}"
					class="btn btn-primary-custom"> <i
					class="fa-solid fa-plus me-2"></i>Add New Sheet
				</a>
				<button onclick="location.reload()"
					class="btn btn-outline-secondary rounded-pill px-3 fw-semibold">
					<i class="fa-solid fa-rotate me-1"></i> Refresh
				</button>
			</div>
		</div>

		<!-- Sheets Grid -->
		<div class="row g-4">
			<c:forEach items="${sheets}" var="sheet">
				<div class="col-md-6 col-lg-4">
					<div class="sheet-card ${sheet.status eq 'banned' ? 'banned' : ''}">

						<!-- ===== IMAGE - FIXED (Single instance) ===== -->
						<c:choose>
							<c:when test="${not empty sheet.fileUrl}">
								<img
									src="${pageContext.request.contextPath}/uploads/${sheet.fileUrl}"
									class="card-img-top" alt="${fn:escapeXml(sheet.title)}"
									onerror="this.style.display='none'" loading="lazy" />
							</c:when>
							<c:otherwise>
								<div
									class="card-img-top bg-light d-flex align-items-center justify-content-center"
									style="height: 200px;">
									<i class="fa-regular fa-file-image fa-3x text-muted"></i>
								</div>
							</c:otherwise>
						</c:choose>

						<!-- Card Body -->
						<div class="card-body">
							<!-- Header: Status + Category -->
							<div
								class="d-flex justify-content-between align-items-center mb-2">
								<span class="status-badge ${sheet.status}"> <i
									class="fa-regular fa-circle me-1"></i> ${sheet.status}
								</span> <span class="category-tag"> <i
									class="fa-regular fa-folder me-1"></i> ${sheet.category != null ? sheet.category.name : 'Uncategorized'}
								</span>
							</div>

							<!-- Title -->
							<h5 class="card-title line-clamp-1"
								title="${fn:escapeXml(sheet.title)}">
								${fn:escapeXml(sheet.title)}</h5>

							<!-- Content Preview - TEXT ONLY (Image already shown above) -->
							<p class="content-preview">
								<c:choose>
									<c:when test="${not empty sheet.content}">
										<c:set var="previewContent"
											value="${fn:substring(sheet.content, 0, 120)}" />
                                        ${fn:escapeXml(previewContent)}
                                        <c:if
											test="${fn:length(sheet.content) > 120}">...</c:if>
									</c:when>
									<c:otherwise>No content available</c:otherwise>
								</c:choose>
							</p>

							<!-- Tags -->
							<div class="tags-container">
								<c:forEach var="tag" items="${sheet.tags}">
									<span class="tag-badge"> <i
										class="fa-solid fa-tag fa-xs me-1"></i>${tag.name}
									</span>
								</c:forEach>
								<c:if test="${empty sheet.tags}">
									<span class="text-muted" style="font-size: 0.65rem;">No
										tags</span>
								</c:if>
							</div>

							<!-- Meta Info -->
							<div class="meta-info">
								<span> <i class="fa-regular fa-user"></i> <a
									href="${pageContext.request.contextPath}/user/profile/${sheet.user.userId}"
									class="author"> ${sheet.user != null ? sheet.user.username : 'Unknown'}
								</a>
								</span> <span> <i class="fa-regular fa-calendar"></i> <fmt:parseDate
										value="${sheet.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss"
										var="parsedDate" type="both" /> <fmt:formatDate
										value="${parsedDate}" pattern="dd MMM yyyy" />
								</span>
								<c:if
									test="${not empty sheet.updatedAt && sheet.updatedAt != sheet.createdAt}">
									<span> <i class="fa-regular fa-pen-to-square"></i> <fmt:parseDate
											value="${sheet.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss"
											var="parsedUpdated" type="both" /> <fmt:formatDate
											value="${parsedUpdated}" pattern="dd MMM" />
									</span>
								</c:if>
							</div>

							<!-- Interaction Buttons -->
							<div class="d-flex justify-content-between align-items-center">
								<div class="interaction-buttons">
									<button class="btn-interaction"
										onclick="toggleLike(${sheet.id}, this)">
										<i class="fa-regular fa-heart"></i> <span class="count">${sheet.likeCount != null ? sheet.likeCount : 0}</span>
									</button>
									<button class="btn-interaction"
										onclick="openComments(${sheet.id})">
										<i class="fa-regular fa-comment"></i> <span class="count">${sheet.commentCount != null ? sheet.commentCount : 0}</span>
									</button>
									<button class="btn-interaction"
										onclick="toggleBookmark(${sheet.id}, this)">
										<i class="fa-regular fa-bookmark"></i>
									</button>
								</div>

								<div class="dropdown">
									<button class="dropdown-toggle-custom" type="button"
										data-bs-toggle="dropdown" aria-expanded="false"
										title="More options">
										<i class="fa-solid fa-ellipsis-vertical"></i>
									</button>
									<ul
										class="dropdown-menu dropdown-menu-end dropdown-menu-custom">
										<li><a class="dropdown-item"
											href="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}">
												<i class="fa-regular fa-eye me-2"></i> View Details
										</a></li>
										<li><a class="dropdown-item"
											onclick="copyToClipboard('${fn:escapeXml(sheet.content)}', '${fn:escapeXml(sheet.title)}')">
												<i class="fa-regular fa-copy me-2"></i> Copy Content
										</a></li>
										<li><a class="dropdown-item"
											onclick="followAuthor(${sheet.user.userId})"> <i
												class="fa-regular fa-user-plus me-2"></i> Follow Author
										</a></li>
										<li><a class="dropdown-item"
											href="${pageContext.request.contextPath}/cheatsheets/rate/${sheet.id}">
												<i class="fa-regular fa-star me-2"></i> Rate this Sheet
										</a></li>
										<c:if test="${sheet.status ne 'banned'}">
											<li><hr class="dropdown-divider"></li>
											<li><a class="dropdown-item dropdown-item-report"
												onclick="openReportModal(${sheet.id}, '${fn:escapeXml(sheet.title)}')">
													<i class="fa-solid fa-flag me-2"></i> Report
											</a></li>
										</c:if>
									</ul>
								</div>
							</div>
						</div>

						<!-- Card Footer -->
						<div class="card-footer-custom">
							<div class="d-flex gap-2 w-100 flex-wrap">
								<a
									href="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}"
									class="btn btn-outline-custom btn-action-sm flex-grow-1 text-center">
									<i class="fa-regular fa-eye me-1"></i> View
								</a>
								<button type="button"
									onclick="copyToClipboard('${fn:escapeXml(sheet.content)}', '${fn:escapeXml(sheet.title)}')"
									class="btn btn-outline-custom btn-action-sm flex-grow-1 text-center">
									<i class="fa-regular fa-copy me-1"></i> Copy
								</button>
								<c:if test="${sheet.status ne 'banned'}">
									<button type="button"
										onclick="openReportModal(${sheet.id}, '${fn:escapeXml(sheet.title)}')"
										class="btn btn-report-sm" title="Report this sheet">
										<i class="fa-solid fa-flag me-1"></i> Report
									</button>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>

			<!-- Empty State -->
			<c:if test="${empty sheets}">
				<div class="col-12">
					<div class="empty-state">
						<i class="fa-regular fa-folder-open"></i>
						<h5>ဤ Category အောက်တွင် မည်သည့် Cheat Sheet မျှ မရှိသေးပါ။</h5>
						<p class="text-muted small">စတင်ရန် "Add New Sheet" ခလုတ်ကို
							နှိပ်ပါ။</p>
						<a
							href="${pageContext.request.contextPath}/cheatsheets/new?prefCat=${categoryName}"
							class="btn btn-primary-custom mt-3"> <i
							class="fa-solid fa-plus me-2"></i>Add Your First Sheet
						</a>
					</div>
				</div>
			</c:if>
		</div>

		<!-- ===== PAGINATION ===== -->
		<c:if test="${totalPages > 1}">
			<nav aria-label="Page navigation" class="mt-5">
				<ul class="pagination justify-content-center">
					<!-- Previous Page -->
					<li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a
						class="page-link" href="?page=${currentPage - 1}&size=${pageSize}"
						aria-label="Previous"> <i class="fa-solid fa-chevron-left"></i>
					</a></li>

					<!-- Page Numbers -->
					<c:forEach begin="1" end="${totalPages}" var="i">
						<li class="page-item ${currentPage == i ? 'active' : ''}"><a
							class="page-link" href="?page=${i}&size=${pageSize}">${i}</a></li>
					</c:forEach>

					<!-- Next Page -->
					<li
						class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
						<a class="page-link"
						href="?page=${currentPage + 1}&size=${pageSize}" aria-label="Next">
							<i class="fa-solid fa-chevron-right"></i>
					</a>
					</li>
				</ul>
			</nav>

			<!-- Page Info -->
			<div class="text-center text-muted small mt-2">Showing page
				${currentPage} of ${totalPages} (${totalItems} total items)</div>
		</c:if>
	</div>

	<!-- Report Modal -->
	<div class="modal fade" id="reportModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content border-0 shadow"
				style="border-radius: 20px;">
				<div class="modal-header border-0 pt-4 px-4">
					<h5 class="modal-title fw-bold text-danger">
						<i class="fa-solid fa-triangle-exclamation me-2"></i>Report Cheat
						Sheet
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="reportForm" onsubmit="submitReportAsync(event)">
					<div class="modal-body px-4 pb-3">
						<p class="text-muted small mb-3">
							<i class="fa-regular fa-file-code me-1"></i> စာရွက်ခေါင်းစဉ် - <strong
								class="text-dark" id="modalSheetTitle"></strong>
						</p>
						<input type="hidden" id="modalCheatsheetId" name="cheatsheetId"
							value="" />

						<div class="mb-3">
							<label class="form-label fw-semibold text-secondary small">
								<i class="fa-regular fa-circle-list me-1"></i> အကြောင်းအရင်း
								ရွေးချယ်ပါ
							</label>
							<div class="d-flex flex-wrap gap-2">
								<button type="button" class="report-reason-btn"
									data-reason="Inappropriate Content">
									<i class="fa-solid fa-ban me-2"></i>Inappropriate Content
								</button>
								<button type="button" class="report-reason-btn"
									data-reason="Spam">
									<i class="fa-solid fa-bug me-2"></i>Spam
								</button>
								<button type="button" class="report-reason-btn"
									data-reason="Copyright Infringement">
									<i class="fa-regular fa-copyright me-2"></i>Copyright
									Infringement
								</button>
								<button type="button" class="report-reason-btn"
									data-reason="Misleading Information">
									<i class="fa-solid fa-circle-exclamation me-2"></i>Misleading
									Information
								</button>
								<button type="button" class="report-reason-btn"
									data-reason="Hate Speech">
									<i class="fa-solid fa-people-arrows me-2"></i>Hate Speech
								</button>
								<button type="button" class="report-reason-btn"
									data-reason="Other">
									<i class="fa-solid fa-ellipsis me-2"></i>Other
								</button>
							</div>
						</div>

						<div class="mb-3">
							<label class="form-label fw-semibold text-secondary small">
								<i class="fa-regular fa-pen-to-square me-1"></i> အသေးစိတ်
								ဖော်ပြချက်
							</label>
							<textarea class="form-control rounded-3" id="reason"
								name="reason" rows="4"
								placeholder="ကျေးဇူးပြု၍ အကြောင်းအရင်းကို အသေးစိတ် ဖော်ပြပါ..."
								required></textarea>
							<div class="form-text">
								<i class="fa-regular fa-circle-info me-1"></i> အနည်းဆုံး ၁၀
								လုံးထက်ပို၍ ဖြည့်သွင်းပါ။
							</div>
						</div>
					</div>
					<div class="modal-footer border-0 pt-0 px-4 pb-4">
						<button type="button"
							class="btn btn-light rounded-pill px-4 btn-sm fw-semibold"
							data-bs-dismiss="modal">
							<i class="fa-regular fa-xmark me-1"></i>Cancel
						</button>
						<button type="submit"
							class="btn btn-danger rounded-pill px-4 btn-sm fw-semibold">
							<i class="fa-regular fa-paper-plane me-1"></i>Submit Report
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS - Use cdnjs with fallback -->
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
        // ============ ALL FUNCTIONS ============

        /**
         * Show loading overlay
         */
        function showLoading() {
            document.getElementById('loadingOverlay').classList.add('show');
        }

        /**
         * Hide loading overlay
         */
        function hideLoading() {
            document.getElementById('loadingOverlay').classList.remove('show');
        }

        /**
         * Like toggle with AJAX
         */
        function toggleLike(sheetId, element) {
            var icon = element.querySelector('i');
            var countSpan = element.querySelector('.count');
            var count = parseInt(countSpan.textContent) || 0;
            
            if (icon.classList.contains('fa-regular')) {
                icon.classList.remove('fa-regular');
                icon.classList.add('fa-solid');
                element.classList.add('liked');
                count++;
            } else {
                icon.classList.remove('fa-solid');
                icon.classList.add('fa-regular');
                element.classList.remove('liked');
                count--;
            }
            countSpan.textContent = count;
            
            fetch('${pageContext.request.contextPath}/cheatsheets/like/' + sheetId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.success) {
                    countSpan.textContent = data.likeCount;
                }
            })
            .catch(function(error) { console.error('Error:', error); });
        }

        /**
         * Bookmark toggle with AJAX
         */
        function toggleBookmark(sheetId, element) {
            var icon = element.querySelector('i');
            
            if (icon.classList.contains('fa-regular')) {
                icon.classList.remove('fa-regular');
                icon.classList.add('fa-solid');
                element.classList.add('bookmarked');
            } else {
                icon.classList.remove('fa-solid');
                icon.classList.add('fa-regular');
                element.classList.remove('bookmarked');
            }
            
            fetch('${pageContext.request.contextPath}/cheatsheets/bookmark/' + sheetId, {
                method: 'POST'
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.success) {
                    // Update bookmark state if needed
                }
            })
            .catch(function(error) { console.error('Error:', error); });
        }

        /**
         * Follow author with AJAX
         */
        function followAuthor(userId) {
            if (!userId) {
                showAlert('warning', 'User information not available.');
                return;
            }
            
            showLoading();
            
            fetch('${pageContext.request.contextPath}/user/follow/' + userId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(function(response) {
                hideLoading();
                if (response.ok) {
                    showAlert('success', '✅ Successfully followed the author!');
                } else {
                    showAlert('danger', '❌ Failed to follow the author.');
                }
            })
            .catch(function(error) {
                hideLoading();
                console.error('Error:', error);
                showAlert('danger', '❌ An error occurred.');
            });
        }

        /**
         * Open comments page
         */
        function openComments(sheetId) {
            window.location.href = '${pageContext.request.contextPath}/cheatsheets/comments/' + sheetId;
        }

        /**
         * Copy to Clipboard with modern and fallback methods
         */
        function copyToClipboard(content, title) {
            if (!content || content.trim() === '' || content === 'null') {
                showAlert('warning', '⚠️ ကူးယူရန် အကြောင်းအရာ (Content) မရှိပါဗျာ။');
                return;
            }
            
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(content).then(function() {
                    showAlert('success', '✅ "' + title + '" content copied to clipboard!');
                }).catch(function(err) {
                    console.error('Could not copy text: ', err);
                    copyToClipboardFallback(content, title);
                });
            } else {
                copyToClipboardFallback(content, title);
            }
        }

        /**
         * Fallback copy method
         */
        function copyToClipboardFallback(content, title) {
            var textarea = document.createElement('textarea');
            textarea.value = content;
            textarea.style.position = 'fixed';
            textarea.style.opacity = '0';
            document.body.appendChild(textarea);
            textarea.select();
            try {
                document.execCommand('copy');
                showAlert('success', '✅ "' + title + '" content copied to clipboard!');
            } catch (err) {
                showAlert('danger', '❌ Copy ကူးယူခြင်း မအောင်မြင်ပါဗျာ။');
            }
            document.body.removeChild(textarea);
        }

        /**
         * Show alert with auto-dismiss
         */
        function showAlert(type, message) {
            var container = document.getElementById('alertContainer');
            if (!container) {
                container = document.createElement('div');
                container.id = 'alertContainer';
                container.style.position = 'sticky';
                container.style.top = '20px';
                container.style.zIndex = '999';
                document.querySelector('.main-content').prepend(container);
            }
            
            var iconClass = '';
            if (type === 'success') {
                iconClass = 'fa-circle-check text-success';
            } else if (type === 'warning') {
                iconClass = 'fa-circle-exclamation text-warning';
            } else {
                iconClass = 'fa-circle-exclamation text-danger';
            }
            
            var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show border-0 shadow-sm rounded-4 mb-4 alert-custom" role="alert">' +
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
         * Open Report Modal
         */
        function openReportModal(sheetId, sheetTitle) {
            // Check if Bootstrap is loaded
            if (typeof bootstrap === 'undefined') {
                console.error('Bootstrap is not loaded. Trying to load it...');
                // Try to load Bootstrap dynamically
                var script = document.createElement('script');
                script.src = 'https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js';
                script.onload = function() {
                    // Retry opening modal after Bootstrap loads
                    openReportModal(sheetId, sheetTitle);
                };
                document.head.appendChild(script);
                return;
            }
            
            document.getElementById('modalCheatsheetId').value = sheetId;
            document.getElementById('modalSheetTitle').innerText = sheetTitle || 'Unknown Sheet';
            document.getElementById('reason').value = '';
            
            // Reset all reason buttons
            var reasonBtns = document.querySelectorAll('.report-reason-btn');
            for (var i = 0; i < reasonBtns.length; i++) {
                reasonBtns[i].classList.remove('active');
            }
            
            var modalElement = document.getElementById('reportModal');
            var myModal = new bootstrap.Modal(modalElement);
            myModal.show();
        }

        /**
         * Submit Report with AJAX
         */
        function submitReportAsync(event) {
            event.preventDefault();
            var sheetId = document.getElementById('modalCheatsheetId').value;
            var reasonVal = document.getElementById('reason').value.trim();
            
            if (!reasonVal) {
                showAlert('warning', '⚠️ ကျေးဇူးပြု၍ အကြောင်းအရင်းကို ဖြည့်သွင်းပါ။');
                return;
            }
            
            if (reasonVal.length < 10) {
                showAlert('warning', '⚠️ အနည်းဆုံး ၁၀ လုံးထက်ပို၍ ဖြည့်သွင်းပါ။');
                return;
            }
            
            var submitBtn = event.target.querySelector('button[type="submit"]');
            var originalText = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-1"></i>Submitting...';

            var formData = new URLSearchParams();
            formData.append('cheatsheetId', sheetId);
            formData.append('reason', reasonVal);

            fetch('${pageContext.request.contextPath}/reports/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                var reportModalEl = document.getElementById('reportModal');
                var modalInstance = bootstrap.Modal.getInstance(reportModalEl);
                if (modalInstance) { modalInstance.hide(); }
                document.getElementById('reason').value = '';
                
                if (data.success) {
                    showAlert('success', '✅ ' + (data.message || 'တိုင်ကြားချက်ကို အောင်မြင်စွာ ပေးပို့ပြီးပါပြီ။ Admin မှ မကြာမီ စစ်ဆေးပေးပါမည်။'));
                } else {
                    showAlert('danger', '❌ ' + (data.message || 'Failed to submit report.'));
                }
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalText;
            })
            .catch(function(error) {
                console.error('Error submitting report:', error);
                showAlert('warning', '⚠️ Submitting report via form...');
                var form = document.getElementById('reportForm');
                form.submit();
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalText;
            });
        }

        /**
         * Initialize on DOM load
         */
        document.addEventListener('DOMContentLoaded', function() {
            // Quick reason button click handler
            var reasonBtns = document.querySelectorAll('.report-reason-btn');
            for (var i = 0; i < reasonBtns.length; i++) {
                reasonBtns[i].addEventListener('click', function() {
                    var allBtns = document.querySelectorAll('.report-reason-btn');
                    for (var j = 0; j < allBtns.length; j++) {
                        allBtns[j].classList.remove('active');
                    }
                    this.classList.add('active');
                    var reason = this.getAttribute('data-reason');
                    document.getElementById('reason').value = reason + ': ';
                    document.getElementById('reason').focus();
                });
            }
            
            // Debug image paths
            var images = document.querySelectorAll('.card-img-top');
            images.forEach(function(img) {
                console.log('Image src:', img.src);
            });
            
            console.log('Category list page initialized successfully');
        });
    </script>
</body>
</html>