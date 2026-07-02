<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Edit Cheat Sheet · DevSheets</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.css" />
<style>
body {
	background-color: #f4f7fe;
	font-family: 'Segoe UI', sans-serif;
	color: #32325d;
}

.main-content {
	padding: 40px;
}

.form-card {
	border-radius: 20px;
	border: none;
	padding: 35px;
	background: #ffffff;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.btn-custom {
	border-radius: 50px;
	font-weight: 600;
	padding: 8px 25px;
}

.form-label {
	font-weight: 600;
	font-size: 0.8rem;
	color: #6c757d;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

.form-control, .form-select {
	border-radius: 10px;
	padding: 12px 15px;
	border: 1px solid #ced4da;
}

.tags-container {
	border: 1px solid #ced4da;
	background-color: #fcfcfd;
	border-radius: 10px;
	padding: 15px;
	max-height: 160px;
	overflow-y: auto;
}

.preview-container {
	margin-top: 15px;
	text-align: center;
}

.preview-img {
	max-width: 100%;
	max-height: 250px;
	border-radius: 12px;
	border: 2px dashed #ced4da;
}

.img-container {
	max-height: 400px;
	overflow: hidden;
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>
</head>
<body>
	<div class="main-content container">
		<div class="mb-4">
			<h3 class="fw-bold text-dark mb-1">Edit Cheat Sheet</h3>
			<p class="text-muted">လိုအပ်သော အချက်အလက်နှင့်
				ပုံရိပ်ဟောင်းများအား ပြင်ဆင်နိုင်ပါသည် ဆရာ။</p>
		</div>

		<div class="form-card">
			<form:form id="editSheetForm"
				action="${pageContext.request.contextPath}/cheatsheets/save"
				method="post" modelAttribute="cheatSheet"
				enctype="multipart/form-data">

				<form:hidden path="id" />

				<div class="row">
					<div class="col-md-12 mb-4">
						<label class="form-label">Cheat Sheet Title <span
							class="text-danger">*</span></label>
						<form:input path="title" class="form-control" required="required" />
					</div>

					<div class="col-md-6 mb-4">
						<label class="form-label">Select Category <span
							class="text-danger">*</span></label>
						<form:select path="category.categoryId" class="form-select"
							required="required">
							<c:forEach items="${categories}" var="cat">
								<option value="${cat.categoryId}"
									${cat.categoryId eq cheatSheet.category.categoryId ? 'selected' : ''}>${cat.name}</option>
							</c:forEach>
						</form:select>
					</div>

					<div class="col-md-6 mb-4">
						<label class="form-label">Visibility Status</label>
						<form:select path="status" class="form-select">
							<option value="draft"
								${cheatSheet.status eq 'draft' ? 'selected' : ''}>Draft
								(Keep Private)</option>
							<option value="published"
								${cheatSheet.status eq 'published' ? 'selected' : ''}>Published
								(Make Public)</option>
						</form:select>
					</div>

					<div class="col-md-12 mb-4">
						<label class="form-label">Select Associated Tags</label>
						<div class="tags-container">
							<div class="row g-2">
								<c:forEach var="tag" items="${tags}">
									<div class="col-sm-4 col-md-3">
										<div class="form-check m-0">
											<input class="form-check-input" type="checkbox" name="tagIds"
												value="${tag.tagId}" id="tag_${tag.tagId}"> <label
												class="form-check-label text-dark fw-semibold small"
												for="tag_${tag.tagId}">${tag.name}</label>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>

					<div class="col-md-12 mb-4">
						<label class="form-label">Upload New Photo / Diagram <small
							class="text-muted">(ပုံအသစ်မချိန်းလိုပါက ထည့်ရန်မလိုပါ)</small></label> <input
							type="file" id="fileInput" class="form-control" accept="image/*" />

						<div class="preview-container" id="previewContainer">
							<label class="form-label d-block mt-3 text-success">Active
								Image Preview</label> <img id="croppedPreview" class="preview-img"
								src="${pageContext.request.contextPath}${cheatSheet.fileUrl}"
								alt="Preview">
						</div>
					</div>

					<div class="col-md-12 mb-4">
						<label class="form-label">Syntax Document Content <span
							class="text-danger">*</span></label>
						<form:textarea path="content" class="form-control" rows="6"
							required="required" />
					</div>
				</div>

				<div class="d-flex gap-3 justify-content-end border-top pt-4 mt-2">
					<a href="${pageContext.request.contextPath}/cheatsheets/my-sheets"
						class="btn btn-outline-secondary btn-custom">Cancel</a>
					<button type="submit"
						class="btn btn-primary btn-custom text-white shadow-sm">
						<i class="fa-solid fa-cloud-arrow-up me-1"></i> Update Changes
					</button>
				</div>
			</form:form>
		</div>
	</div>

	<div class="modal fade" id="cropModal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog modal-lg modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header bg-dark text-white">
					<h5 class="modal-title">
						<i class="fa-solid fa-crop-simple me-2"></i>Crop & Zoom Image
					</h5>
					<button type="button" class="btn-close btn-close-white"
						data-bs-dismiss="modal" id="btnCloseModal"></button>
				</div>
				<div class="modal-body p-0">
					<div class="img-container">
						<img id="imageToCrop" src="">
					</div>
				</div>
				<div class="modal-footer bg-light">
					<button type="button" class="btn btn-secondary me-auto"
						id="btnZoomIn">
						<i class="fa-solid fa-magnifying-glass-plus"></i>
					</button>
					<button type="button" class="btn btn-secondary me-2"
						id="btnZoomOut">
						<i class="fa-solid fa-magnifying-glass-minus"></i>
					</button>
					<button type="button" class="btn btn-primary" id="btnCrop">Crop
						& Select</button>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.js"></script>
	<script>
        let cropper; let croppedBlob = null;
        const fileInput = document.getElementById('fileInput');
        const imageToCrop = document.getElementById('imageToCrop');
        const cropModal = new bootstrap.Modal(document.getElementById('cropModal'));
        const form = document.getElementById('editSheetForm');

        fileInput.addEventListener('change', function (e) {
            if (e.target.files.length > 0) {
                const reader = new FileReader();
                reader.onload = function (event) { imageToCrop.src = event.target.result; cropModal.show(); };
                reader.readAsDataURL(e.target.files[0]);
            }
        });

        document.getElementById('cropModal').addEventListener('shown.bs.modal', function () {
            cropper = new Cropper(imageToCrop, { aspectRatio: 16 / 9, viewMode: 1, autoCropArea: 1, movable: true, zoomable: true });
        });

        document.getElementById('btnCrop').addEventListener('click', function () {
            const canvas = cropper.getCroppedCanvas({ width: 800, height: 450 });
            canvas.toBlob(function (blob) {
                croppedBlob = blob;
                document.getElementById('croppedPreview').src = URL.createObjectURL(blob);
                cropModal.hide();
            }, 'image/jpeg');
        });

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(form);
            if (croppedBlob) {
                formData.set('imageFile', croppedBlob, 'updated_image.jpg');
            } else {
                // ပုံအသစ်မရွေးထားရင် Controller error မတက်အောင် empty file ဆောက်ပေးခြင်း
                formData.set('imageFile', new Blob(), '');
            }
            fetch(form.action, { method: 'POST', body: formData })
            .then(res => window.location.href = "${pageContext.request.contextPath}/cheatsheets/my-cheatsheets");
        });
    </script>
</body>
</html>