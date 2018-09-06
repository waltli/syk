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
						<a href="${ctx }/resource/add-page?mi=${movie.movieId }">添加资源</a>
					</div>
					<div class="panel-body">
						<table class="table table-striped table-movie">
							<thead>
								<tr>
									<th>名字</th>
									<th>制式</th>
									<th>分辨率</th>
									<th>清晰度得分</th>
									<th>字幕</th>
									<th>大小</th>
									<th>更新时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${resources }" var="entity" varStatus="stat">
									<tr>
										<c:set value="${entity.pureName }" var="pureName"></c:set>
									
										<c:if test="${!empty entity.format }">
											<c:set value="${entity.pureName }.${entity.format }" var="pureName"></c:set>
										</c:if>
										<c:if test="${movie.category == 2 }">
											<c:set var="episodeStr" value="${entity.episodeEnd}"></c:set>
											<c:if test="${!empty entity.episodeStart }">
												<c:set var="episodeStr" value="${entity.episodeStart }-${entity.episodeEnd }"></c:set>
											</c:if>
											<c:set value="${pureName }&nbsp;&nbsp;&nbsp;第${episodeStr }集" var="pureName"></c:set>
										</c:if>
										<td><a>${pureName }</a></td>
										<td class="quality">
											<div class="text-ellipsis">
													${entity.quality }
											</div>
										</td>
										<td class="resolution"><div class="text-ellipsis">${entity.resolution }</div></td>
										<td class="definition"><div class="text-ellipsis">${entity.definition }</div></td>
										<td class="subtitle"><div class="text-ellipsis">${entity.subtitle }</div></td>
										<td class="size"><div class="text-ellipsis">${entity.size }</div></td>
										<td class="">${!empty entity.updateTimeStr ? entity.updateTimeStr : entity.createTimeStr }</td>
										<td>
											<a class="optimaled" style="${movie.optimalResourceId == entity.resourceId ? '' : 'display:none;'} color:red;" resourceId="${entity.resourceId }" movieId="${entity.movieId }">最佳</a>
											<a class="setOptimal" style="${movie.optimalResourceId == entity.resourceId ? 'display:none;' : ''} cursor:pointer;" resourceId="${entity.resourceId }" movieId="${entity.movieId }">设置最佳</a> | 
											<a href="${ctx }/resource/modi-page?ri=${entity.resourceId }">修改</a> | 
											<a class="toDelete" resourceId="${entity.resourceId }" movieId="${entity.movieId }" style="cursor:pointer; ${entity.resourceStatus == 2 ? 'display:none;' : '' }">删除</a>
											<a class="toRecovery" resourceId="${entity.resourceId }" movieId="${entity.movieId }" style="color:red; cursor:pointer; ${entity.resourceStatus == 2 ? '' : 'display:none;' }">恢复</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<%@ include file="/pages/common/footer.jsp" %>
	</div>
</body>
<script type="text/javascript">
$("#list").addClass("active");

$(".setOptimal").click(function(){
	var resourceId = $(this).attr("resourceId");
	var movieId = $(this).attr("movieId");
	var $self = $(this);
	var M = jqueryAlert({
		'title'   : '系统提示',
		'content' : '确定将该资源设置为有影片最佳吗？',
		'width'  :  '300px',
		'height'  :  '130px',
		'modal'   : true,
		'buttons' :{
			'确定' : function(){
				M.close();
				$(".cover").show();
		    	$.ajax({
		    		url:ctx+"/movie/set-optimal",
		    		data:{"movieId":movieId,"optimalResourceId" : resourceId},
		    		type:"post",
		    		success:function(data){
		    			$(".setOptimal").show();
		    			$(".optimaled").hide();
		    			$self.hide();
		    			$self.parent().find(".optimaled").show();
		    			$(".cover").hide();
		    			toastr.success('设置成功！');
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


$(".toDelete").click(function(){
	var resourceId = $(this).attr("resourceId");
	var movieId = $(this).attr("movieId");
	var $self = $(this);
	var M = jqueryAlert({
		'title'   : '系统提示',
		'content' : '确定删除该资源？',
		'width'  :  '300px',
		'height'  :  '130px',
		'modal'   : true,
		'buttons' :{
			'确定' : function(){
				debugger;
				M.close();
				$(".cover").show();
		    	$.ajax({
		    		url:ctx+"/resource/signDelete",
		    		data:{"movieId":movieId, "resourceId":resourceId},
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
	var resourceId = $(this).attr("resourceId");
	var movieId = $(this).attr("movieId");
	var $self = $(this);
	
	var M = jqueryAlert({
		'title'   : '系统提示',
		'content' : '确定恢复资源？',
		'width'  :  '300px',
		'height'  :  '130px',
		'modal'   : true,
		'buttons' :{
			'确定' : function(){
				M.close();
				$(".cover").show();
		    	$.ajax({
		    		url:ctx+"/resource/signAvailable",
		    		data:{"movieId":movieId, "resourceId":resourceId},
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