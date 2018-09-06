<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${pageContext.request.serverPort eq 80 }">
	<c:set var="serverPort" value="" ></c:set>
</c:if>
<c:if test="${pageContext.request.serverPort != 80 }">
	<c:set var="serverPort" value=":${pageContext.request.serverPort}" ></c:set>
</c:if>
<c:set var="host" value="${pageContext.request.scheme }://${pageContext.request.serverName}${serverPort}"></c:set>
<c:set var="ctx" value="${pageContext.request.scheme }://${pageContext.request.serverName}${serverPort}${pageContext.request.contextPath }" ></c:set>
<c:set var="suffix_index" value="${fn:substring(ctx, (fn:length(ctx)-1), (fn:length(ctx))) }" ></c:set>
<c:if test="${suffix_index == '/'}">
	<c:set var="ctx" value="${fn:substring(ctx,0, fn:length(ctx)-1) }"></c:set>
</c:if>
<c:set var="ctxassets" value="${ctx}/assets"></c:set>

<c:if test="${!empty params }">
	<c:set var="availableParams" value="" ></c:set>
	<c:forEach items="${params }" var="c">
		<c:set var="availableParams" value="${availableParams }${c.key }=${c.value }&" ></c:set>
	</c:forEach>
	<c:set var="availableParams" value="${fn:substring(availableParams,0, fn:length(availableParams)-1) }"></c:set>
</c:if>

<script>
 var ctx='${ctx}';
 var ctxassets='${ctxassets}';
 var availableParams = '${availableParams}';
 var params = {};
 var paramsJson = '${paramsJson}';
 if(paramsJson){
	 params = JSON.parse(paramsJson);
 }
</script>