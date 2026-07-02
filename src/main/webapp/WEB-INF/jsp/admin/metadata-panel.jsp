<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Metadata Management | Admin</title>
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

/* Header */
.admin-header {
	background: white;
	padding: 20px 0;
	border-bottom: 1px solid #e9ecef;
	margin-bottom: 30px;
}

/* Card */
.admin-card {
	background: white;
	border-radius: 12px;
	border: none;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
	padding: 25px;
}

/* Table */
.table thead {
	background: #f4f7fe;
	color: #8898aa;
	text-transform: uppercase;
	font-size: 0.75rem;
	letter-spacing: 1px;
}

/* Buttons Style */
.btn-edit {
	color: #5e72e4;
	background: #f0f3ff;
	border: none;
	padding: 5px 12px;
	border-radius: 6px;
	font-size: 0.8rem;
	font-weight: 600;
	text-decoration: none;
	margin-right: 5px;
}

.btn-edit:hover {
	background: #5e72e4;
	color: white;
}

.btn-delete {
	color: #f5365c;
	background: #fff5f7;
	border: none;
	padding: 5px 12px;
	border-radius: 6px;
	font-size: 0.8rem;
	font-weight: 600;
	text-decoration: none;
}

.btn-delete:hover {
	background: #f5365c;
	color: white;
}

/* Form */
.input-group .form-control {
	border-radius: 8px 0 0 8px;
	border: 1px solid #dee2e6;
}

