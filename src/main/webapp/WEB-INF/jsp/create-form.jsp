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
<title><c:choose>
		<c:when test="${not empty cheatSheet.id}">Edit Workspace Sheet</c:when>
		<c:otherwise>Create New Sheet</c:otherwise>
	</c:choose> · DevSheets</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<!-- CropperJS CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.css" />

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

/* Modern Minimalist Card (Floe Style) */
.form-card {
	background: #ffffff;
	border-radius: 24px;
	border: 1px solid #f0f0f0;
	padding: 40px;
	box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.01);
	max-width: 1200px;
}

.page-title {
	font-size: 1.6rem;
	font-weight: 700;
	letter-spacing: -0.5px;
	color: #1a1a1a;
}

/* Form Control Aesthetics */
.form-label {
	font-weight: 600;
	font-size: 0.85rem;
	color: #555555;
	margin-bottom: 8px;
}

.form-control, .form-select {
	border-radius: 12px;
	border: 1px solid #ededed;
	background-color: #f9f9f9;
	padding: 12px 16px;
	font-size: 0.95rem;
	color: #1a1a1a;
	transition: all 0.2s ease;
}

.form-control:focus, .form-select:focus {
	background-color: #ffffff;
	border-color: #d0d0d0;
	box-shadow: none;
	outline: none;
}

/* Tags Container */
.tags-container {
	border: 1px solid #ededed;
	background-color: #f9f9f9;
	border-radius: 12px;
	padding: 16px;
	max-height: 160px;
	overflow-y: auto;
}

.tags-container::-webkit-scrollbar {
	width: 4px;
}

.tags-container::-webkit-scrollbar-thumb {
	background: #e8e8e8;
	border-radius: 4px;
}

/* Image Upload Box Accent */
.upload-box {
	border: 2px dashed #e0e0e0;
	border-radius: 16px;
	padding: 30px;
	text-align: center;
	background: #fafafa;
	cursor: pointer;
	transition: all 0.2s;
}

.upload-box:hover {
	border-color: #adadad;
	background: #f5f5f5;
}

/* Modern Rounded Buttons */
.btn-action {
	padding: 12px 28px;
	border-radius: 20px;
	font-weight: 600;
	font-size: 0.95rem;
	transition: all 0.2s;
}

.btn-save {
	background-color: #1a1a1a;
	color: #ffffff;
	border: none;
}

.btn-save:hover {
	background-color: #333333;
	color: #ffffff;
}

.btn-cancel {
	background-color: transparent;
	color: #757575;
	border: 1px solid #ededed;
}

.btn-cancel:hover {
	background-color: #f5f5f5;
	color: #1a1a1a;
}

/* Tags area decoration */
.tag-info-text {
	font-size: 0.8rem;
	color: #9c9c9c;
	margin-top: 4px;
}

/* Cropper container styling */
.cropper-container-wrapper {
	max-height: 400px;
	border-radius: 14px;
	overflow: hidden;
	background: #f0f0f0;
}

/* Error messages */
.text-danger {
	color: #dc3545 !important;
	font-size: 0.875rem;
	margin-top: 4px;
}

/* Responsive adjustments */
@media ( max-width : 992px) {
	.main-content {
		margin-left: 0;
		padding: 20px;
	}
	.form-card {
		padding: 24px;
	}
}

