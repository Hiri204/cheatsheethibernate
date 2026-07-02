<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes" />
<title>Register · CheatSheet</title>

<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<style>
body {
	background: #f4f7fe;
	font-family: 'Segoe UI', sans-serif;
	display: flex;
	align-items: center;
	justify-content: center;
	min-height: 100vh;
}

.reg-card {
	background: white;
	border-radius: 20px;
	box-shadow: 0 20px 40px rgba(0, 0, 0, 0.05);
	overflow: hidden;
	width: 950px;
	max-width: 95%;
	display: flex;
}

.terms-side {
	background: #f1f5f9;
	padding: 40px;
	width: 35%;
	color: #64748b;
}

.form-side {
	padding: 40px;
	width: 65%;
}

.form-control {
	border-radius: 10px;
	padding: 12px;
	border: 1px solid #e2e8f0;
}

.btn-primary {
	background: #5e72e4;
	border: none;
	padding: 12px 30px;
	border-radius: 10px;
	font-weight: 600;
}

.btn-primary:hover {
	background: #4e62d4;
}

.preview-avatar {
	width: 80px;
	height: 80px;
	border-radius: 50%;
	object-fit: cover;
	border: 2px dashed #ced4da;
	display: none;
	margin-bottom: 10px;
}
</style>
</head>
<body>

	<div class="reg-card">
		<div class="terms-side d-none d-md-block">
			<h4 class="text-dark fw-bold mb-4">Terms & Conditions</h4>
			<small class="lh-lg"> 01. By registering, you agree to our
				terms of service.<br> <br> 02. Your data is handled with
				privacy and care.<br> <br> 03. Keep your account
				credentials secure.<br> <br> 04. We reserve the right to
				modify these terms at any time.
			</small>
		</div>

		<div class="form-side">
			<h3 class="fw-bold mb-1">Registration Form</h3>
			<p class="text-muted mb-4">Complete the fields below to get
				started with your avatar.</p>

			<c:if test="${not empty errorMsg}">
				<div class="alert alert-danger alert-dismissible fade show mb-3"
					role="alert" style="border-radius: 10px;">
					<i class="fa-solid fa-circle-exclamation me-2"></i> ${errorMsg}
					<button type="button" class="btn-close" data-bs-dismiss="alert"
						aria-label="Close"></button>
				</div>
			</c:if>

			<form:form action="${pageContext.request.contextPath}/register"
				method="POST" modelAttribute="user" enctype="multipart/form-data">

				<div class="mb-3 text-center text-md-start">
					<label class="form-label small fw-bold d-block">Profile
						Picture</label> <img id="avatarPreview" class="preview-avatar" src="#"
						alt="Preview"> <input type="file" name="profileImage"
						id="profileImageInput" class="form-control" accept="image/*" />
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Username</label>
					<form:input path="username" class="form-control"
						placeholder="Enter username" required="required" />
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Email</label>
					<form:input path="email" type="email" class="form-control"
						placeholder="example@mail.com" required="required" />
				</div>

				<div class="mb-4">
					<label class="form-label small fw-bold">Password</label>
					<form:password path="passwordHash" class="form-control"
						placeholder="Create a password" required="required" />
				</div>

				<button type="submit" class="btn btn-primary w-100 shadow-sm">CREATE
					ACCOUNT</button>
			</form:form>

			<div class="text-center mt-4">
				<small class="text-muted">Already have an account? <a
					href="${pageContext.request.contextPath}/login"
					class="text-primary fw-semibold text-decoration-none">Sign in</a></small>
			</div>
		</div>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<script>
		// 💡 ပုံရွေးချယ်လိုက်လျှင် ချက်ချင်း ဝိုင်းဝိုင်းလေးဖြင့် Preview ပြပေးမည့် JavaScript Logic
		document.getElementById('profileImageInput').onchange = function(evt) {
			var tgt = evt.target || window.event.srcElement, files = tgt.files;

			if (FileReader && files && files.length) {
				var fr = new FileReader();
				fr.onload = function() {
					var img = document.getElementById('avatarPreview');
					img.src = fr.result;
					img.style.display = 'inline-block';
				}
				fr.readAsDataURL(files[0]);
			}
		}
	</script>
</body>
</html>