.btn-add {
	background: #5e72e4;
	color: white;
	border-radius: 0 8px 8px 0;
	border: none;
	padding: 0 20px;
}
</style>
</head>
<body>

	<div class="admin-header shadow-sm">
		<div
			class="container d-flex justify-content-between align-items-center">
			<h5 class="m-0 fw-bold">
				<i class="fa-solid fa-list-ul text-primary me-2"></i> Metadata
				Configuration
			</h5>
			<a href="${pageContext.request.contextPath}/dashboard"
				class="btn btn-sm btn-outline-secondary">Back to Dashboard</a>
		</div>
	</div>

	<div class="container">
		<div class="row g-4">

			<div class="col-lg-6">
				<div class="admin-card">
					<h6 class="fw-bold mb-3">
						<i class="fa-solid fa-folder me-2 text-primary"></i> Categories
					</h6>

					<form:form
						action="${pageContext.request.contextPath}/admin/metadata/category/save"
						method="post" modelAttribute="newCategory" class="mb-3">
						<div class="input-group">
							<form:input path="name" class="form-control"
								placeholder="New Category Name..." required="true" />
							<button type="submit" class="btn btn-add">Add</button>
						</div>
					</form:form>

					<table class="table align-middle">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th class="text-end">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="cat" items="${categories}">
								<tr>
									<td><span class="badge bg-light text-dark">#${cat.categoryId}</span></td>
									<td class="fw-semibold text-dark">${cat.name}</td>
									<td class="text-end">
										<button type="button" class="btn btn-edit"
											data-bs-toggle="modal" data-bs-target="#editCategoryModal"
											data-id="${cat.categoryId}" data-name="${cat.name}">
											<i class="fa-solid fa-pen"></i> Edit
										</button> <a
										href="${pageContext.request.contextPath}/admin/metadata/category/delete/${cat.categoryId}"
										class="btn btn-delete"
										onclick="return confirm('သေချာပါသလားဗျာ?')"> <i
											class="fa-solid fa-trash"></i> Delete
									</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

			<div class="col-lg-6">
				<div class="admin-card">
					<h6 class="fw-bold mb-3">
						<i class="fa-solid fa-tags me-2 text-primary"></i> Meta Tags
					</h6>

					<form:form
						action="${pageContext.request.contextPath}/admin/metadata/tag/save"
						method="post" modelAttribute="newTag" class="mb-3">
						<div class="input-group">
							<form:input path="name" class="form-control"
								placeholder="New Tag Name..." required="true" />
							<button type="submit" class="btn btn-add">Add Tag</button>
						</div>
					</form:form>

					<table class="table align-middle">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th class="text-end">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="tag" items="${tags}">
								<tr>
									<td><span class="badge bg-light text-dark">#${tag.tagId}</span></td>
									<td class="fw-semibold text-dark">${tag.name}</td>
									<td class="text-end">
										<button type="button" class="btn btn-edit"
											data-bs-toggle="modal" data-bs-target="#editTagModal"
											data-id="${tag.tagId}" data-name="${tag.name}">
											<i class="fa-solid fa-pen"></i> Edit
										</button> <a
										href="${pageContext.request.contextPath}/admin/metadata/tag/delete/${tag.tagId}"
										class="btn btn-delete"
										onclick="return confirm('သေချာပါသလားဗျာ?')"> <i
											class="fa-solid fa-trash"></i> Delete
									</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="editCategoryModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content style-radius border-0 shadow">
				<div class="modal-header">
					<h5 class="modal-title fw-bold">
						<i class="fa-solid fa-pen-to-square text-primary me-2"></i> Edit
						Category
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="editCategoryForm" action="" method="post">
					<div class="modal-body">
						<div class="mb-3">
							<label class="form-label fw-bold small text-muted">Category
								Name</label> <input type="text" name="name" id="editCategoryName"
								class="form-control rounded-3" required />
						</div>
					</div>
					<div class="modal-footer border-0">
						<button type="button"
							class="btn btn-sm btn-light rounded-pill px-3"
							data-bs-dismiss="modal">Cancel</button>
						<button type="submit"
							class="btn btn-sm btn-primary rounded-pill px-4 fw-bold">Update
							Category</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div class="modal fade" id="editTagModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content style-radius border-0 shadow">
				<div class="modal-header">
					<h5 class="modal-title fw-bold">
						<i class="fa-solid fa-pen-to-square text-primary me-2"></i> Edit
						Meta Tag
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="editTagForm" action="" method="post">
					<div class="modal-body">
						<div class="mb-3">
							<label class="form-label fw-bold small text-muted">Tag
								Name</label> <input type="text" name="name" id="editTagName"
								class="form-control rounded-3" required />
						</div>
					</div>
					<div class="modal-footer border-0">
						<button type="button"
							class="btn btn-sm btn-light rounded-pill px-3"
							data-bs-dismiss="modal">Cancel</button>
						<button type="submit"
							class="btn btn-sm btn-primary rounded-pill px-4 fw-bold">Update
							Tag</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<script>
		// 💡 Java Web URL Context Path ကို Dynamic ယူရန်
		const ctxPath = "${pageContext.request.contextPath}";

		// 🎯 Category Edit ခလုတ်နှိပ်လျှင် Data များအား Modal Form ထဲဖြည့်သွင်းပေးခြင်း Logic
		const editCategoryModal = document.getElementById('editCategoryModal');
		editCategoryModal.addEventListener('show.bs.modal', function(event) {
			const button = event.relatedTarget;
			const catId = button.getAttribute('data-id');
			const catName = button.getAttribute('data-name');

			// Form action url ကို ID အလိုက် ပြောင်းလဲပေးခြင်း
			document.getElementById('editCategoryForm').action = ctxPath
					+ "/admin/metadata/category/update/" + catId;
			// Input box ထဲသို့ လက်ရှိနာမည်ဟောင်း ထည့်ပေးခြင်း
			document.getElementById('editCategoryName').value = catName;
		});

		// 🎯 Tag Edit ခလုတ်နှိပ်လျှင် Data များအား Modal Form ထဲဖြည့်သွင်းပေးခြင်း Logic
		const editTagModal = document.getElementById('editTagModal');
		editTagModal.addEventListener('show.bs.modal', function(event) {
			const button = event.relatedTarget;
			const tagId = button.getAttribute('data-id');
			const tagName = button.getAttribute('data-name');

			// Form action url ကို ID အလိုက် ပြောင်းလဲပေးခြင်း
			document.getElementById('editTagForm').action = ctxPath
					+ "/admin/metadata/tag/update/" + tagId;
			// Input box ထဲသို့ လက်ရှိနာမည်ဟောင်း ထည့်ပေးခြင်း
			document.getElementById('editTagName').value = tagName;
		});
	</script>
</body>
</html>