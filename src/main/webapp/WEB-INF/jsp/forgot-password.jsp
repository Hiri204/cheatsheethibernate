<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password | CheatSheet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { 
            background: linear-gradient(135deg, #74c3ff 0%, #3a86ff 100%); 
            min-height: 100vh; display: flex; align-items: center; justify-content: center;
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
        .form-control { background: white; border: none; border-radius: 8px; padding: 12px; }
        .btn-action { background: #001d3d; color: white; border: none; width: 100%; padding: 12px; border-radius: 8px; font-weight: bold; }
        .btn-action:hover { background: #000814; color: white; }
    </style>
</head>
<body>

    <div class="card login-card shadow-lg">
        <h2 class="text-center fw-bold mb-2">Forgot Password</h2>
        <p class="text-center small mb-4" style="opacity: 0.85;">
            Enter your email address below and we will send you a verification code to reset your password.
        </p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center mb-3" style="border-radius: 8px;">
                ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password" method="POST">
            <div class="mb-3">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="example@email.com" required>
            </div>
            
            <button type="submit" class="btn btn-action mb-3 mt-2">Send Reset Code</button>
            
            <div class="text-center small">
                <a href="${pageContext.request.contextPath}/login" class="text-white fw-bold text-decoration-none">
                    <i class="fa-solid fa-arrow-left me-1 small"></i> Back to Login
                </a>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>