<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>User Management | Admin</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<style>
body {
	background-color: #f4f7fe;
	font-family: 'Segoe UI', sans-serif;
}

.content-card {
	border-radius: 20px;
	border: none;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
	background: #ffffff;
	padding: 30px;
}

.modal-xl-custom {
	max-width: 800px;
}
</style>
</head>
<body>

	<div class="container py-5">
		<div class="row justify-content-center">
			<!-- 📊 MAIN CONTENT SECTION -->
			<div class="col-md-10">
				<div class="d-flex justify-content-between align-items-center mb-4">
					<h4 class="fw-bold m-0">
						<i class="fa-solid fa-users-gear text-primary me-2"></i>
						Registered Users Management
					</h4>
					<a href="${pageContext.request.contextPath}/dashboard"
						class="btn btn-outline-secondary rounded-pill"> <i
						class="fa-solid fa-arrow-left me-1"></i> Back to Dashboard Sidebar
					</a>
				</div>

				<!-- REGISTERED USERS CARD -->
				<div class="content-card mb-5">
					<div class="d-flex justify-content-between align-items-center mb-4">
						<h5 class="fw-bold text-dark mb-0">
							<i class="fa-solid fa-users text-primary me-2"></i> Registered
							Users
						</h5>

						<!-- 💡 Total Badge ဘေးမှာ Banned List ခလုတ်ကို ပူးတွဲထားရှိပုံ -->
						<div class="d-flex align-items-center gap-2">
							<button type="button"
								class="btn btn-danger btn-sm rounded-pill px-3 fw-semibold"
								data-bs-toggle="modal" data-bs-target="#bannedListModal">
								<i class="fa-solid fa-user-slash me-1"></i> View Banned List
							</button>
							<span class="badge bg-primary rounded-pill px-3 py-2 fs-7">
								Total: ${not empty users ? users.size() : 0} </span>
						</div>
					</div>

					<div class="table-responsive">
						<table class="table table-hover align-middle">
							<thead class="table-light">
								<tr>
									<th>No</th>
									<th>Username</th>
									<th>Role</th>
									<th class="text-center">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty users}">
										<c:forEach items="${users}" var="user" varStatus="count">
											<tr>
												<td class="text-muted fw-bold">#${count.index + 1}</td>
												<td class="fw-bold">${user.username}</td>
												<td><span
													class="badge ${user.role eq 'admin' ? 'bg-primary' : (user.status eq 'suspended' ? 'bg-danger' : 'bg-secondary')}">
														${user.status eq 'suspended' ? 'Suspended' : user.role} </span></td>
												<td class="text-center"><c:choose>
														<c:when
															test="${user.userId != sessionScope.loginUser.userId && user.status != 'suspended'}">
															<button type="button"
																class="btn btn-outline-warning btn-sm rounded-pill px-3"
																onclick="openBanModal('${user.userId}', '${user.username}', '${user.status}')">
																<i class="fa-solid fa-ban me-1"></i> Ban User
															</button>
														</c:when>
														<c:when test="${user.status == 'suspended'}">
															<span class="text-danger small fw-semibold"><i
																class="fa-solid fa-user-lock me-1"></i> Suspended</span>
														</c:when>
														<c:otherwise>
															<span class="text-muted small">Current Admin</span>
														</c:otherwise>
													</c:choose></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4" class="text-center text-muted py-4">No
												active system users found.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- ======================================================= -->
	<!-- 🛑 1. BANNED LIST POPUP MODAL (ခလုတ်နှိပ်မှ ပေါ်လာမည့် ဇယားကွက် မိုဒယ်) -->
	<!-- ======================================================= -->
	<div class="modal fade" id="bannedListModal" tabindex="-1"
		aria-labelledby="bannedListModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-xl-custom">
			<div class="modal-content"
				style="border-radius: 20px; border: none; box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2); border-top: 5px solid #dc3545;">
				<div class="modal-header border-0 pt-4 px-4">
					<h5 class="modal-title fw-bold text-danger"
						id="bannedListModalLabel">
						<i class="fa-solid fa-user-slash me-2"></i> Banned Accounts List
					</h5>
					<span class="badge bg-danger rounded-pill px-3 py-2 ms-3">
						Banned Total: ${not empty bannedUsers ? bannedUsers.size() : 0} </span>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body px-4 pb-4">
					<div class="table-responsive">
						<table class="table table-hover align-middle">
							<thead class="table-light text-danger">
								<tr>
									<th>No</th>
									<th>Username</th>
									<th class="text-center">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty bannedUsers}">
										<c:forEach items="${bannedUsers}" var="bu" varStatus="bCount">
											<tr>
												<td class="text-muted fw-bold">#${bCount.index + 1}</td>
												<td class="fw-bold text-dark">${bu.user.username}</td>
												<td class="text-center">
													<div class="d-flex justify-content-center gap-2">
														<!-- Reason ပြမည့် ခလုတ် (Reason အတွက် Popup ထပ်ခေါ်ပါမည်) -->
														<button type="button"
															class="btn btn-sm btn-warning px-3 rounded-pill fw-semibold"
															onclick="viewReason('${bu.user.username}', '${bu.reason}')">
															<i class="fa-solid fa-eye me-1"></i> Reason
														</button>

														<form
															action="${pageContext.request.contextPath}/admin/user/unban"
															method="POST" class="d-inline">
															<input type="hidden" name="userId"
																value="${bu.user.userId}" />
															<button type="submit"
																class="btn btn-sm btn-success px-3 rounded-pill fw-semibold"
																onclick="return confirm('Are you sure you want to unban ${bu.user.username}?');">
																<i class="fa-solid fa-user-check me-1"></i> Lift Ban
															</button>
														</form>
													</div>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="3" class="text-center text-muted py-4">
												လက်ရှိတွင် ပိတ်ပင်ထားသော အသုံးပြုသူ မရှိပါ။</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 🎯 2. VIEW BAN REASON MODAL -->
	<div class="modal fade" id="reasonModal" tabindex="-1"
		aria-labelledby="reasonModalLabel" aria-hidden="true"
		style="z-index: 1060;">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content"
				style="border-radius: 20px; border: none; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);">
				<div class="modal-header border-0 pt-4 px-4">
					<h5 class="modal-title fw-bold text-dark" id="reasonModalLabel">
						<i class="fa-solid fa-circle-info text-warning me-2"></i> Ban
						Reason
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body px-4 pb-4">
					<p class="text-muted mb-2">
						Account Name: <strong id="displayBannedUser" class="text-dark"></strong>
					</p>
					<div class="p-3 bg-light rounded-3 border">
						<span id="displayBanReason" class="fw-semibold text-secondary"></span>
					</div>
				</div>
				<div class="modal-footer border-0 px-4 pb-4">
					<button type="button" class="btn btn-secondary px-4 rounded-pill"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 3. BAN ACTION INPUT MODAL -->
	<div class="modal fade" id="banModal" tabindex="-1"
		aria-labelledby="banModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content"
				style="border-radius: 20px; border: none; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);">
				<form action="${pageContext.request.contextPath}/admin/user/ban"
					method="POST">
					<div class="modal-header border-0 pt-4 px-4">
						<h5 class="modal-title fw-bold text-danger" id="banModalLabel">
							<i class="fa-solid fa-user-lock me-2"></i> Ban User: <span
								id="targetUsername" class="text-dark"></span>
						</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body px-4">
						<input type="hidden" id="modalUserId" name="userId" /> <input
							type="hidden" id="modalCurrentStatus" name="currentStatus" /> <input
							type="hidden" name="adminId"
							value="${sessionScope.loginUser.userId}" />
						<div class="mb-3">
							<label for="banDuration"
								class="form-label fw-semibold text-secondary">Ban
								Duration</label> <select class="form-select" id="banDuration"
								name="banDuration" style="border-radius: 12px;" required>
								<option value="3 Days">3 Days</option>
								<option value="7 Days">7 Days</option>
								<option value="30 Days">30 Days</option>
								<option value="Permanent" selected>Permanent</option>
							</select>
						</div>
						<div class="mb-3">
							<label for="reason" class="form-label fw-semibold text-secondary">Reason</label>
							<textarea class="form-control" id="reason" name="reason" rows="3"
								required style="border-radius: 12px;"
								placeholder="Please provide a valid reason..."></textarea>
						</div>
					</div>
					<div class="modal-footer border-0 pb-4 px-4 gap-2">
						<button type="button" class="btn btn-light px-4 rounded-pill"
							data-bs-dismiss="modal">Cancel</button>
						<button type="submit"
							class="btn btn-danger px-4 rounded-pill fw-bold">Confirm
							Ban</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<script>
		function viewReason(username, reasonText) {
			document.getElementById('displayBannedUser').innerText = username;
			document.getElementById('displayBanReason').innerText = reasonText ? reasonText
					: "အကြောင်းပြချက် မရှိပါ။";

			// Banned list modal ထဲကနေ ဆင့်ခေါ်ရင် ထပ်ဆင့်အလုပ်လုပ်စေရန် z-index ကို trigger ပေးခြင်း
			var reasonModal = new bootstrap.Modal(document
					.getElementById('reasonModal'));
			reasonModal.show();
		}

		function openBanModal(userId, username, currentRole) {
			document.getElementById('modalUserId').value = userId;
			document.getElementById('targetUsername').innerText = username;
			document.getElementById('modalCurrentStatus').value = currentRole;

			var myModal = new bootstrap.Modal(document
					.getElementById('banModal'));
			myModal.show();
		}
	</script>
</body>
</html>