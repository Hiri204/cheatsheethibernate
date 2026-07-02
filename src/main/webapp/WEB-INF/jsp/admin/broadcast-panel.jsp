<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Broadcast Management | Admin</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body {
	background-color: #f8f9fe;
	font-family: 'Segoe UI', sans-serif;
	color: #525f7f;
}

/* Layout */
.admin-header {
	background: white;
	padding: 20px 0;
	border-bottom: 1px solid #e9ecef;
}

.card-admin {
	background: white;
	border-radius: 12px;
	border: none;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
	padding: 30px;
}

/* Inputs */
.form-label {
	font-weight: 600;
	font-size: 0.85rem;
	color: #8898aa;
	text-transform: uppercase;
}

.form-control {
	border-radius: 8px;
	padding: 12px;
	border: 1px solid #dee2e6;
}

.form-control:focus {
	border-color: #5e72e4;
	box-shadow: 0 0 0 2px rgba(94, 114, 228, 0.2);
}

/* Button */
.btn-broadcast {
	background: #5e72e4;
	color: white;
	padding: 12px 25px;
	border-radius: 8px;
	font-weight: 600;
	border: none;
}

.btn-broadcast:hover {
	background: #4863d6;
	color: white;
}
</style>
</head>
<body>

	<div class="admin-header shadow-sm mb-5">
		<div
			class="container d-flex justify-content-between align-items-center">
			<h5 class="m-0 fw-bold">
				<i class="fa-solid fa-bullhorn text-primary me-2"></i> Broadcast
				Manager
			</h5>

			<a href="${pageContext.request.contextPath}/dashboard"
				class="btn btn-sm btn-outline-secondary">Back to Dashboard</a>
		</div>
	</div>

	<div class="container">
		<div class="row justify-content-center">
			<div class="col-lg-7">
				<div class="card-admin">
					<h4 class="fw-bold mb-4">Publish New Announcement</h4>

					<form:form
						action="${pageContext.request.contextPath}/admin/broadcast/announcement/save"
						method="post" modelAttribute="newAnnouncement">
						<div class="mb-3">
							<label class="form-label">Title</label>
							<form:input path="title" class="form-control"
								placeholder="Enter announcement title" required="true" />
						</div>

						<div class="mb-4">
							<label class="form-label">Message Content</label>
							<form:textarea path="content" class="form-control" rows="6"
								placeholder="Write the announcement details here..."
								required="true" />
						</div>

						<button type="submit" class="btn btn-broadcast w-100">
							<i class="fa-solid fa-paper-plane me-2"></i> Publish Announcement
						</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>