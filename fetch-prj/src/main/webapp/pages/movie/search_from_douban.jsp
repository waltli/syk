<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/search_from_douban.css" />
</head>
<body>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<%@ include file="/pages/common/loading.jsp" %>
				<div class="panel list">
					<div class="panel-heading">
						<span class="panel-title">Search movie from douban.com</span>
					</div>
					<div class="panel-body">
						<div class="input-group">
							<input class="form-control" id="query" type="text" placeholder="Search to douban.com">
							<span class="input-group-btn"><button class="btn btn-primary" type="button" id="searchDouban">Go!</button></span>
						</div>
						<div class="panel" id="fetch-result">
							
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
	$("#query").focus();

	$("#searchDouban").click(function(){
		var query = $("#query").val();
		if(!query){
			return;
		}
		$("#fetch-result").empty();
		$(".cover").show();
		$.ajax({
			url:ctx+"/movie/fetch-list",
			data:{"query":query},
			success : function(data){
				if(!data.requestResult){
					$("#fetch-result").append(buildError(data.error));
					$(".cover").hide();
					return;
				}
				var html = buildFetchList(query, data.movies);
				$("#fetch-result").append(html);
				$(".cover").hide();
			},
			error : function(data){
				$("#fetch-result").append(buildError(data.responseText));
				$(".cover").hide();
			}
		});
	});
	
	function buildFetchList(query, list){
		html = '<div class="panel-heading">'+
			   '	<span class="panel-title">The result of search \"'+query+'\" from douban.com</span>'+
			   '</div>'+
			   '<div class="panel-body">';
		if(list.length > 0){
			for(var idx in list){
				var movie = list[idx];
				html += '<div class="item">'+
						'	<a href="'+ctx+'/movie/add-page?doubanId='+movie.doubanId+'">'+
						'		<img src="'+movie.icon+'">'+
						'	</a>'+
						'	<div class="detail">'+
						'		<div class="title">'+
						'			<a href="'+ctx+'/movie/add-page?doubanId='+movie.doubanId+'">'+movie.pureName;
				if(movie.anotherName){
					html += ' / '+movie.anotherName;
				}
				if(movie.year){
					html += ' ('+movie.year+')';
				}
				html += '</a>'+
						'		</div>'+
						'		<div class="meta abstract">'+movie.labels+'</div>'+
						'		<div class="meta abstract_2">'+movie.cast+'</div>'+
						'	</div>'+
						'</div>';
			}
		}else {
			html += '<p>没有在豆瓣中找到关于 “'+query+'” 的电影，换个搜索词试试吧。</p>'+
					'</div>'
		}
		return html;
	}
	
	function buildError(error){
		html = '<div class="panel-heading">'+
		   '	<span class="panel-title">搜索出现异常</span>'+
		   '</div>'+
		   '<div class="panel-body">'+
		   '<p>'+error+'</p>'+
		   '</div>';
		return html;
	}
</script>
</html>