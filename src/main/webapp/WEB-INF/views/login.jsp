<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!doctype html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<meta name="description" content="Web site created using create-react-app" />
	<title>React App</title>
	<script defer="defer" src="${pageContext.request.contextPath}../main.7e5a4429.js"></script>
	<link href="${pageContext.request.contextPath}../main.d5e42dc7.css" rel="stylesheet">
</head>
<body>
	<div id="root" data-csrf-token="${_csrf.token}" ></div>
</body>
</html>