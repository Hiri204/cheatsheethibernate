<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${sheet.title}·DevSheets</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<style>
body {
	background-color: #f8fafc;
	font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
	color: #334155;
}

.detail-container {
	max-width: 950px;
	margin: 40px auto;
	padding: 0 24px;
}

.main-card {
	border-radius: 24px;
	border: none;
	box-shadow: 0 4px 20px rgba(15, 23, 42, 0.05);
	background: #fff;
	overflow: hidden;
}

.author-box {
	display: flex;
	align-items: center;
	gap: 12px;
}

.author-img {
	width: 44px;
	height: 44px;
	border-radius: 50%;
	object-fit: cover;
	border: 2px solid #e2e8f0;
}

.content-area {
	background: #0f172a;
	border-radius: 16px;
	padding: 30px;
	font-family: 'Fira Code', 'Courier New', monospace;
	white-space: pre-wrap;
	font-size: 0.95rem;
	color: #f8fafc;
	border: none;
	box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.2);
}

.tag-badge {
	font-size: 0.75rem;
	background-color: #f1f5f9;
	color: #627d98;
	border-radius: 8px;
	padding: 6px 12px;
	font-weight: 600;
	text-decoration: none;
	transition: all 0.2s;
}

.tag-badge:hover {
	background-color: #e2e8f0;
	color: #334155;
}

/* 🌟 Star Rating Custom Design */
.rating-stars-input {
	display: flex;
	flex-direction: row-reverse;
	justify-content: flex-end;
	gap: 6px;
}

.rating-stars-input input {
	display: none;
}

.rating-stars-input label {
	font-size: 2rem;
	color: #cbd5e1;
	cursor: pointer;
	transition: transform 0.2s, color 0.1s;
}

.rating-stars-input label:hover {
	transform: scale(1.15);
}

.rating-stars-input input:checked ~ label, .rating-stars-input label:hover,
	.rating-stars-input label:hover ~ label {
	color: #eab308;
}

.review-avatar {
	width: 42px;
	height: 42px;
	border-radius: 50%;
	font-size: 1rem;
	font-weight: 600;
}

.review-item {
	transition: background-color 0.2s;
	border-radius: 12px;
}

.review-item:hover {
	background-color: #f8fafc;
}

/* 💬 Comment Tree Timeline Line Decoration */
.comment-node {
	background: #f8fafc;
	border-radius: 16px;
	border: 1px solid #f1f5f9;
}

