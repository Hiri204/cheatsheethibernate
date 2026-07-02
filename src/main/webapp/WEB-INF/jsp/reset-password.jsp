<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reset Password | CheatSheet</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet">
<%-- မျက်လုံး Icon သုံးနိုင်ရန် FontAwesome Link ချိတ်ဆက်ထားခြင်း --%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<style>
body {
	background: linear-gradient(135deg, #74c3ff 0%, #3a86ff 100%);
	min-height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
	font-family: 'Segoe UI', sans-serif;
}

.login-card {
	background: rgba(255, 255, 255, 0.15);
	backdrop-filter: blur(10px);
	border: 1px solid rgba(255, 255, 255, 0.2);
	border-radius: 20px;
	padding: 40px;
	width: 100%;
	max-width: 400px;
	color: white;
	box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.form-control {
	background: white;
	border: none;
	border-radius: 8px 0 0 8px;
	padding: 12px;
	color: #333;
}

.form-control:focus {
	background: white;
	color: #333;
	box-shadow: none;
}

.input-group-text {
	background: white;
	border: none;
	border-radius: 0 8px 8px 0;
	color: #666;
	cursor: pointer;
}

.btn-action {
	background: #001d3d;
	color: white;
	border: none;
	width: 100%;
	padding: 12px;
	border-radius: 8px;
	font-weight: bold;
}

.btn-action:hover {
	background: #000814;
	color: white;
}
</style>
</head>
<body>

	<div class="card login-card shadow-lg">

		<c:if test="${not empty message}">
			<div class="alert alert-success text-center mb-3"
				style="border-radius: 8px;">${message}</div>
		</c:if>

		<c:if test="${not empty error}">
			<div class="alert alert-danger text-center mb-3"
				style="border-radius: 8px;">${error}</div>
		</c:if>

		<%-- JavaScript မှ စစ်ဆေးမည့် Client-side Error Message --%>
		<div id="jsError" class="alert alert-danger text-center mb-3 d-none"
			style="border-radius: 8px;"></div>

		<c:choose>
			<%-- အဆင့် ၂။ Code မှန်သွားလျှင် ပွင့်လာမည့် Password အသစ် Form --%>
			<c:when test="${codeVerified == true}">
				<h2 class="text-center fw-bold mb-4">New Password</h2>
				<form action="${pageContext.request.contextPath}/reset-password"
					method="POST" onsubmit="return validatePasswords()">
					<input type="hidden" name="email" value="${email}"> <input
						type="hidden" name="code" value="${code}">

					<%-- New Password Input Field --%>
					<div class="mb-3">
						<label for="newPassword" class="form-label">Type New
							Password</label>
						<div class="input-group">
							<input type="password" class="form-control" id="newPassword"
								name="newPassword" placeholder="Enter new password" required>
							<span class="input-group-text"
								onclick="togglePasswordVisibility('newPassword', 'eyeIcon1')">
								<i class="fa-solid fa-eye-slash" id="eyeIcon1"></i>
							</span>
						</div>
					</div>

					<%-- Confirm Password Input Field --%>
					<div class="mb-3">
						<label for="confirmPassword" class="form-label">Confirm
							New Password</label>
						<div class="input-group">
							<input type="password" class="form-control" id="confirmPassword"
								placeholder="Confirm new password" required> <span
								class="input-group-text"
								onclick="togglePasswordVisibility('confirmPassword', 'eyeIcon2')">
								<i class="fa-solid fa-eye-slash" id="eyeIcon2"></i>
							</span>
						</div>
					</div>

					<button type="submit" class="btn btn-action mb-3 mt-2">Save
						Password</button>
				</form>
			</c:when>

			<%-- အဆင့် ၁။ အစဦးတွင် Code ကိုပဲ အရင်တောင်းမည့် Form --%>
			<c:otherwise>
				<h2 class="text-center fw-bold mb-4">Verify Code</h2>
				<form action="${pageContext.request.contextPath}/verify-code"
					method="POST">
					<input type="hidden" name="email" value="${email}">

					<div class="mb-3">
						<label for="code" class="form-label">6-Digit Verification
							Code</label> <input type="text" class="form-control" id="code"
							name="code" placeholder="Enter code from email"
							style="border-radius: 8px;" required autocomplete="off">
					</div>

					<button type="submit" class="btn btn-action mb-3 mt-2">Verify
						Code</button>

					<div class="text-center small">
						Didn't receive the code? <a
							href="${pageContext.request.contextPath}/forgot-password"
							class="text-white fw-bold text-decoration-none">Resend Code</a>
					</div>
				</form>
			</c:otherwise>
		</c:choose>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

	<script>
		// ၁။ မျက်လုံး Icon နှိပ်လျှင် Password ကို ဖွင့်/ပိတ် ပြသပေးမည့် JavaScript Function
		function togglePasswordVisibility(inputId, iconId) {
			var passwordInput = document.getElementById(inputId);
			var eyeIcon = document.getElementById(iconId);

			if (passwordInput.type === "password") {
				passwordInput.type = "text";
				eyeIcon.classList.remove("fa-eye-slash");
				eyeIcon.classList.add("fa-eye");
			} else {
				passwordInput.type = "password";
				eyeIcon.classList.remove("fa-eye");
				eyeIcon.classList.add("fa-eye-slash");
			}
		}

		// ၂။ Form မတင်မီ Password နှစ်ခု ကိုက်ညီမှု ရှိမရှိ စစ်ဆေးမည့် JavaScript Function
		function validatePasswords() {
			var newPassword = document.getElementById("newPassword").value;
			var confirmPassword = document.getElementById("confirmPassword").value;
			var errorDiv = document.getElementById("jsError");

			if (newPassword !== confirmPassword) {
				errorDiv.innerText = "Passwords do not match. Please check again.";
				errorDiv.classList.remove("d-none");
				return false; // Form Submit လုပ်ခြင်းကို ရပ်တန့်မည်
			}

			errorDiv.classList.add("d-none");
			return true; // စစ်ဆေးမှု အောင်မြင်သဖြင့် Form တင်မည်
		}
	</script>
</body>
</html>