<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/list.css" />
<script type="text/javascript" src="${ctxassets }/laypage/laypage.js"></script>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<%@ include file="/pages/common/loading.jsp" %>
				<div class="panel list">
					<div class="panel-heading">
						<c:if test="${!empty params.label || !empty params.q }">
							<div id="titleBar">
								<h1 class="label_notice">
									<c:if test="${!empty params.label }">
										电影标签：<span class="tags-name">${params.label }</span><sup class="tags-del" belong="label"></sup>
									</c:if>
								    <c:if test="${!empty params.q }">
										搜索：<span class="tags-name">${params.q }</span><sup class="tags-del" belong="q"></sup>
									</c:if>
								</h1>
							</div>
						</c:if>
					</div>
					<div class="panel-body">
						<table class="table table-striped table-movie">
							<thead>
								<tr>
									<th>海报</th>
									<th>名字</th>
									<th>导演</th>
									<th>演员</th>
									<th>更新时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pagingResult.list }" var="entity" varStatus="stat">
									<tr>
										<td class="icon">
											<a>
												<img alt="" src="${ctxassets }/img/movie_default_small.png" data-original="${entity.busIcon }">
											</a>
										</td>
										<td class="pureName">
											<div class="text-ellipsis pureName">
												<a>
													${entity.pureName }
												</a>
											</div>
										</td>
										<td class="director"><div class="text-ellipsis director">${entity.director }</div></td>
										<td class="cast"><div class="text-ellipsis cast">${entity.cast }</div></td>
										<td class="">${!empty entity.updateTimeStr ? entity.updateTimeStr : entity.createTimeStr }</td>
										<td>
											<a href="${ctx }/movie/modi-movie-page?mi=${entity.movieId }">修改</a> |
											<a href="${ctx }/resource/list?mi=${entity.movieId}">资源列表</a>| 
											<a class="toDelete" movieId="${entity.movieId }" style="cursor:pointer; ${entity.movieStatus == 2 ? 'display:none;' : '' }">删除</a>
											<a class="toRecovery" movieId="${entity.movieId }" style="color:red; cursor:pointer; ${entity.movieStatus == 2 ? '' : 'display:none;' }">恢复</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<%@ include file="/pages/common/paginator.jsp" %>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<%@ include file="/pages/common/footer.jsp" %>
	</div>
</body>
<script type="text/javascript">
$("#list").addClass("active");

$(".toDelete").click(function(){
	var movieId = $(this).attr("movieId");
	var $self = $(this);
	
	var M = jqueryAlert({
		'title'   : '系统提示',
		'content' : '确定删除该影片以及它的所有资源？',
		'width'  :  '300px',
		'height'  :  '130px',
		'modal'   : true,
		'buttons' :{
			'确定' : function(){
				M.close();
				$(".cover").show();
		    	$.ajax({
		    		url:ctx+"/movie/signDelete",
		    		data:{"movieId":movieId},
		    		type:"post",
		    		success:function(data){
		    			$self.hide();
		    			$self.parent().find(".toRecovery").show();
		    			$(".cover").hide();
		    			toastr.success('删除成功！');
		    		},
		    		error : function(data){
		    			$(".cover").hide();
		    			toastr.error('失败，请稍后再试！');
		    		}
		    	});
			},
			'取消' : function(){
				M.close();
			}
		}
	});
});

$(".toRecovery").click(function(){
	var movieId = $(this).attr("movieId");
	var $self = $(this);
	
	var M = jqueryAlert({
		'title'   : '系统提示',
		'content' : '确定恢复该影片以及它的所有资源？',
		'width'  :  '300px',
		'height'  :  '130px',
		'modal'   : true,
		'buttons' :{
			'确定' : function(){
				M.close();
				$(".cover").show();
		    	$.ajax({
		    		url:ctx+"/movie/signAvailable",
		    		data:{"movieId":movieId},
		    		type:"post",
		    		success:function(data){
		    			$self.hide();
		    			$self.parent().find(".toDelete").show();
		    			$(".cover").hide();
		    			toastr.success('恢复成功！');
		    		},
		    		error : function(data){
		    			$(".cover").hide();
		    			toastr.error('失败，请稍后再试！');
		    		}
		    	});
			},
			'取消' : function(){
				M.close();
			}
		}
	});
});
</script>
</html>