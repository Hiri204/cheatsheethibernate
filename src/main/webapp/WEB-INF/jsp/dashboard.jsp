<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes" />
<title>Dynamic Dashboard · Panel</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<style>
body {
	background-color: #fafafa;
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
		Helvetica, Arial, sans-serif;
	color: #1a1a1a;
}

/* Folder Structure with Richer Solid-Pastel Colors (အရောင်ပိုရင့်ပေးထားပါတယ်) */
.folder-wrapper {
	position: relative;
	filter: drop-shadow(0px 4px 6px rgba(0, 0, 0, 0.02));
	cursor: pointer;
	transition: transform 0.2s ease, filter 0.2s ease;
	text-decoration: none;
	display: block;
}

.folder-wrapper:hover {
	transform: translateY(-4px);
	filter: drop-shadow(0px 8px 12px rgba(0, 0, 0, 0.05));
}

.folder-tab {
	width: 90px;
	height: 16px;
	border-top-left-radius: 12px;
	border-top-right-radius: 12px;
	position: absolute;
	top: -15px;
	left: 0;
}

.folder-body {
	border-radius: 20px;
	border-top-left-radius: 0px;
	padding: 35px 24px 25px 24px;
	min-height: 130px;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.folder-dots {
	position: absolute;
	top: 15px;
	right: 20px;
	color: #757575;
	font-size: 0.85rem;
}

/* Richer & Solid Folder Colors (ပိုတောက်ပြီး ရင့်တဲ့အရောင်များ) */
.folder-cat-0 .folder-tab, .folder-cat-0 .folder-body {
	background-color: #dcd9f8;
} /* Richer Purple */
.folder-cat-1 .folder-tab, .folder-cat-1 .folder-body {
	background-color: #ccecfc;
} /* Richer Blue */
.folder-cat-2 .folder-tab, .folder-cat-2 .folder-body {
	background-color: #f7dfce;
} /* Richer Peach */
.folder-cat-3 .folder-tab, .folder-cat-3 .folder-body {
	background-color: #e2f7cb;
} /* Richer Green */

/* Controls & Topbar */
.main-content {
	margin-left: 260px;
	padding: 35px 45px;
}

.search-wrapper {
	position: relative;
}

.search-bar {
	border-radius: 12px;
	padding: 10px 16px 10px 42px;
	border: 1px solid #ededed;
	background: #f7f7f7;
	width: 320px;
	font-size: 0.9rem;
	transition: all 0.2s;
}

.search-bar:focus {
	outline: none;
	background: #ffffff;
	border-color: #e0e0e0;
}

.search-wrapper i {
	position: absolute;
	left: 16px;
	top: 50%;
	transform: translateY(-50%);
	color: #adadad;
}

.collapse-icon {
	transition: transform 0.2s;
	font-size: 0.75rem;
	color: #adadad;
}

[aria-expanded="true"] .collapse-icon {
	transform: rotate(90deg);
}

.header-avatar {
	width: 36px;
	height: 36px;
	border-radius: 50%;
	object-fit: cover;
}

.user-badge {
	font-size: 0.8rem;
	color: #757575;
	background: #f3f3f3;
	padding: 4px 10px;
	border-radius: 8px;
}

.btn-logout {
	background: transparent;
	color: #757575;
	border: 1px solid #ededed;
	border-radius: 14px;
	font-size: 0.95rem;
	transition: all 0.2s;
}

.btn-logout:hover {
	background: #fff5f5;
	color: #ff4d4f;
	border-color: #ffcccc;
}
</style>
</head>
<body>
	<jsp:include page="sidebar.jsp" />

	<div class="sidebar">
		<div class="sidebar-menu-wrapper">
			<a href="${pageContext.request.contextPath}/dashboard"
				class="sidebar-brand"> <i
				class="fa-solid fa-feather-pointed me-2"></i>Floe
			</a>

			<ul class="nav flex-column">
				<li class="nav-item"><a class="nav-link active"
					href="${pageContext.request.contextPath}/dashboard"> <span><i
							class="fa-solid fa-house me-2"></i> Home</span>
				</a></li>

				<div class="section-title">Actions</div>
				<li class="nav-item"><a class="nav-link"
					data-bs-toggle="collapse" href="#quickActionsMenu"
					aria-expanded="false"> <span><i
							class="fa-solid fa-bolt me-2"></i> Quick Actions</span> <i
						class="fa-solid fa-chevron-right collapse-icon"></i>
				</a>
					<ul class="collapse list-unstyled ps-3 mt-1" id="quickActionsMenu">
						<li><a class="nav-link"
							href="${pageContext.request.contextPath}/cheatsheets/new"> <span><i
									class="fa-solid fa-circle-plus me-2"></i> Create New Sheet</span>
						</a></li>
						<li><a class="nav-link"
							href="${pageContext.request.contextPath}/cheatsheets/my-cheatsheets">
								<span><i class="fa-solid fa-folder-open me-2"></i> My
									Cheat Sheets</span>
						</a></li>
					</ul></li>

				<c:if test="${sessionScope.loginUser.role eq 'admin'}">
					<div class="mt-2">
						<div class="section-title">Admin Tools</div>
						<li class="nav-item"><a class="nav-link"
							data-bs-toggle="collapse" href="#adminTools"
							aria-expanded="false"> <span><i
									class="fa-solid fa-shield-halved me-2"></i> Management</span> <i
								class="fa-solid fa-chevron-right collapse-icon"></i>
						</a>
							<ul class="collapse list-unstyled ps-3 mt-1" id="adminTools">
								<li><a class="nav-link"
									href="${pageContext.request.contextPath}/admin/metadata/panel"><span><i
											class="fa-solid fa-folder-tree me-2"></i> Categories</span></a></li>
								<li><a class="nav-link"
									href="${pageContext.request.contextPath}/admin/broadcast/panel"><span><i
											class="fa-solid fa-bullhorn me-2"></i> Announcements</span></a></li>
							</ul></li>

						<li class="nav-item"><a class="nav-link"
							data-bs-toggle="collapse" href="#userTools" aria-expanded="false">
								<span><i class="fa-solid fa-user-gear me-2"></i> User
									Tools</span> <i class="fa-solid fa-chevron-right collapse-icon"></i>
						</a>
							<ul class="collapse list-unstyled ps-3 mt-1" id="userTools">
								<li><a class="nav-link"
									href="${pageContext.request.contextPath}/user/list"><span><i
											class="fa-solid fa-users me-2"></i> User List</span></a></li>
								<li><a class="nav-link"
									href="${pageContext.request.contextPath}/reports/admin/list"><span><i
											class="fa-solid fa-triangle-exclamation me-2"></i> Reports</span></a></li>
								<li><a class="nav-link"
									href="${pageContext.request.contextPath}/reports/admin/summary"><span><i
											class="fa-solid fa-chart-simple me-2"></i> Analytics</span></a></li>
							</ul></li>
					</div>
				</c:if>
			</ul>
		</div>

		<div class="pt-2 border-top">
			<ul class="nav flex-column mb-3">
				<li class="nav-item"><a class="nav-link"
					data-bs-toggle="collapse" href="#settingTools"
					aria-expanded="false"> <span><i
							class="fa-solid fa-gear me-2"></i> Settings</span> <i
						class="fa-solid fa-chevron-right collapse-icon"></i>
				</a>
					<ul class="collapse list-unstyled ps-3 mt-1" id="settingTools">
						<li><a class="nav-link"
							href="${pageContext.request.contextPath}/profile/view"> <span><i
									class="fa-solid fa-user me-2"></i> My Profile</span>
						</a></li>
					</ul></li>
			</ul>
			<a href="${pageContext.request.contextPath}/logout"
				class="btn btn-logout w-100 py-2 fw-medium"> <i
				class="fa-solid fa-right-from-bracket me-1"></i> Logout
			</a>
		</div>
	</div>

	<div class="main-content">
		<div class="d-flex justify-content-between align-items-center mb-5">
			<div>
				<h4 class="fw-bold mb-1" style="letter-spacing: -0.5px;">My
					Workflows</h4>
			</div>

			<div class="d-flex gap-3 align-items-center">
				<form action="${pageContext.request.contextPath}/cheatsheets/search"
					method="GET" class="d-none d-md-block">
					<div class="search-wrapper">
						<i class="fa-solid fa-magnifying-glass"></i> <input type="text"
							name="keyword" class="search-bar" placeholder="Search...">
					</div>
				</form>

				<div class="d-flex align-items-center gap-3 ms-2">
					<span class="user-badge">${sessionScope.loginUser.role}</span> <a
						href="${pageContext.request.contextPath}/profile/view"> <c:choose>
							<c:when
								test="${not empty sessionScope.loginUser.profileImg and sessionScope.loginUser.profileImg ne 'default-avatar.png'}">
								<img
									src="${pageContext.request.contextPath}/uploads/${sessionScope.loginUser.profileImg}?v=${pageContext.session.lastAccessedTime}"
									class="header-avatar" alt="Avatar">
							</c:when>
							<c:otherwise>
								<img
									src="https://ui-avatars.com/api/?name=${sessionScope.loginUser.username}&background=e8e8e8&color=555555&bold=true"
									class="header-avatar" alt="Default Avatar">
							</c:otherwise>
						</c:choose>
					</a>
				</div>
			</div>
		</div>

		<div class="d-flex align-items-center gap-2 mb-4">
			<h6 class="text-secondary fw-semibold mb-0"
				style="color: #1a1a1a !important; font-size: 1.05rem;">Folders</h6>
			<span class="badge rounded-pill bg-light text-muted border px-2 py-1"
				style="font-size: 0.75rem;">${categories.size()}</span>
		</div>

		<div class="row g-4 mb-5" style="margin-top: 5px;">
			<c:forEach items="${categories}" var="cat" varStatus="status">
				<div class="col-md-4 col-sm-6">
					<a
						href="${pageContext.request.contextPath}/cheatsheets/category/${cat.name}"
						class="folder-wrapper folder-cat-${status.index % 4}">
						<div class="folder-tab"></div>
						<div class="folder-body">
							<div class="folder-dots">
								<i class="fa-solid fa-ellipsis-vertical"></i>
							</div>
							<h6 class="fw-bold text-dark mb-0"
								style="font-size: 1.05rem; letter-spacing: -0.3px;">${cat.name}</h6>
							<small class="text-muted"
								style="font-size: 0.8rem; opacity: 0.8;">View resources</small>
						</div>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>