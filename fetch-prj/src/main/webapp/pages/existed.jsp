<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/add_resources.css" />
<script type="text/javascript" src="${ctxassets }/upload/upload.js"></script>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<div class="resourceArea">
					<div class="panel panel-resource">
						<div class="panel-heading">
							<div style="margin-bottom:20px;">影片 [${movie.pureName }] 已经存在，你可以..</div>
							
							<span style="margin-right:10px;"><a href="${ctx }/movie/modi-movie-page?mi=${movie.movieId}">修改影片信息</a></span>
							<span style="margin-right:10px;"><a href="${ctx }/resource/list?mi=${movie.movieId}">查看资源列表</a></span>
							<span style="margin-right:10px;"><a href="${ctx }/resource/add-page?mi=${movie.movieId}">添加资源</a></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<%@ include file="/pages/common/footer.jsp" %>
	</div>
</body>
<script type="text/javascript">

$("#add").addClass("active");

</script>
</html>