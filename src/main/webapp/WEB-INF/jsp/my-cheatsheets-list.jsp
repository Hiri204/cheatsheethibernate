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
<title>My Cheat Sheets · DevSheets</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
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
	background-color: #fafafa;
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
		Helvetica, Arial, sans-serif;
	color: #1a1a1a;
	min-height: 100vh;
}

/* Main Layout Alignment with Shared Sidebar */
.main-content {
	margin-left: 260px;
	padding: 40px 50px;
	min-height: 100vh;
	background-color: #fafafa;
}

/* Page Header */
.page-title {
	font-size: 1.6rem;
	font-weight: 700;
	letter-spacing: -0.5px;
	color: #1a1a1a;
}

/* ─── Card Styles ─── */
.sheet-card {
	border-radius: 16px;
	border: 1px solid #f0f0f0;
	background: #ffffff;
	box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.01);
	transition: all 0.3s ease;
	height: 100%;
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.sheet-card:hover {
	transform: translateY(-5px);
	box-shadow: 0px 8px 30px rgba(0, 0, 0, 0.06);
	border-color: #e0e0e0;
}

.sheet-card .card-img-top {
	height: 180px;
	object-fit: cover;
	border-bottom: 1px solid #f0f0f0;
}

/* Image placeholder */
.image-placeholder {
	height: 180px;
	background: #f5f5f5;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #b0b0b0;
	font-size: 0.9rem;
	flex-direction: column;
	gap: 8px;
	border-bottom: 1px solid #f0f0f0;
}

.image-placeholder i {
	font-size: 3rem;
	color: #d0d0d0;
}

.card-body {
	padding: 24px;
	flex: 1;
	display: flex;
	flex-direction: column;
}

/* Badge Styles */
.badge-draft {
	background-color: #f0f0f0;
	color: #757575;
	font-weight: 600;
	padding: 4px 12px;
	border-radius: 20px;
	font-size: 0.7rem;
	text-transform: uppercase;
}

.badge-published {
	background-color: #e8f5e9;
	color: #2e7d32;
	font-weight: 600;
	padding: 4px 12px;
	border-radius: 20px;
	font-size: 0.7rem;
	text-transform: uppercase;
}

.badge-archived {
	background-color: #fbe9e7;
	color: #c62828;
	font-weight: 600;
	padding: 4px 12px;
	border-radius: 20px;
	font-size: 0.7rem;
	text-transform: uppercase;
}

.category-badge {
	background-color: #f5f5f5;
	color: #555555;
	font-weight: 500;
	padding: 4px 12px;
	border-radius: 20px;
	font-size: 0.7rem;
}

.time-text {
	font-size: 0.75rem;
	color: #9c9c9c;
	font-weight: 500;
}

.tag-badge {
	font-size: 0.7rem;
	background-color: #f5f5f5;
	color: #555555;
	border-radius: 6px;
	padding: 4px 10px;
	font-weight: 500;
	display: inline-block;
	margin: 2px;
}

/* Modern Rounded Buttons */
.btn-action-sm {
	padding: 8px 16px;
	border-radius: 20px;
	font-weight: 600;
	font-size: 0.85rem;
	transition: all 0.2s;
}

.btn-outline-primary-custom {
	background-color: transparent;
	color: #1a1a1a;
	border: 1px solid #e8e8e8;
}

.btn-outline-primary-custom:hover {
	background-color: #f5f5f5;
	color: #1a1a1a;
	border-color: #d0d0d0;
}

.btn-outline-danger-custom {
	background-color: transparent;
	color: #757575;
	border: 1px solid #e8e8e8;
}

.btn-outline-danger-custom:hover {
	background-color: #fff5f5;
	color: #ff4d4f;
	border-color: #ffcccc;
}

