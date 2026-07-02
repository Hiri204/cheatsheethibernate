<%-- sidebar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
// Get active page from request parameter or attribute
String activePage = request.getParameter("activePage");
if (activePage == null) {
	activePage = (String) request.getAttribute("activePage");
}
// Default to home if still null
if (activePage == null) {
	activePage = "home";
}

// Determine if Quick Actions should be expanded
boolean quickActionsExpanded = "create".equals(activePage) || "mysheets".equals(activePage);

// Determine if Settings should be expanded
boolean settingsExpanded = "profile".equals(activePage);
%>

<style>
/* Sidebar Scrollable Styling */
.sidebar {
	width: 260px;
	height: 100vh;
	position: fixed;
	left: 0;
	top: 0;
	background: #ffffff;
	padding: 30px 20px;
	z-index: 1000;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	border-right: 1px solid #f0f0f0;
}

.sidebar-menu-wrapper {
	flex-grow: 1;
	overflow-y: auto;
	margin-bottom: 20px;
	padding-right: 5px;
}

.sidebar-menu-wrapper::-webkit-scrollbar {
	width: 4px;
}

.sidebar-menu-wrapper::-webkit-scrollbar-thumb {
	background: #e8e8e8;
	border-radius: 4px;
}

.sidebar .sidebar-brand {
	color: #1a1a1a;
	font-size: 1.4rem;
	font-weight: 700;
	text-decoration: none;
	display: flex;
	align-items: center;
	margin-bottom: 2rem;
	padding-left: 10px;
}

.sidebar .sidebar-brand i {
	color: #1a1a1a;
}

.sidebar .section-title {
	font-size: 0.75rem;
	font-weight: 600;
	color: #9c9c9c;
	margin-top: 1.5rem;
	margin-bottom: 0.5rem;
	padding-left: 12px;
	text-transform: uppercase;
	letter-spacing: 0.5px;
}

.sidebar .nav-link {
	color: #555555;
	font-weight: 500;
	font-size: 0.95rem;
	padding: 11px 14px;
	border-radius: 14px;
	margin-bottom: 3px;
	display: flex;
	align-items: center;
	justify-content: space-between;
	text-decoration: none;
	transition: all 0.2s ease;
	cursor: pointer;
}

.sidebar .nav-link i {
	font-size: 1.05rem;
	width: 26px;
	color: #555555;
}

.sidebar .nav-link:hover {
	background: #f7f7f7;
	color: #1a1a1a;
}

.sidebar .nav-link.active {
	color: #1a1a1a;
	background: #f3f3f3;
	font-weight: 600;
}

.sidebar .nav-link.active i {
	color: #1a1a1a;
}

.sidebar .nav-link .nav-text {
	display: flex;
	align-items: center;
}

.collapse-icon {
	transition: transform 0.2s;
	font-size: 0.75rem;
	color: #adadad;
}

[aria-expanded="true"] .collapse-icon {
	transform: rotate(90deg);
}

.btn-logout {
	background: transparent;
	color: #757575;
	border: 1px solid #ededed;
	border-radius: 14px;
	font-size: 0.95rem;
	transition: all 0.2s;
	padding: 12px;
	text-align: center;
	display: block;
	text-decoration: none;
	font-weight: 500;
}

.btn-logout:hover {
	background: #fff5f5;
	color: #ff4d4f;
	border-color: #ffcccc;
}

/* Submenu items */
.sidebar .sub-menu .nav-link {
	padding: 8px 14px 8px 20px;
	font-size: 0.9rem;
}

.sidebar .sub-menu .nav-link i {
	font-size: 0.9rem;
	width: 22px;
}

/* Active submenu item */
.sidebar .sub-menu .nav-link.active {
	background: #f0f0f0;
}

/* Keep collapsed state persistent */
.sidebar .collapse.show {
	visibility: visible;
}

/* Responsive */
@media ( max-width : 768px) {
	.sidebar {
		width: 100%;
		height: auto;
		position: relative;
		border-right: none;
		border-bottom: 1px solid #f0f0f0;
		padding: 16px;
	}
	.sidebar-menu-wrapper {
		max-height: 70vh;
	}
}
</style>

