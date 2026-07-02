<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes" />
<title>My Profile · DevSheets</title>

<!-- Bootstrap 5 CSS -->
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
.profile-card {
	border-radius: 24px;
	border: 1px solid #f0f0f0;
	background: #ffffff;
	padding: 40px;
	box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.01);
	margin-bottom: 24px;
}

.avatar-circle {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	background: linear-gradient(135deg, #1a1a1a, #444444);
	color: white;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 2.8rem;
	font-weight: 700;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
	flex-shrink: 0;
}

.avatar-img {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	object-fit: cover;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
	border: 3px solid #f0f0f0;
}

/* Stats */
.stat-item {
	text-align: center;
	padding: 8px 20px;
	border-right: 1px solid #f0f0f0;
}

.stat-item:last-child {
	border-right: none;
}

.stat-number {
	font-size: 1.3rem;
	font-weight: 700;
	color: #1a1a1a;
	display: block;
}

.stat-label {
	font-size: 0.75rem;
	color: #9c9c9c;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

/* Buttons */
.btn-edit-profile {
	background-color: #1a1a1a;
	color: #ffffff;
	border: none;
	border-radius: 20px;
	padding: 10px 28px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-edit-profile:hover {
	background-color: #333333;
	color: #ffffff;
}

/* Modal Styles */
.modal-content-custom {
	border-radius: 24px;
	border: 1px solid #f0f0f0;
	box-shadow: 0 10px 40px rgba(0, 0, 0, 0.06);
}

.modal-header-custom {
	border-bottom: 1px solid #f0f0f0;
	padding: 24px 32px;
}

.modal-body-custom {
	padding: 32px;
}

.modal-footer-custom {
	border-top: 1px solid #f0f0f0;
	padding: 16px 32px 24px;
}

.avatar-container {
	position: relative;
	width: 120px;
	height: 120px;
	margin: 0 auto 20px;
}

.profile-avatar {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	object-fit: cover;
	border: 3px solid #f0f0f0;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
}

.upload-badge {
	position: absolute;
	bottom: 0;
	right: 0;
	background: #1a1a1a;
	color: white;
	width: 36px;
	height: 36px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	border: 3px solid white;
	cursor: pointer;
	transition: all 0.2s;
}

.upload-badge:hover {
	background: #333333;
	transform: scale(1.05);
}

/* Form Controls in Modal */
.modal-form-label {
	font-weight: 600;
	font-size: 0.85rem;
	color: #555555;
	margin-bottom: 6px;
}

.modal-form-control {
	border-radius: 12px;
	border: 1px solid #ededed;
	background-color: #f9f9f9;
	padding: 10px 16px;
	font-size: 0.95rem;
	color: #1a1a1a;
	transition: all 0.2s ease;
}

.modal-form-control:focus {
	background-color: #ffffff;
	border-color: #d0d0d0;
	box-shadow: none;
	outline: none;
}

.btn-modal-cancel {
	background-color: transparent;
	color: #757575;
	border: 1px solid #ededed;
	border-radius: 20px;
	padding: 10px 28px;
	font-weight: 600;
}

.btn-modal-cancel:hover {
	background-color: #f5f5f5;
	color: #1a1a1a;
}

.btn-modal-save {
	background-color: #1a1a1a;
	color: #ffffff;
	border: none;
	border-radius: 20px;
	padding: 10px 28px;
	font-weight: 600;
}

.btn-modal-save:hover {
	background-color: #333333;
	color: #ffffff;
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
	.profile-card {
		padding: 24px;
	}
	.stat-item {
		padding: 8px 12px;
	}
	.stat-number {
		font-size: 1rem;
	}
}
</style>
</head>
<body>

	<!-- Include sidebar with active page parameter -->
	<jsp:include page="sidebar.jsp">
		<jsp:param name="activePage" value="profile" />
	</jsp:include>

	<!-- Main Content -->
	<div class="main-content">

		<!-- Page Header -->
		<div
			class="d-flex flex-wrap justify-content-between align-items-center mb-4">
			<div>
				<h3 class="page-title">
					<i class="fa-regular fa-user me-2"></i>My Profile
				</h3>
				<p class="text-muted small mt-1">Manage your account information
					and preferences</p>
			</div>
		</div>

		<!-- Check if user exists -->
		<c:choose>
			<c:when test="${empty user}">
				<div class="alert alert-warning shadow-sm mb-4" role="alert"
					style="border-radius: 16px;">
					<i class="fa-solid fa-triangle-exclamation me-2"></i> User
					information not available. Please <a
						href="${pageContext.request.contextPath}/login" class="alert-link">login</a>
					again.
				</div>
			</c:when>
			<c:otherwise>

				<!-- Success Message -->
				<c:if test="${not empty successMsg}">
					<div
						class="alert alert-success alert-dismissible fade show shadow-sm mb-4"
						role="alert"
						style="border-radius: 16px; border: 1px solid #d4edda;">
						<i class="fa-solid fa-circle-check me-2"></i> ${successMsg}
						<button type="button" class="btn-close" data-bs-dismiss="alert"
							aria-label="Close"></button>
					</div>
				</c:if>

				<!-- Error Message -->
				<c:if test="${not empty errorMsg}">
					<div
						class="alert alert-danger alert-dismissible fade show shadow-sm mb-4"
						role="alert"
						style="border-radius: 16px; border: 1px solid #f8d7da;">
						<i class="fa-solid fa-circle-exclamation me-2"></i> ${errorMsg}
						<button type="button" class="btn-close" data-bs-dismiss="alert"
							aria-label="Close"></button>
					</div>
				</c:if>

				<!-- Profile Card -->
				<div class="profile-card">
					<div class="row align-items-center g-4">
						<!-- Avatar -->
						<div class="col-md-auto text-center text-md-start">
							<c:choose>
								<c:when
									test="${not empty user.profileImg && user.profileImg ne 'default-avatar.png'}">
									<img
										src="${pageContext.request.contextPath}/uploads/${user.profileImg}"
										class="avatar-img" alt="${user.username}"
										onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
									<div class="avatar-circle" style="display: none;">
										<c:set var="firstLetter"
											value="${fn:substring(user.username, 0, 1)}" />
										${fn:toUpperCase(firstLetter)}
									</div>
								</c:when>
								<c:otherwise>
									<div class="avatar-circle">
										<c:set var="firstLetter"
											value="${fn:substring(user.username, 0, 1)}" />
										${fn:toUpperCase(firstLetter)}
									</div>
								</c:otherwise>
							</c:choose>
						</div>

						<!-- User Info -->
						<div class="col-md">
							<div class="d-flex flex-wrap align-items-center gap-2 mb-2">
								<h2 class="fw-bold mb-0" style="font-size: 1.6rem;">${user.username}</h2>
								<span
									class="badge ${user.role eq 'admin' ? 'bg-danger' : 'bg-secondary'} rounded-pill px-3 py-1.5 text-uppercase"
									style="font-size: 0.7rem; font-weight: 600;"> ${not empty user.role ? user.role : 'User'}
								</span>
							</div>

							<div class="d-flex flex-wrap gap-3 mb-3">
								<span class="text-muted" style="font-size: 0.95rem;"> <i
									class="fa-regular fa-envelope me-2"></i>${user.email}
								</span>
								<c:if test="${not empty user.phone}">
									<span class="text-muted" style="font-size: 0.95rem;"> <i
										class="fa-regular fa-phone me-2"></i>${user.phone}
									</span>
								</c:if>
							</div>

							<div class="d-flex flex-wrap">
								<div class="stat-item">
									<span class="stat-number">${sheetCount != null ? sheetCount : 0}</span>
									<span class="stat-label">Sheets</span>
								</div>
								<div class="stat-item">
									<span class="stat-number">${followerCount != null ? followerCount : 0}</span>
									<span class="stat-label">Followers</span>
								</div>
								<div class="stat-item">
									<span class="stat-number">${followingCount != null ? followingCount : 0}</span>
									<span class="stat-label">Following</span>
								</div>
							</div>
						</div>

						<!-- Action Button -->
						<div class="col-md-auto text-center text-md-end">
							<button type="button" class="btn-edit-profile"
								data-bs-toggle="modal" data-bs-target="#editProfileModal">
								<i class="fa-regular fa-pen-to-square me-2"></i>Edit Profile
							</button>
						</div>
					</div>
				</div>

				<!-- Additional Info Card -->
				<div class="profile-card">
					<h5 class="fw-bold mb-3" style="font-size: 1rem;">
						<i class="fa-regular fa-clock me-2"></i>Account Information
					</h5>
					<div class="row g-3">
						<div class="col-md-6">
							<div class="d-flex justify-content-between py-2 border-bottom"
								style="border-color: #f5f5f5 !important;">
								<span class="text-muted" style="font-size: 0.9rem;">Member
									Since</span> <span class="fw-medium" style="font-size: 0.9rem;">
									<c:choose>
										<c:when test="${not empty user.createdAt}">
											<fmt:parseDate value="${user.createdAt}"
												pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedCreated" />
											<fmt:formatDate value="${parsedCreated}"
												pattern="dd MMM yyyy" />
										</c:when>
										<c:otherwise>N/A</c:otherwise>
									</c:choose>
								</span>
							</div>
						</div>
						<div class="col-md-6">
							<div class="d-flex justify-content-between py-2 border-bottom"
								style="border-color: #f5f5f5 !important;">
								<span class="text-muted" style="font-size: 0.9rem;">Account
									Status</span> <span class="fw-medium text-success"
									style="font-size: 0.9rem;"> <i
									class="fa-solid fa-circle-check me-1"></i>Active
								</span>
							</div>
						</div>
						<div class="col-md-6">
							<div class="d-flex justify-content-between py-2 border-bottom"
								style="border-color: #f5f5f5 !important;">
								<span class="text-muted" style="font-size: 0.9rem;">User
									ID</span> <span class="fw-medium" style="font-size: 0.9rem;"> <c:choose>
										<c:when test="${not empty user.userId}">
                                            #${user.userId}
                                        </c:when>
										<c:otherwise>N/A</c:otherwise>
									</c:choose>
								</span>
							</div>
						</div>
						<div class="col-md-6">
							<div class="d-flex justify-content-between py-2 border-bottom"
								style="border-color: #f5f5f5 !important;">
								<span class="text-muted" style="font-size: 0.9rem;">Total
									Sheets</span> <span class="fw-medium" style="font-size: 0.9rem;">${sheetCount != null ? sheetCount : 0}</span>
							</div>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<!-- Edit Profile Modal -->
	<div class="modal fade" id="editProfileModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="editProfileModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content modal-content-custom">
				<div class="modal-header modal-header-custom">
					<h5 class="modal-title fw-bold" id="editProfileModalLabel">
						<i class="fa-regular fa-user-gear me-2"></i>Account Settings
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>

				<form action="${pageContext.request.contextPath}/profile/update"
					method="POST" enctype="multipart/form-data">
					<div class="modal-body modal-body-custom">
						<!-- Avatar Upload -->
						<div class="text-center mb-4">
							<div class="avatar-container">
								<c:choose>
									<c:when
										test="${not empty user.profileImg && user.profileImg ne 'default-avatar.png'}">
										<img id="profilePreview"
											src="${pageContext.request.contextPath}/uploads/${user.profileImg}"
											class="profile-avatar" alt="Profile" />
									</c:when>
									<c:otherwise>
										<c:set var="firstLetter"
											value="${fn:substring(user.username, 0, 1)}" />
										<img id="profilePreview"
											src="https://ui-avatars.com/api/?name=${fn:toUpperCase(firstLetter)}&background=1a1a1a&color=fff&size=120&bold=true"
											class="profile-avatar" alt="Default Profile" />
									</c:otherwise>
								</c:choose>
								<label for="profileImageInput" class="upload-badge"
									title="Change photo"> <i
									class="fa-solid fa-camera small"></i>
								</label> <input type="file" name="profileImage" id="profileImageInput"
									accept="image/*" class="d-none" />
							</div>
							<h5 class="fw-bold mb-1">${user.username}</h5>
							<span
								class="badge ${user.role eq 'admin' ? 'bg-danger' : 'bg-secondary'} rounded-pill px-3 py-1.5 text-uppercase"
								style="font-size: 0.7rem; font-weight: 600;"> ${not empty user.role ? user.role : 'User'}
							</span>
							<p class="text-muted small mt-2 mb-0">
								<i class="fa-regular fa-circle-info me-1"></i>Click the camera
								icon to change your photo
							</p>
						</div>

						<!-- Form Fields -->
						<div class="row g-3">
							<div class="col-12">
								<label class="modal-form-label">Username</label> <input
									type="text" name="username"
									class="form-control modal-form-control"
									value="${user.username}" required />
							</div>
							<div class="col-12">
								<label class="modal-form-label">Email Address</label> <input
									type="email" name="email"
									class="form-control modal-form-control" value="${user.email}"
									required />
							</div>
							<div class="col-12">
								<label class="modal-form-label">Phone Number</label> <input
									type="text" name="phone"
									class="form-control modal-form-control" value="${user.phone}"
									placeholder="Enter your phone number" />
							</div>
							<div class="col-12">
								<label class="modal-form-label">New Password <span
									class="text-muted fw-normal">(leave blank to keep
										current)</span></label> <input type="password" name="password"
									class="form-control modal-form-control"
									placeholder="Enter new password" />
							</div>
						</div>
					</div>

					<div class="modal-footer modal-footer-custom">
						<button type="button" class="btn-modal-cancel"
							data-bs-dismiss="modal">
							<i class="fa-regular fa-xmark me-1"></i>Cancel
						</button>
						<button type="submit" class="btn-modal-save">
							<i class="fa-regular fa-floppy-disk me-1"></i>Save Changes
						</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
		
	</script>

	<script>
		// Image preview for profile upload
		document.getElementById('profileImageInput').onchange = function(evt) {
			var tgt = evt.target || window.event.srcElement;
			var files = tgt.files;
			if (FileReader && files && files.length) {
				var fr = new FileReader();
				fr.onload = function() {
					document.getElementById('profilePreview').src = fr.result;
				}
				fr.readAsDataURL(files[0]);
			}
		}

		// Auto-dismiss alerts after 5 seconds
		document.addEventListener('DOMContentLoaded', function() {
			const alerts = document.querySelectorAll('.alert');
			alerts.forEach(function(alert) {
				setTimeout(function() {
					const bsAlert = new bootstrap.Alert(alert);
					bsAlert.close();
				}, 5000);
			});
		});
	</script>
</body>
</html>