.btn-primary-custom {
	background-color: #1a1a1a;
	color: #ffffff;
	border: none;
	border-radius: 20px;
	padding: 12px 28px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-primary-custom:hover {
	background-color: #333333;
	color: #ffffff;
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

/* Responsive adjustments */
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
}
</style>
</head>
<body>

	<!-- Include sidebar with active page parameter -->
	<jsp:include page="sidebar.jsp">
		<jsp:param name="activePage" value="mysheets" />
	</jsp:include>

	<!-- Main Content -->
	<div class="main-content">

		<!-- Page Header -->
		<div
			class="d-flex flex-wrap justify-content-between align-items-center mb-4">
			<div>
				<h3 class="page-title">
					<i class="fa-regular fa-folder-open me-2"></i>My Workspaces
				</h3>
				<p class="text-muted small mt-1">သင်၏ Cheat Sheets များကို
					စီမံခန့်ခွဲရန် သို့မဟုတ် ပြင်ဆင်ရန် အောက်ပါအတိုင်း
					လုပ်ဆောင်နိုင်ပါသည်။</p>
			</div>
			<div>
				<a href="${pageContext.request.contextPath}/cheatsheets/new"
					class="btn btn-primary-custom"> <i
					class="fa-solid fa-plus me-2"></i>Create New Sheet
				</a>
			</div>
		</div>

		<!-- Sheets Grid -->
		<div class="row g-4">
			<c:forEach var="sheet" items="${sheets}">
				<div class="col-md-6 col-lg-4">
					<div class="sheet-card">

						<!-- 🎯 FIXED: Image with fallback -->
						<c:choose>
							<c:when test="${not empty sheet.fileUrl}">
								<img src="${pageContext.request.contextPath}${sheet.fileUrl}"
									class="card-img-top" alt="${sheet.title}"
									onerror="this.onerror=null; this.parentElement.innerHTML='<div class=\'image-placeholder\'>
								<i class=\'fa-regularfa-image\'></i>
								<span>No Image</span>
					</div>
					'" />
					</c:when>
					<c:otherwise>
						<div class="image-placeholder">
							<i class="fa-regular fa-image"></i> <span>No Image</span>
						</div>
					</c:otherwise>
					</c:choose>

					<div class="card-body">
						<!-- Header: Category + Status -->
						<div
							class="d-flex justify-content-between align-items-center mb-3">
							<span class="category-badge"> <i
								class="fa-regular fa-folder me-1"></i> ${sheet.category != null ? sheet.category.name : 'Uncategorized'}
							</span> <span class="badge-${sheet.status}"> <i
								class="fa-regular fa-circle me-1"></i> ${sheet.status}
							</span>
						</div>

						<!-- Title -->
						<h5 class="fw-bold text-dark mb-3" style="font-size: 1.05rem;">
							${sheet.title}</h5>

						<!-- Content Preview -->
						<c:if test="${not empty sheet.content}">
							<p class="text-muted small mb-3"
								style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
								${sheet.content}</p>
						</c:if>

						<!-- Timestamps -->
						<div class="mb-3 p-2 bg-light rounded-3">
							<div class="time-text mb-1">
								<i class="fa-regular fa-calendar-plus me-1"></i> Created:
								<fmt:parseDate value="${sheet.createdAt}"
									pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedCreated" />
								<fmt:formatDate value="${parsedCreated}"
									pattern="dd MMM yyyy, hh:mm a" />
							</div>
							<c:if test="${not empty sheet.updatedAt}">
								<div class="time-text">
									<i class="fa-regular fa-calendar-check me-1"></i> Updated:
									<fmt:parseDate value="${sheet.updatedAt}"
										pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedUpdated" />
									<fmt:formatDate value="${parsedUpdated}"
										pattern="dd MMM yyyy, hh:mm a" />
								</div>
							</c:if>
						</div>

						<!-- Tags -->
						<div class="mb-3 d-flex flex-wrap gap-1">
							<c:forEach var="tag" items="${sheet.tags}">
								<span class="tag-badge"> <i
									class="fa-solid fa-tag me-1 fa-xs"></i>${tag.name}
								</span>
							</c:forEach>
							<c:if test="${empty sheet.tags}">
								<small class="text-muted fst-italic" style="font-size: 0.7rem;">
									No tags assigned </small>
							</c:if>
						</div>

						<!-- Action Buttons -->
						<div class="d-flex gap-2 mt-auto pt-3 border-top">
							<a
								href="${pageContext.request.contextPath}/cheatsheets/edit/${sheet.id}"
								class="btn btn-outline-primary-custom btn-action-sm flex-grow-1">
								<i class="fa-regular fa-pen-to-square me-1"></i> Edit
							</a> <a
								href="${pageContext.request.contextPath}/cheatsheets/delete/${sheet.id}"
								onclick="return confirm('ဤ Cheat Sheet အား စနစ်ထဲမှ အပြီးတိုင် ဖျက်ဆီးရန် သေချာပါသလား ဆရာ?');"
								class="btn btn-outline-danger-custom btn-action-sm flex-grow-1">
								<i class="fa-regular fa-trash-can me-1"></i> Delete
							</a>
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
					<h5>ဆရာ တင်ထားသော Cheat Sheet တစ်စောင်မှ မရှိသေးပါဗျာ။</h5>
					<p class="text-muted small">စတင်ရန် "Create New Sheet" ခလုတ်ကို
						နှိပ်ပါ။</p>
					<a href="${pageContext.request.contextPath}/cheatsheets/new"
						class="btn btn-primary-custom mt-3"> <i
						class="fa-solid fa-plus me-2"></i>Create Your First Sheet
					</a>
				</div>
			</div>
		</c:if>
	</div>

	<!-- Pagination (if you have pagination support) -->
	<c:if test="${not empty sheets and not empty totalPages}">
		<nav aria-label="Page navigation" class="mt-5">
			<ul class="pagination justify-content-center">
				<c:if test="${currentPage > 0}">
					<li class="page-item"><a class="page-link"
						href="?page=${currentPage - 1}"> <i
							class="fa-solid fa-chevron-left"></i>
					</a></li>
				</c:if>
				<c:forEach begin="0" end="${totalPages - 1}" var="i">
					<li class="page-item ${i == currentPage ? 'active' : ''}"><a
						class="page-link" href="?page=${i}">${i + 1}</a></li>
				</c:forEach>
				<c:if test="${currentPage < totalPages - 1}">
					<li class="page-item"><a class="page-link"
						href="?page=${currentPage + 1}"> <i
							class="fa-solid fa-chevron-right"></i>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js">
		
	</script>
</body>
</html>