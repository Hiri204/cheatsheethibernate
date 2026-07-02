<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=yes" />
<title>${profileUser.username}·DevSheets</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body {
	background: #f0f2f5;
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
		sans-serif;
	color: #1a1a1a;
	min-height: 100vh;
}

.main-content {
	margin-left: 260px;
	padding: 40px 50px;
	min-height: 100vh;
	background: #f0f2f5;
}

/* Alert Container */
#alertContainer {
	margin-bottom: 20px;
}

.profile-header {
	background: #ffffff;
	border-radius: 20px;
	padding: 40px;
	margin-bottom: 30px;
	box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.avatar-large {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	object-fit: cover;
	border: 4px solid #f0f0f0;
}

.avatar-circle-large {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	background: linear-gradient(135deg, #1a1a1a, #444444);
	color: white;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 3rem;
	font-weight: 700;
}

.stat-box {
	text-align: center;
	padding: 10px 20px;
}

.stat-number {
	font-size: 1.5rem;
	font-weight: 700;
	color: #1a1a1a;
}

.stat-label {
	font-size: 0.75rem;
	color: #9c9c9c;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

.sheet-card {
	background: #ffffff;
	border-radius: 16px;
	border: none;
	box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
	transition: all 0.3s;
	height: 100%;
	overflow: hidden;
}

.sheet-card:hover {
	transform: translateY(-4px);
	box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.sheet-card .card-img-top {
	height: 160px;
	object-fit: cover;
}

.btn-primary-custom {
	background: #1a1a1a;
	color: #ffffff;
	border: none;
	border-radius: 20px;
	padding: 10px 24px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-primary-custom:hover {
	background: #333333;
	color: #ffffff;
}

.btn-following {
	background: #e8f5e9;
	color: #2e7d32;
	border: 1px solid #a5d6a7;
	border-radius: 20px;
	padding: 10px 24px;
	font-weight: 600;
	transition: all 0.2s;
}

.btn-following:hover {
	background: #c8e6c9;
	color: #1b5e20;
}

.btn-outline-custom {
	background: transparent;
	color: #555555;
	border: 1px solid #e8e8e8;
	border-radius: 20px;
	padding: 6px 16px;
	font-weight: 600;
	font-size: 0.85rem;
	transition: all 0.2s;
}

.btn-outline-custom:hover {
	background: #f5f5f5;
	color: #1a1a1a;
}

/* Image placeholder */
.image-placeholder {
	height: 160px;
	background: #f5f5f5;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #b0b0b0;
	font-size: 0.9rem;
	flex-direction: column;
	gap: 8px;
}

.image-placeholder i {
	font-size: 3rem;
	color: #d0d0d0;
}

@media ( max-width : 992px) {
	.main-content {
		margin-left: 0;
		padding: 20px;
	}
}

@media ( max-width : 768px) {
	.profile-header {
		padding: 20px;
	}
}
</style>
</head>
<body>

	<jsp:include page="sidebar.jsp">
		<jsp:param name="activePage" value="home" />
	</jsp:include>

	<div class="main-content">

		<!-- Alert Container -->
		<div id="alertContainer"></div>

		<div class="profile-header">
			<div class="row align-items-center">
				<div class="col-md-auto text-center text-md-start">
					<c:choose>
						<c:when
							test="${not empty profileUser.profileImg && profileUser.profileImg ne 'default-avatar.png'}">
							<img
								src="${pageContext.request.contextPath}/uploads/${profileUser.profileImg}"
								class="avatar-large" alt="${profileUser.username}"
								onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
							<div class="avatar-circle-large" style="display: none;">
								${fn:toUpperCase(fn:substring(profileUser.username, 0, 1))}</div>
						</c:when>
						<c:otherwise>
							<div class="avatar-circle-large">
								${fn:toUpperCase(fn:substring(profileUser.username, 0, 1))}</div>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="col-md">
					<h2 class="fw-bold mb-1">${profileUser.username}</h2>
					<p class="text-muted small mb-2">
						<i class="fa-regular fa-envelope me-1"></i>${profileUser.email}
					</p>
					<p class="text-muted small">
						<i class="fa-regular fa-calendar me-1"></i> Member since
						<fmt:parseDate value="${profileUser.createdAt}"
							pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />
						<fmt:formatDate value="${parsedDate}" pattern="dd MMM yyyy" />
					</p>
				</div>
				<div class="col-md-auto text-center text-md-end mt-3 mt-md-0">
					<c:choose>
						<c:when
							test="${sessionScope.loginUser.userId == profileUser.userId}">
							<a href="${pageContext.request.contextPath}/profile/view"
								class="btn btn-primary-custom"> <i
								class="fa-regular fa-pen-to-square me-1"></i> Edit Profile
							</a>
						</c:when>
						<c:otherwise>
							<button
								class="${isFollowing ? 'btn-following' : 'btn-primary-custom'}"
								onclick="toggleFollow(${profileUser.userId}, this)">
								<i
									class="fa-regular ${isFollowing ? 'fa-user-check' : 'fa-user-plus'} me-1"></i>
								${isFollowing ? 'Following' : 'Follow'}
							</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="row mt-4 pt-3 border-top">
				<div class="col-4 col-md-3">
					<div class="stat-box">
						<div class="stat-number">${sheetCount != null ? sheetCount : 0}</div>
						<div class="stat-label">Sheets</div>
					</div>
				</div>
				<div class="col-4 col-md-3">
					<div class="stat-box">
						<div class="stat-number">${followerCount != null ? followerCount : 0}</div>
						<div class="stat-label">Followers</div>
					</div>
				</div>
				<div class="col-4 col-md-3">
					<div class="stat-box">
						<div class="stat-number">${followingCount != null ? followingCount : 0}</div>
						<div class="stat-label">Following</div>
					</div>
				</div>
			</div>
		</div>

		<h4 class="fw-bold mb-3">
			<i class="fa-regular fa-file-lines me-2"></i>
			${profileUser.username}'s Sheets
		</h4>

		<div class="row g-4">
			<c:forEach items="${userSheets}" var="sheet">
				<div class="col-md-6 col-lg-4">
					<div class="sheet-card">
						<!-- 🎯 FIXED: Image path -->
						<c:choose>
							<c:when test="${not empty sheet.fileUrl}">
								<img src="${pageContext.request.contextPath}${sheet.fileUrl}"
									class="card-img-top" alt="${sheet.title}"
									onerror="this.onerror=null; this.parentElement.innerHTML='<div class=\"image-placeholder\">
								<i class=\"fa-regularfa-image\"></i>
								<span>No Image</span>
					</div>
					'" />
					</c:when>
					<c:otherwise>
						<div class="image-placeholder">
							<i class="fa-regular fa-image"></i> <span>No Image</span>
						</div>
					</c:otherwise>
					</c:choose>
					<div class="card-body p-3">
						<span class="badge bg-secondary-subtle text-secondary mb-2">
							${sheet.category != null ? sheet.category.name : 'Uncategorized'}
						</span>
						<h6 class="fw-bold mb-1">${sheet.title}</h6>
						<p class="text-muted small mb-2">
							${fn:substring(sheet.content, 0, 80)}${fn:length(sheet.content) > 80 ? '...' : ''}
						</p>
						<div class="d-flex gap-2">
							<a
								href="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}"
								class="btn btn-outline-custom flex-grow-1 text-center"> <i
								class="fa-regular fa-eye me-1"></i> View
							</a>
							<button
								onclick="copyToClipboard('${fn:escapeXml(sheet.content)}', '${fn:escapeXml(sheet.title)}')"
								class="btn btn-outline-custom flex-grow-1 text-center">
								<i class="fa-regular fa-copy me-1"></i> Copy
							</button>
						</div>
					</div>
				</div>
		</div>
		</c:forEach>
		<c:if test="${empty userSheets}">
			<div class="col-12 text-center py-5">
				<i class="fa-regular fa-file-lines fa-3x text-muted mb-3"></i>
				<p class="text-muted">No sheets published yet.</p>
			</div>
		</c:if>
	</div>
	</div>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js">
    </script>

	<script>
        function toggleFollow(userId, button) {
            if (!userId) {
                alert('User information not available.');
                return;
            }
            
            var originalHtml = button.innerHTML;
            button.disabled = true;
            button.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-1"></i> Loading...';

            fetch('${pageContext.request.contextPath}/user/follow/' + userId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.success) {
                    // Toggle button state
                    if (data.following) {
                        button.className = 'btn-following';
                        button.innerHTML = '<i class="fa-regular fa-user-check me-1"></i> Following';
                    } else {
                        button.className = 'btn-primary-custom';
                        button.innerHTML = '<i class="fa-regular fa-user-plus me-1"></i> Follow';
                    }
                    
                    // Show success message
                    showAlert('success', data.message);
                } else {
                    showAlert('danger', data.message || 'Failed to follow/unfollow.');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                showAlert('danger', 'An error occurred. Please try again.');
            })
            .finally(function() {
                button.disabled = false;
            });
        }

        function showAlert(type, message) {
            var container = document.getElementById('alertContainer');
            if (!container) {
                var newContainer = document.createElement('div');
                newContainer.id = 'alertContainer';
                newContainer.className = 'mb-4';
                document.querySelector('.main-content').prepend(newContainer);
                container = newContainer;
            }
            
            var iconClass = type === 'success' ? 'fa-circle-check text-success' : 'fa-circle-exclamation text-danger';
            
            var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show shadow-sm" ' +
                'role="alert" style="border-radius: 12px;">' +
                '<div class="d-flex align-items-center gap-2">' +
                '<i class="fa-solid ' + iconClass + ' fs-5"></i>' +
                '<span>' + message + '</span>' +
                '</div>' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>' +
                '</div>';
            
            container.innerHTML = alertHtml;
            
            // Auto dismiss after 3 seconds
            setTimeout(function() {
                var alert = container.querySelector('.alert');
                if (alert) {
                    var bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            }, 3000);
        }

        function copyToClipboard(content, title) {
            if (!content || content.trim() === '' || content === 'null') {
                alert("ကူးယူရန် အကြောင်းအရာ (Content) မရှိပါဗျာ။");
                return;
            }
            
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(content).then(function() {
                    showAlert('success', '"' + title + '" content copied to clipboard!');
                }).catch(function(err) {
                    console.error('Could not copy text: ', err);
                    alert("Copy ကူးယူခြင်း မအောင်မြင်ပါဗျာ။");
                });
            } else {
                // Fallback for older browsers
                var textarea = document.createElement('textarea');
                textarea.value = content;
                textarea.style.position = 'fixed';
                textarea.style.opacity = '0';
                document.body.appendChild(textarea);
                textarea.select();
                try {
                    document.execCommand('copy');
                    showAlert('success', '"' + title + '" content copied to clipboard!');
                } catch (err) {
                    alert("Copy ကူးယူခြင်း မအောင်မြင်ပါဗျာ။");
                }
                document.body.removeChild(textarea);
            }
        }
    </script>
</body>
</html>