<div class="sidebar">
	<div class="sidebar-menu-wrapper">
		<a href="${pageContext.request.contextPath}/dashboard"
			class="sidebar-brand"> <i
			class="fa-solid fa-feather-pointed me-2"></i>Floe
		</a>

		<ul class="nav flex-column">
			<!-- Home -->
			<li class="nav-item"><a
				class="nav-link <%= "home".equals(activePage) ? "active" : "" %>"
				id="nav-home" href="${pageContext.request.contextPath}/dashboard">
					<span class="nav-text"> <i class="fa-solid fa-house me-2"></i>
						Home
				</span>
			</a></li>

			<!-- Actions Section -->
			<div class="section-title">Actions</div>
			<li class="nav-item"><a
				class="nav-link <%=quickActionsExpanded ? "active" : ""%>"
				data-bs-toggle="collapse" href="#quickActionsMenu"
				aria-expanded="<%=quickActionsExpanded ? "true" : "false"%>">
					<span class="nav-text"> <i class="fa-solid fa-bolt me-2"></i>
						Quick Actions
				</span> <i class="fa-solid fa-chevron-right collapse-icon"></i>
			</a>
				<ul
					class="collapse list-unstyled ps-3 mt-1 sub-menu <%=quickActionsExpanded ? "show" : ""%>"
					id="quickActionsMenu">
					<li><a
						class="nav-link <%= "create".equals(activePage) ? "active" : "" %>"
						id="nav-create"
						href="${pageContext.request.contextPath}/cheatsheets/new"> <span
							class="nav-text"> <i class="fa-solid fa-circle-plus me-2"></i>
								Create New Sheet
						</span>
					</a></li>
					<li><a
						class="nav-link <%= "mysheets".equals(activePage) ? "active" : "" %>"
						id="nav-mysheets"
						href="${pageContext.request.contextPath}/cheatsheets/my-cheatsheets">
							<span class="nav-text"> <i
								class="fa-solid fa-folder-open me-2"></i> My Cheat Sheets
						</span>
					</a></li>
				</ul></li>

			<!-- Admin Tools (conditionally shown) -->
			<c:if test="${sessionScope.loginUser.role eq 'admin'}">
				<div class="mt-2">
					<div class="section-title">Admin Tools</div>

					<!-- Management -->
					<li class="nav-item"><a
						class="nav-link <%="categories".equals(activePage) || "broadcast".equals(activePage) ? "active" : ""%>"
						data-bs-toggle="collapse" href="#adminTools"
						aria-expanded="<%="categories".equals(activePage) || "broadcast".equals(activePage) ? "true" : "false"%>">
							<span class="nav-text"> <i
								class="fa-solid fa-shield-halved me-2"></i> Management
						</span> <i class="fa-solid fa-chevron-right collapse-icon"></i>
					</a>
						<ul
							class="collapse list-unstyled ps-3 mt-1 sub-menu <%="categories".equals(activePage) || "broadcast".equals(activePage) ? "show" : ""%>"
							id="adminTools">
							<li><a
								class="nav-link <%= "categories".equals(activePage) ? "active" : "" %>"
								id="nav-categories"
								href="${pageContext.request.contextPath}/admin/metadata/panel">
									<span class="nav-text"> <i
										class="fa-solid fa-folder-tree me-2"></i> Categories
								</span>
							</a></li>
							<li><a
								class="nav-link <%= "broadcast".equals(activePage) ? "active" : "" %>"
								id="nav-broadcast"
								href="${pageContext.request.contextPath}/admin/broadcast/panel">
									<span class="nav-text"> <i
										class="fa-solid fa-bullhorn me-2"></i> Announcements
								</span>
							</a></li>
						</ul></li>

					<!-- User Tools -->
					<li class="nav-item"><a
						class="nav-link <%="users".equals(activePage) || "reports".equals(activePage) || "analytics".equals(activePage) ? "active" : ""%>"
						data-bs-toggle="collapse" href="#userTools"
						aria-expanded="<%="users".equals(activePage) || "reports".equals(activePage) || "analytics".equals(activePage) ? "true"
		: "false"%>">
							<span class="nav-text"> <i
								class="fa-solid fa-user-gear me-2"></i> User Tools
						</span> <i class="fa-solid fa-chevron-right collapse-icon"></i>
					</a>
						<ul
							class="collapse list-unstyled ps-3 mt-1 sub-menu <%="users".equals(activePage) || "reports".equals(activePage) || "analytics".equals(activePage) ? "show" : ""%>"
							id="userTools">
							<li><a
								class="nav-link <%= "users".equals(activePage) ? "active" : "" %>"
								id="nav-users"
								href="${pageContext.request.contextPath}/user/list"> <span
									class="nav-text"> <i class="fa-solid fa-users me-2"></i>
										User List
								</span>
							</a></li>
							<li><a
								class="nav-link <%= "reports".equals(activePage) ? "active" : "" %>"
								id="nav-reports"
								href="${pageContext.request.contextPath}/reports/admin/list">
									<span class="nav-text"> <i
										class="fa-solid fa-triangle-exclamation me-2"></i> Reports
								</span>
							</a></li>
							<li><a
								class="nav-link <%= "analytics".equals(activePage) ? "active" : "" %>"
								id="nav-analytics"
								href="${pageContext.request.contextPath}/reports/admin/summary">
									<span class="nav-text"> <i
										class="fa-solid fa-chart-simple me-2"></i> Analytics
								</span>
							</a></li>
						</ul></li>
				</div>
			</c:if>
		</ul>
	</div>

	<!-- Bottom Section -->
	<div class="pt-2 border-top">
		<ul class="nav flex-column mb-3">
			<li class="nav-item"><a
				class="nav-link <%=settingsExpanded ? "active" : ""%>"
				data-bs-toggle="collapse" href="#settingTools"
				aria-expanded="<%=settingsExpanded ? "true" : "false"%>"> <span
					class="nav-text"> <i class="fa-solid fa-gear me-2"></i>
						Settings
				</span> <i class="fa-solid fa-chevron-right collapse-icon"></i>
			</a>
				<ul
					class="collapse list-unstyled ps-3 mt-1 sub-menu <%=settingsExpanded ? "show" : ""%>"
					id="settingTools">
					<li><a
						class="nav-link <%= "profile".equals(activePage) ? "active" : "" %>"
						id="nav-profile"
						href="${pageContext.request.contextPath}/profile/view"> <span
							class="nav-text"> <i class="fa-solid fa-user me-2"></i> My
								Profile
						</span>
					</a></li>
				</ul></li>
		</ul>
		<a href="${pageContext.request.contextPath}/logout" class="btn-logout">
			<i class="fa-solid fa-right-from-bracket me-1"></i> Logout
		</a>
	</div>
</div>