@media ( max-width : 768px) {
	.main-content {
		padding: 15px;
	}
	.form-card {
		padding: 16px;
	}
	.page-title {
		font-size: 1.2rem;
	}
}
</style>
</head>
<body>

	<!-- Include sidebar with active page parameter -->
	<jsp:include page="sidebar.jsp">
		<jsp:param name="activePage" value="create" />
	</jsp:include>

	<!-- Main Content -->
	<div class="main-content">

		<!-- Page Header -->
		<div
			class="d-flex flex-wrap justify-content-between align-items-center mb-4">
			<div>
				<h3 class="page-title">
					<c:choose>
						<c:when test="${not empty cheatSheet.id}">
							<i class="fa-regular fa-pen-to-square me-2"></i>Edit Workspace Sheet
                        </c:when>
						<c:otherwise>
							<i class="fa-regular fa-file-lines me-2"></i>Create New Sheet
                        </c:otherwise>
					</c:choose>
				</h3>
				<p class="text-muted small mt-1">
					<c:choose>
						<c:when test="${not empty cheatSheet.id}">
                            Update your cheat sheet details
                        </c:when>
						<c:otherwise>
                            Fill in the details to create a new cheat sheet
                        </c:otherwise>
					</c:choose>
				</p>
			</div>
			<div>
				<a
					href="${pageContext.request.contextPath}/cheatsheets/my-cheatsheets"
					class="btn btn-cancel btn-action"> <i
					class="fa-solid fa-arrow-left me-2"></i>Back to List
				</a>
			</div>
		</div>

		<!-- Form Card -->
		<div class="form-card">
			<form:form modelAttribute="cheatSheet"
				action="${pageContext.request.contextPath}/cheatsheets/save"
				method="POST" enctype="multipart/form-data" id="cheatsheetForm">

				<form:hidden path="id" />

				<div class="row g-4">
					<!-- Sheet Title -->
					<div class="col-md-6">
						<label for="title" class="form-label"> <i
							class="fa-regular fa-rectangle-list me-1"></i>Sheet Title <span
							class="text-danger">*</span>
						</label>
						<form:input path="title" id="title" class="form-control"
							placeholder="Enter workspace title..." required="required" />
						<form:errors path="title" cssClass="text-danger" />
					</div>

					<!-- Category -->
					<div class="col-md-6">
						<label for="categoryId" class="form-label"> <i
							class="fa-regular fa-folder me-1"></i>Category Folder <span
							class="text-danger">*</span>
						</label> <select name="categoryId" id="categoryId" class="form-select"
							required="required">
							<option value="">-- Choose Folder Level --</option>
							<c:forEach items="${categories}" var="cat">
								<option value="${cat.categoryId}"
									${cheatSheet.category != null && cheatSheet.category.categoryId == cat.categoryId ? 'selected' : ''}>
									${cat.name}</option>
							</c:forEach>
						</select>
						<form:errors path="category" cssClass="text-danger" />
					</div>

					<!-- Status -->
					<div class="col-md-6">
						<label for="status" class="form-label"> <i
							class="fa-regular fa-circle me-1"></i>Visibility Status
						</label>
						<form:select path="status" id="status" class="form-select">
							<form:option value="draft">Draft (Keep Private)</form:option>
							<form:option value="published">Published (Make Public)</form:option>
							<form:option value="archived">Archived</form:option>
						</form:select>
						<form:errors path="status" cssClass="text-danger" />
					</div>

					<!-- Tags -->
					<div class="col-md-6">
						<label class="form-label"> <i
							class="fa-regular fa-tags me-1"></i>Select Associated Tags
						</label>
						<div class="tags-container">
							<div class="row g-2">
								<c:forEach var="tag" items="${tags}">
									<div class="col-sm-6 col-md-4">
										<div class="form-check m-0">
											<input class="form-check-input" type="checkbox" name="tagIds"
												value="${tag.tagId}" id="tag_${tag.tagId}"
												<c:forEach var="existingTag" items="${cheatSheet.tags}">
                                                    ${existingTag.tagId eq tag.tagId ? 'checked' : ''}
                                                </c:forEach> />
											<label class="form-check-label text-dark fw-semibold small"
												for="tag_${tag.tagId}"> <i
												class="fa-solid fa-tag fa-xs me-1 text-muted"></i>
												${tag.name}
											</label>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
						<c:if test="${empty tags}">
							<div class="text-muted small mt-2">
								<i class="fa-regular fa-circle-info me-1"></i> No tags
								available. Create tags in the admin panel.
							</div>
						</c:if>
						<form:errors path="tags" cssClass="text-danger" />
					</div>

					<!-- Content -->
					<div class="col-12">
						<label for="content" class="form-label"> <i
							class="fa-regular fa-file-lines me-1"></i>Content <span
							class="text-danger">*</span>
						</label>
						<form:textarea path="content" id="content" class="form-control"
							rows="10" placeholder="Write your cheat sheet content here..."
							required="required" />
						<form:errors path="content" cssClass="text-danger" />
					</div>

					<!-- File Upload (Optional) -->
					<div class="col-md-5">
						<label class="form-label"> <i
							class="fa-regular fa-image me-1"></i>Resource File
						</label>
						<div class="upload-box"
							onclick="document.getElementById('fileInput').click();">
							<i
								class="fa-solid fa-cloud-arrow-up fs-2 text-muted mb-2 d-block"></i>
							<p class="mb-0 text-secondary fw-medium"
								style="font-size: 0.9rem;">Click to upload file</p>
							<small class="text-muted">Supports JPG, PNG, PDF (Max
								5MB)</small> <input type="file" name="imageFile" id="fileInput"
								accept="image/*,.pdf" style="display: none;" />
						</div>

						<c:if test="${not empty cheatSheet.fileUrl}">
							<div class="mt-3 p-2 border rounded-4 text-center bg-light">
								<small class="text-muted d-block mb-2">Current File</small> <a
									href="${pageContext.request.contextPath}/uploads/${cheatSheet.fileUrl}"
									target="_blank" class="text-decoration-none"> <i
									class="fa-regular fa-file me-2"></i>${cheatSheet.fileUrl}
								</a>
							</div>
						</c:if>
					</div>

					<!-- Cropper (for images only) -->
					<div class="col-md-7 d-none" id="cropperCardWrapper">
						<label class="form-label text-dark fw-bold"> <i
							class="fa-solid fa-crop-simple me-1"></i> Adjust Image View
							(Crop)
						</label>
						<div class="cropper-container-wrapper p-2">
							<img id="imageCropCanvas" src="" alt="Source Preview"
								style="max-width: 100%; display: block;" />
						</div>
						<div class="d-flex gap-2 mt-2">
							<button type="button"
								class="btn btn-sm btn-dark rounded-pill px-3" id="btnRotateLeft">
								<i class="fa-solid fa-rotate-left"></i> Rotate
							</button>
							<button type="button"
								class="btn btn-sm btn-outline-secondary rounded-pill px-3"
								id="btnResetCrop">
								<i class="fa-solid fa-arrow-rotate-left"></i> Reset
							</button>
						</div>
					</div>

					<!-- Form Actions -->
					<div
						class="col-12 border-top pt-4 mt-4 d-flex flex-wrap justify-content-end gap-3">
						<a
							href="${pageContext.request.contextPath}/cheatsheets/my-cheatsheets"
							class="btn btn-cancel btn-action"> <i
							class="fa-solid fa-xmark me-1"></i>Cancel
						</a>
						<button type="submit" class="btn btn-save btn-action"
							id="btnSubmitForm">
							<i class="fa-regular fa-floppy-disk me-1"></i>
							<c:choose>
								<c:when test="${not empty cheatSheet.id}">Update Sheet</c:when>
								<c:otherwise>Save Resource Sheet</c:otherwise>
							</c:choose>
						</button>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js">
    </script>
	<!-- CropperJS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.js">
    </script>

	<script>
        document.addEventListener('DOMContentLoaded', function() {
            let cropper = null;
            let croppedBlob = null;
            const fileInput = document.getElementById('fileInput');
            const cropWrapper = document.getElementById('cropperCardWrapper');
            const cropImage = document.getElementById('imageCropCanvas');

            // Initialize Cropper when file is selected
            fileInput.addEventListener('change', function(e) {
                const files = e.target.files;
                if (files && files.length > 0) {
                    const file = files[0];
                    
                    // Validate file type
                    if (!file.type.match('image.*')) {
                        // If not an image, just upload as file without cropping
                        cropWrapper.classList.add('d-none');
                        if (cropper) {
                            cropper.destroy();
                            cropper = null;
                        }
                        return;
                    }
                    
                    // Validate file size (max 5MB)
                    if (file.size > 5 * 1024 * 1024) {
                        alert('File size should not exceed 5MB');
                        this.value = '';
                        return;
                    }
                    
                    const reader = new FileReader();
                    reader.onload = function(event) {
                        if (cropper) {
                            cropper.destroy();
                            cropper = null;
                        }
                        cropImage.src = event.target.result;
                        cropWrapper.classList.remove('d-none');
                        
                        // Initialize cropper after image loads
                        setTimeout(function() {
                            cropper = new Cropper(cropImage, {
                                viewMode: 1,
                                autoCropArea: 0.8,
                                responsive: true,
                                restore: false,
                                checkOrientation: false,
                                modal: true,
                                guides: true,
                                center: true,
                                highlight: false,
                                cropBoxMovable: true,
                                cropBoxResizable: true,
                                toggleDragModeOnDblclick: false,
                                aspectRatio: 16 / 9
                            });
                        }, 100);
                    };
                    reader.readAsDataURL(file);
                }
            });

            // Rotate image
            document.getElementById('btnRotateLeft').addEventListener('click', function() {
                if (cropper) {
                    cropper.rotate(-90);
                }
            });

            // Reset crop
            document.getElementById('btnResetCrop').addEventListener('click', function() {
                if (cropper) {
                    cropper.reset();
                }
            });

            // Handle form submission with cropped image
            document.getElementById('cheatsheetForm').addEventListener('submit', function(e) {
                e.preventDefault();
                const form = this;

                // Validate required fields
                const title = document.getElementById('title').value.trim();
                const content = document.getElementById('content').value.trim();
                const categoryId = document.getElementById('categoryId').value;
                
                if (!title) {
                    alert('Please enter a sheet title.');
                    document.getElementById('title').focus();
                    return;
                }
                
                if (!content) {
                    alert('Please enter the content.');
                    document.getElementById('content').focus();
                    return;
                }
                
                if (!categoryId) {
                    alert('Please select a category.');
                    document.getElementById('categoryId').focus();
                    return;
                }

                const submitBtn = document.getElementById('btnSubmitForm');
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-1"></i> Saving...';

                if (cropper) {
                    // Get cropped image as blob
                    cropper.getCroppedCanvas({
                        maxWidth: 1920,
                        maxHeight: 1080,
                        imageSmoothingQuality: 'high'
                    }).toBlob(function(blob) {
                        croppedBlob = blob;
                        submitFormWithData(form, submitBtn);
                    }, 'image/jpeg', 0.92);
                } else {
                    submitFormWithData(form, submitBtn);
                }
            });

            function submitFormWithData(form, submitBtn) {
                const formData = new FormData(form);
                
                // Handle image upload
                if (croppedBlob) {
                    formData.set('imageFile', croppedBlob, 'cropped_cheatsheet.jpg');
                } else if (fileInput.files.length === 0) {
                    formData.delete('imageFile');
                }
                
                // Send AJAX request
                fetch(form.action, {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    } else if (response.ok) {
                        window.location.href = "${pageContext.request.contextPath}/cheatsheets/my-cheatsheets";
                    } else {
                        return response.text().then(text => {
                            throw new Error(text || 'Server error occurred');
                        });
                    }
                })
                .catch(error => {
                    console.error('Error submitting form:', error);
                    alert('သိမ်းဆည်းစဉ် အမှားအယွင်း ဖြစ်ပွားခဲ့ပါသည်။ ကျေးဇူးပြု၍ နောက်မှ ထပ်မံကြိုးစားပါ။');
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = '<i class="fa-regular fa-floppy-disk me-1"></i> Save Resource Sheet';
                });
            }

            // Cleanup cropper when leaving page
            window.addEventListener('beforeunload', function() {
                if (cropper) {
                    cropper.destroy();
                    cropper = null;
                }
            });
        });
    </script>
</body>
</html>