.nested-reply-line {
	border-left: 2px solid #cbd5e1;
	padding-left: 20px;
	margin-left: 20px;
}
</style>
</head>
<body>

	<div class="detail-container">
		<div class="mb-4">
			<a href="javascript:history.back()"
				class="btn btn-white btn-sm rounded-pill px-3 border shadow-sm fw-semibold text-secondary bg-white">
				<i class="fa-solid fa-arrow-left me-1"></i> Back to List
			</a>
		</div>

		<div class="card main-card mb-4">
			<c:if test="${not empty sheet.fileUrl}">
				<img src="${pageContext.request.contextPath}${sheet.fileUrl}"
					class="w-100" style="max-height: 380px; object-fit: cover;"
					alt="Cover">
			</c:if>

			<div class="card-body p-4 p-md-5">
				<div class="d-flex justify-content-between align-items-center mb-4">
					<span
						class="badge bg-primary-subtle text-primary rounded-pill px-3 py-2 fw-semibold">
						<i class="fa-solid fa-folder-open me-1"></i>
						${sheet.category.name}
					</span>

					<div
						class="d-flex align-items-center gap-2 bg-warning-subtle text-warning-emphasis px-3 py-1.5 rounded-pill fw-bold">
						<i class="fa-solid fa-star text-warning"></i> ${avgRating} <span
							class="text-muted small fw-normal">/ 5.0</span>
					</div>
				</div>

				<h1 class="fw-bold text-dark mb-4" style="letter-spacing: -0.5px;">${sheet.title}</h1>

				<div
					class="d-flex flex-wrap justify-content-between align-items-center gap-3 bg-light p-3 rounded-4 mb-4">
					<div class="author-box">
						<c:choose>
							<c:when test="${not empty sheet.user.profileImg}">
								<img
									src="${pageContext.request.contextPath}${sheet.user.profileImg}"
									class="author-img" alt="Profile">
							</c:when>
							<c:otherwise>
								<div
									class="bg-primary text-white d-flex align-items-center justify-content-center review-avatar shadow-sm">
									${fn:substring(sheet.user.username, 0, 1).toUpperCase()}</div>
							</c:otherwise>
						</c:choose>
						<div>
							<h6 class="mb-0 fw-bold text-dark">${sheet.user.username}</h6>
							<span class="text-muted small">Contributor</span>
						</div>
					</div>

					<div class="text-secondary small">
						<i class="fa-regular fa-calendar-check me-1 text-primary"></i> <strong>Posted
							on:</strong>
						<c:set var="cleanCreated"
							value="${fn:replace(sheet.createdAt, 'T', ' ')}" />
						<fmt:parseDate value="${cleanCreated}"
							pattern="yyyy-MM-dd HH:mm:ss" var="pCreated" />
						<fmt:formatDate value="${pCreated}" pattern="dd MMM yyyy, hh:mm a" />
					</div>
				</div>

				<div class="d-flex align-items-center justify-content-between mb-2">
					<h5 class="fw-bold text-dark m-0">
						<i class="fa-solid fa-code me-2 text-primary"></i>Cheat Sheet
						Blueprint
					</h5>
				</div>
				<div class="content-area mb-4">
					<c:out value="${sheet.content}" />
				</div>

				<div class="border-top pt-4">
					<h6
						class="fw-bold text-secondary mb-3 small uppercase tracking-wider">Tags
						& Keywords</h6>
					<div class="d-flex flex-wrap gap-2">
						<c:forEach var="tag" items="${sheet.tags}">
							<span class="tag-badge"><i
								class="fa-solid fa-hashtag fa-xs text-muted me-1"></i>${tag.name}</span>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>

		<div class="card main-card p-4 p-md-5 mb-4">
			<h4 class="fw-bold text-dark mb-4">
				<i class="fa-regular fa-star-half-stroke me-2 text-warning"></i>Ratings
				& Reviews
			</h4>

			<c:choose>
				<c:when test="${not empty sessionScope.loginUser}">
					<form
						action="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}/review"
						method="POST" class="border p-4 rounded-4 bg-light-subtle mb-5">
						<div class="row">
							<div class="col-md-4 mb-3 mb-md-0">
								<label class="form-label small fw-bold text-secondary">သင်၏
									အဆင့်သတ်မှတ်ချက်</label>
								<div class="rating-stars-input">
									<input type="radio" id="star5" name="rating" value="5"
										${currentReview.rating == 5 ? 'checked' : ''} required /><label
										for="star5"><i class="fa-solid fa-star"></i></label> <input
										type="radio" id="star4" name="rating" value="4"
										${currentReview.rating == 4 ? 'checked' : ''} /><label
										for="star4"><i class="fa-solid fa-star"></i></label> <input
										type="radio" id="star3" name="rating" value="3"
										${currentReview.rating == 3 ? 'checked' : ''} /><label
										for="star3"><i class="fa-solid fa-star"></i></label> <input
										type="radio" id="star2" name="rating" value="2"
										${currentReview.rating == 2 ? 'checked' : ''} /><label
										for="star2"><i class="fa-solid fa-star"></i></label> <input
										type="radio" id="star1" name="rating" value="1"
										${currentReview.rating == 1 ? 'checked' : ''} /><label
										for="star1"><i class="fa-solid fa-star"></i></label>
								</div>
							</div>
							<div class="col-md-8">
								<label class="form-label small fw-bold text-secondary">အကြံပြုချက်
									ရေးသားရန်</label>
								<textarea class="form-control rounded-3 border-secondary-subtle"
									name="reviewText" rows="3"
									placeholder="ဤလမ်းညွှန်ချက်အပေါ် သင်၏အမြင်ကို ရေးသားပေးပါဦး ဆရာ..."
									required><c:out
										value="${currentReview != null ? currentReview.reviewText : ''}" /></textarea>
								<div class="d-flex justify-content-end mt-3">
									<button type="submit"
										class="btn btn-warning text-white btn-sm rounded-pill px-4 fw-semibold shadow-sm">
										<c:choose>
											<c:when test="${not empty currentReview}">
												<i class="fa-solid fa-pen-to-square me-1"></i> Update My Review
											</c:when>
											<c:otherwise>
												<i class="fa-solid fa-paper-plane me-1"></i> Submit Review
											</c:otherwise>
										</c:choose>
									</button>
								</div>
							</div>
						</div>
					</form>
				</c:when>
				<c:otherwise>
					<div
						class="alert alert-info border-0 rounded-4 p-3 mb-4 small d-flex align-items-center gap-2">
						<i class="fa-solid fa-circle-info fs-5"></i> <span>တုံ့ပြန်ချက်ပေးရန်အတွက်
							ကျေးဇူးပြု၍ <a href="${pageContext.request.contextPath}/login"
							class="alert-link text-decoration-underline">Login</a>
							အရင်ဝင်ပေးပါ ဆရာ။
						</span>
					</div>
				</c:otherwise>
			</c:choose>

			<div class="review-list">
				<h6 class="fw-bold text-secondary small border-bottom pb-3 mb-4">FEEDBACKS
					(${reviews.size()})</h6>

				<c:forEach var="rev" items="${reviews}">
					<div class="d-flex gap-3 mb-2 p-3 review-item">
						<div
							class="review-avatar bg-info-subtle text-info d-flex align-items-center justify-content-center flex-shrink-0 shadow-sm border border-info-subtle">
							${fn:substring(rev.user.username, 0, 1).toUpperCase()}</div>
						<div class="flex-grow-1">
							<div
								class="d-flex justify-content-between align-items-center mb-1">
								<h6
									class="fw-bold text-dark small m-0 d-flex align-items-center gap-2">
									${rev.user.username}
									<c:if
										test="${sessionScope.loginUser.userId == rev.user.userId}">
										<span class="badge bg-primary text-white rounded-pill px-2"
											style="font-size: 0.6rem; font-weight: 500;">You</span>
									</c:if>
								</h6>
								<div class="text-warning small">
									<c:forEach begin="1" end="${rev.rating}">
										<i class="fa-solid fa-star" style="color: #eab308;"></i>
									</c:forEach>
									<c:forEach begin="${rev.rating + 1}" end="5">
										<i class="fa-regular fa-star text-muted" style="opacity: 0.4;"></i>
									</c:forEach>
								</div>
							</div>
							<p class="text-secondary small m-0"
								style="white-space: pre-wrap; line-height: 1.6;">
								<c:out value="${rev.reviewText}" />
							</p>
						</div>
					</div>
				</c:forEach>

				<c:if test="${empty reviews}">
					<div class="text-center py-4 text-muted fst-italic small">
						<i class="fa-regular fa-folder-open d-block fs-3 mb-2 opacity-50"></i>
						ဤနေရာတွင် တုံ့ပြန်ချက်များ မရှိသေးပါဗျာ။
					</div>
				</c:if>
			</div>
		</div>

		<div class="card main-card p-4 p-md-5 mb-4">
			<h4 class="fw-bold text-dark mb-4">
				<i class="fa-regular fa-comments me-2 text-primary"></i>Q&A
				Discussion Space
			</h4>

			<c:choose>
				<c:when test="${not empty sessionScope.loginUser}">
					<form
						action="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}/comment"
						method="POST" class="mb-5">
						<div
							class="input-group shadow-sm rounded-pill overflow-hidden border">
							<input type="text" name="content"
								class="form-control border-0 px-4 py-2.5 bg-white"
								placeholder="မေးခွန်း သို့မဟုတ် သိလိုသည်များအား ရေးသားဆွေးနွေးနိုင်ပါသည် ဆရာ..."
								required />
							<button type="submit" class="btn btn-primary px-4 fw-semibold">
								<i class="fa-regular fa-paper-plane me-2"></i>Post Comment
							</button>
						</div>
					</form>
				</c:when>
				<c:otherwise>
					<div
						class="alert alert-light border rounded-4 p-3 mb-4 small text-muted">
						<i class="fa-solid fa-lock me-2"></i>ဆွေးနွေးမှုများတွင်
						ပါဝင်ရေးသားရန်အတွက် ကျေးဇူးပြု၍ <a
							href="${pageContext.request.contextPath}/login"
							class="fw-bold text-primary text-decoration-underline">Login</a>
						အရင်ဝင်ပေးပါဗျာ။
					</div>
				</c:otherwise>
			</c:choose>

			<div class="comments-stream">
				<h6 class="fw-bold text-secondary small border-bottom pb-3 mb-4">ALL
					DISCUSSIONS (${comments.size()})</h6>

				<c:forEach var="com" items="${comments}">
					<div class="comment-node p-4 mb-4">
						<div
							class="d-flex justify-content-between align-items-center mb-2">
							<span
								class="fw-bold text-dark small d-flex align-items-center gap-1.5">
								<div
									class="bg-secondary-subtle text-secondary rounded-circle d-flex align-items-center justify-content-center fw-bold"
									style="width: 28px; height: 28px; font-size: 0.75rem;">
									${fn:substring(com.user.username, 0, 1).toUpperCase()}</div>
								${com.user.username}
							</span> <span class="text-muted" style="font-size: 0.75rem;"> <c:set
									var="cTime" value="${fn:replace(com.createdAt, 'T', ' ')}" />
								<fmt:parseDate value="${cTime}" pattern="yyyy-MM-dd HH:mm:ss"
									var="pcTime" /> <fmt:formatDate value="${pcTime}"
									pattern="dd MMM, hh:mm a" />
							</span>
						</div>
						<p class="text-secondary small mb-3 fs-6 px-1"
							style="line-height: 1.5; color: #475569 !important;">
							<c:out value="${com.content}" />
						</p>

						<c:if test="${not empty sessionScope.loginUser}">
							<button
								class="btn btn-light btn-sm rounded-pill px-3 border-0 bg-secondary-subtle text-dark fw-semibold small"
								style="font-size: 0.75rem;"
								onclick="toggleReplyBox(${com.commentId})">
								<i class="fa-solid fa-reply me-1"></i>Reply ပြန်ရန်
							</button>
						</c:if>

						<div id="replyBox-${com.commentId}"
							class="mt-3 p-3 bg-white rounded-3 border d-none">
							<form
								action="${pageContext.request.contextPath}/cheatsheets/view/${sheet.id}/comment"
								method="POST">
								<input type="hidden" name="parentId" value="${com.commentId}" />
								<div class="input-group input-group-sm">
									<input type="text" name="content" class="form-control px-3"
										placeholder="${com.user.username} ထံသို့ အကြောင်းပြန်ချက် ရေးရန်..."
										required />
									<button type="submit" class="btn btn-dark px-3 fw-semibold">Send
										Reply</button>
								</div>
							</form>
						</div>

						<div class="nested-reply-line mt-3">
							<c:forEach var="rep" items="${com.replies}">
								<div
									class="bg-white p-3 rounded-3 mb-2 border border-light-subtle shadow-xs">
									<div
										class="d-flex justify-content-between align-items-center mb-1.5"
										style="font-size: 0.8rem;">
										<span
											class="fw-bold text-dark d-flex align-items-center gap-1">
											<i class="fa-regular fa-comment-dots text-primary"></i>
											${rep.user.username}
										</span> <span class="text-muted" style="font-size: 0.7rem;"> <c:set
												var="rTime" value="${fn:replace(rep.createdAt, 'T', ' ')}" />
											<fmt:parseDate value="${rTime}" pattern="yyyy-MM-dd HH:mm:ss"
												var="prTime" /> <fmt:formatDate value="${prTime}"
												pattern="dd MMM, hh:mm a" />
										</span>
									</div>
									<p class="text-secondary small m-0" style="line-height: 1.5;">
										<c:out value="${rep.content}" />
									</p>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>

				<c:if test="${empty comments}">
					<div class="text-center py-4 text-muted fst-italic small">
						<i
							class="fa-solid fa-bubble-exclamation d-block fs-3 mb-2 opacity-50"></i>
						ဆွေးနွေးချက်များ မရှိသေးပါဗျာ။ စတင်မေးမြန်းနိုင်ပါပြီ။
					</div>
				</c:if>
			</div>
		</div>
	</div>

	<script>
		function toggleReplyBox(commentId) {
			var box = document.getElementById('replyBox-' + commentId);
			if(box.classList.contains('d-none')) {
				box.classList.remove('d-none');
			} else {
				box.classList.add('d-none');
			}
		}
	</script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>