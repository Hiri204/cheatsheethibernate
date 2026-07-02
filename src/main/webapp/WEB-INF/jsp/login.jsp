<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Login | DevSheets</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	min-height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
	font-family: 'Segoe UI', sans-serif;
	margin: 0;
	padding: 20px;
}

.login-card {
	background: rgba(255, 255, 255, 0.95);
	backdrop-filter: blur(10px);
	border: 1px solid rgba(255, 255, 255, 0.2);
	border-radius: 24px;
	padding: 48px 40px;
	width: 100%;
	max-width: 420px;
	color: #1a1a1a;
	box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-card h2 {
	color: #1a1a1a;
	font-weight: 700;
	font-size: 2rem;
}

.login-card .subtitle {
	color: #6c757d;
	font-size: 0.9rem;
	margin-bottom: 24px;
}

.form-control {
	background: #f8f9fa;
	border: 2px solid #e9ecef;
	border-radius: 12px;
	padding: 12px 16px;
	transition: all 0.2s;
	color: #1a1a1a;
}

.form-control:focus {
	border-color: #667eea;
	box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
	background: #ffffff;
}

.form-label {
	color: #495057;
	font-weight: 600;
	font-size: 0.85rem;
	margin-bottom: 6px;
}

.btn-sign-in {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	border: none;
	width: 100%;
	padding: 14px;
	border-radius: 12px;
	font-weight: 600;
	font-size: 1rem;
	transition: all 0.3s;
}

.btn-sign-in:hover {
	transform: translateY(-2px);
	box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.social-btn {
	background: #f8f9fa;
	border: 2px solid #e9ecef;
	border-radius: 12px;
	padding: 10px;
	flex: 1;
	text-align: center;
	color: #495057;
	transition: all 0.2s;
	cursor: pointer;
}

.social-btn:hover {
	background: #e9ecef;
	transform: translateY(-2px);
}

.social-btn i {
	font-size: 1.2rem;
}

.forgot-link {
	color: #667eea;
	text-decoration: none;
	font-weight: 500;
	font-size: 0.85rem;
}

.forgot-link:hover {
	text-decoration: underline;
}

.register-link {
	color: #667eea;
	font-weight: 600;
	text-decoration: none;
}

.register-link:hover {
	text-decoration: underline;
}

.divider {
	display: flex;
	align-items: center;
	text-align: center;
	color: #adb5bd;
	font-size: 0.8rem;
	margin: 20px 0;
}

.divider::before, .divider::after {
	content: '';
	flex: 1;
	border-bottom: 2px solid #e9ecef;
}

.divider::before {
	margin-right: 16px;
}

.divider::after {
	margin-left: 16px;
}

/* Alert Styles */
.alert-custom {
	border-radius: 12px;
	border: none;
	padding: 12px 16px;
}

/* Modal Styles */
.modal-content-custom {
	border-radius: 24px;
	border: none;
	box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
	overflow: hidden;
}

.modal-header-custom {
	background: linear-gradient(135deg, #dc3545 0%, #c62828 100%);
	padding: 24px 32px;
	border-bottom: none;
}

.modal-header-custom h5 {
	color: white;
	font-weight: 700;
}

.modal-header-custom .btn-close {
	filter: brightness(0) invert(1);
	opacity: 0.8;
}

.modal-header-custom .btn-close:hover {
	opacity: 1;
}

.modal-body-custom {
	padding: 32px;
}

.modal-footer-custom {
	padding: 16px 32px 32px;
	border-top: none;
}

.ban-duration-badge {
	background: #dc3545;
	color: white;
	padding: 4px 16px;
	border-radius: 20px;
	font-weight: 600;
	font-size: 0.85rem;
}

.ban-reason-box {
	background: #fff5f5;
	border: 1px solid #fcc;
	border-radius: 12px;
	padding: 16px;
	margin-top: 12px;
}

.ban-reason-box .label {
	color: #dc3545;
	font-weight: 600;
	font-size: 0.75rem;
	text-transform: uppercase;
	letter-spacing: 0.5px;
	display: block;
	margin-bottom: 4px;
}

.ban-reason-box .reason-text {
	color: #1a1a1a;
	font-weight: 500;
	font-size: 0.95rem;
}

.icon-danger {
	font-size: 3.5rem;
	color: #dc3545;
	display: block;
	margin-bottom: 12px;
}

@media ( max-width : 480px) {
	.login-card {
		padding: 32px 24px;
	}
}
</style>
</head>
<body>

	<div class="login-card">
		<h2 class="text-center">Welcome Back</h2>
		<p class="text-center subtitle">Sign in to access your cheat
			sheets</p>

		<!-- Error Message -->
		<c:if test="${not empty error}">
			<div
				class="alert alert-danger alert-dismissible fade show alert-custom"
				role="alert">
				<i class="fa-solid fa-circle-exclamation me-2"></i> ${error}
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>

		<!-- Success Message -->
		<c:if test="${not empty successMsg}">
			<div
				class="alert alert-success alert-dismissible fade show alert-custom"
				role="alert">
				<i class="fa-solid fa-circle-check me-2"></i> ${successMsg}
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/login" method="POST">
			<div class="mb-3">
				<label class="form-label">Email or Username</label> <input
					type="text" name="username" class="form-control"
					placeholder="Enter your email or username" required>
			</div>

			<div class="mb-3">
				<label class="form-label">Password</label> <input type="password"
					name="password" class="form-control"
					placeholder="Enter your password" required>
				<div class="text-end mt-1">
					<a href="${pageContext.request.contextPath}/forgot-password"
						class="forgot-link">Forgot Password?</a>
				</div>
			</div>

			<button type="submit" class="btn-sign-in mb-3">
				<i class="fa-regular fa-arrow-right-to-bracket me-2"></i>Sign In
			</button>
		</form>

		<div class="divider">or continue with</div>

		<div class="d-flex gap-2 mb-3">
			<div class="social-btn">
				<i class="fa-brands fa-google text-danger"></i>
			</div>
			<div class="social-btn">
				<i class="fa-brands fa-github text-dark"></i>
			</div>
			<div class="social-btn">
				<i class="fa-brands fa-facebook text-primary"></i>
			</div>
		</div>

		<div class="text-center small" style="color: #6c757d;">
			Don't have an account? <a
				href="${pageContext.request.contextPath}/register"
				class="register-link">Register for free</a>
		</div>
	</div>

	<!-- 🛑 BANNED ACCOUNT MODAL -->
	<div class="modal fade" id="bannedModal" tabindex="-1"
		aria-labelledby="bannedModalLabel" aria-hidden="true"
		data-bs-backdrop="static">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content modal-content-custom">

				<!-- Modal Header -->
				<div class="modal-header modal-header-custom">
					<h5 class="modal-title" id="bannedModalLabel">
						<i class="fa-solid fa-shield-halved me-2"></i>Account Suspended
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>

				<!-- Modal Body -->
				<div class="modal-body modal-body-custom">
					<div class="text-center">
						<i class="fa-solid fa-triangle-exclamation icon-danger"></i>
						<p class="text-muted mb-3 fw-medium" style="font-size: 1rem;">
							Your account has been temporarily suspended due to a violation of
							our terms of service.</p>
					</div>

					<div class="ban-reason-box">
						<span class="label"> <i class="fa-regular fa-clock me-1"></i>
							Ban Duration
						</span> <span class="ban-duration-badge"> ${not empty banDuration ? banDuration : 'Permanent'}
						</span>
					</div>

					<div class="ban-reason-box mt-3">
						<span class="label"> <i class="fa-solid fa-gavel me-1"></i>
							Reason for Suspension
						</span>
						<div class="reason-text">${not empty banReason ? banReason : 'No specific reason provided. Please contact support for more information.'}
						</div>
					</div>

					<div class="mt-3 p-3 bg-light rounded-3">
						<p class="text-muted small mb-0">
							<i class="fa-regular fa-circle-info me-1"></i> If you believe
							this is a mistake, please contact our support team.
						</p>
					</div>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer modal-footer-custom justify-content-center">
					<button type="button"
						class="btn btn-danger px-5 rounded-pill fw-bold py-2 shadow-sm"
						data-bs-dismiss="modal">
						<i class="fa-regular fa-check me-2"></i>I Understand
					</button>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

	<script>
		document.addEventListener("DOMContentLoaded", function() {
			// Check if user is banned
			var isBanned = "${isBanned}";

			if (isBanned === "true") {
				var modalElement = document.getElementById('bannedModal');
				if (modalElement) {
					var myModal = new bootstrap.Modal(modalElement, {
						backdrop : 'static',
						keyboard : false
					});
					myModal.show();
				}
			}

			// Auto-dismiss alerts after 5 seconds
			var alerts = document.querySelectorAll('.alert');
			alerts.forEach(function(alert) {
				setTimeout(function() {
					var bsAlert = new bootstrap.Alert(alert);
					bsAlert.close();
				}, 5000);
			});
		});
	</script>
</body>
</html>