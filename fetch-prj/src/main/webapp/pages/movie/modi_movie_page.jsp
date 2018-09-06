<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/modi_movie_page.css" />
<script type="text/javascript" src="${ctxassets }/upload/upload.js"></script>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<form action="${ctx }/movie/modi-movie-work" id="modiMovieForm" method="post">
					<div class="panel panel-movie">
						<div class="panel-heading">
							<span class="panel-title">Movie Info</span>
						</div>
						<div class="panel-body movie-body">
							<%@ include file="/pages/common/loading.jsp" %>
							<input type="hidden" name="movieId" value="${movie.movieId }">
							<table class="table">
								<tbody>
									<tr>
										<td class="title">名称<span class="required-start">*</span></td>
										<td><input type="text" name="pureName" id="pureNameVal" value="${movie.pureName }" class="form-control pureName" placeholder="影片名称"></td>
										<td class="title">又名</td>
										<td><input type="text" name="anotherName" id="anotherNameVal" value="${movie.anotherName }" class="form-control anotherName" placeholder="其他地区取名"></td>
									</tr>
									<tr>
										<td class="title">导演</td>
										<td><input type="text" name="director" id="directorVal" value="${movie.director }" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
										<td class="title">编剧</td>
										<td><input type="text" name="writers" id="writersVal" value="${movie.writers }" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
									</tr>
									<tr>
										<td class="title">主演</td>
										<td><input type="text" name="cast" id="castVal" value="${movie.cast }" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
										<td class="title">语言</td>
										<td><input type="text" name="language" id="languageVal" value="${movie.language }" class="form-control" placeholder="电影默认使用的语言"></td>
										
									</tr>
									<%-- <tr>
										<td class="title">制片国家/地区<span class="required-start">*</span></td>
										<td><input type="text" name="locations" id="locationsVal" value="${movie.locations }" class="form-control" placeholder="多个用 ' / '分隔。例：中国大陆 / 美国"></td>
										<td class="title">类型<span class="required-start">*</span></td>
										<td><input type="text" name="labels" id="labelsVal" value="${movie.labels }" class="form-control" placeholder="多个用 ' / '分隔。例：科幻 / 动作"></td>
									</tr> --%>
									<tr>
										<td class="title">上映日期<span class="required-start">*</span></td>
										<td><input type="text" name="releaseTimeStr" value="${movie.releaseTimeStr }" id="releaseTimeStrVal" class="form-control" placeholder="上映日期"></td>
										<td class="title">年代</td>
										<td><input type="text" name="year" id="yearVal" maxlength="4" value="${movie.year }" class="form-control" placeholder="出品年代"></td>
									</tr>
									<tr>
										<td class="title">片长</td>
										<td><input type="text" name="duration" id="durationVal" value="${movie.duration }" class="form-control" placeholder="影片片长。例：126分钟"></td>
										<td class="title">影片类别<span class="required-start">*</span></td>
										<td>
											<select class="form-control input-sm" name="category" id="categoryVal">
												<option >请选择..</option>
												<option value="1" ${movie.category == 1 ? 'selected="selected"' : '' }>电影</option>
												<option value="2" ${movie.category == 2 ? 'selected="selected"' : '' }>电视剧</option>
											</select>
										</td>
									</tr>
									<tr class="tv" style="${movie.category == 2 ? 'display:table-tr;' : '' }">
										<td class="title">季数</td>
										<td><input type="text" name="presentSeason" id="presentSeasonVal" value="${movie.presentSeason }" class="form-control" placeholder="当前季数"></td>
										<td class="title">总集数</td>
										<td><input type="text" name="totalEpisode" id="totalEpisodeVal" value="${movie.totalEpisode }" class="form-control" placeholder="影片总集数"></td>
									</tr>
									<%-- <tr>
										<td class="title">豆瓣ID</td>
										<td><input type="text" name="doubanId" id="doubanIdVal" value="${movie.doubanId }" class="form-control" placeholder="豆瓣ID"></td>
										<td class="title">IMDB ID</td>
										<td><input type="text" name="imdbId" id="imdbIdVal" value="${movie.imdbId }" class="form-control" placeholder="IMDB ID"></td>
									</tr> --%>
									<tr>
										<td class="title">豆瓣评分</td>
										<td><input type="text" name="doubanScore" id="doubanScoreVal" value="${movie.doubanScore }" class="form-control" placeholder="豆瓣评分"></td>
										<td class="title">参评人数</td>
										<td><input type="text" name="attentionRate" id="attentionRateVal" value="${movie.attentionRate }" class="form-control" placeholder="参评人数"></td>
									</tr>
									<tr>
										<td class="title">简介</td>
										<td colspan="3"><textarea name="summary" id="summaryVal" class="form-control" placeholder="影片简介" rows="5">${movie.summary }</textarea></td>
									</tr>
									<tr>
										<td class="title">ICON</td>
										<td colspan="3">
											<input type="hidden" name="busIcon" id="iconVal" value="${movie.busIcon }">
											<a id="iconPlace" preview="${movie.busIcon }"></a>
											
										</td>
									</tr>
									<tr id="posterTr">
										<td class="title">海报</td>
										<td colspan="3">
											<input type="hidden" name="busPoster" id="posterVal" value="">
											<a id="posterPlace_1"></a>
											<a id="posterPlace_2"></a>
											<a id="posterPlace_3"></a>
											<a id="posterPlace_4"></a>
										</td>
									</tr>
								</tbody>
							</table>
							<div style="float:right;"><input type="submit" value="Submit" class="btn btn-primary"></div>
						</div>
					</div>
				</form>
				
			</div>
		</div>
		<div class="clearfix"></div>
		<%@ include file="/pages/common/footer.jsp" %>
	</div>
</body>
<script type="text/javascript">

$("#list").addClass("active");

var uploadCount = 0;

$("#modiMovieForm").validate({
	debug:false,	//调试模式，即使验证成功也不会跳转到目标页面
//	regex:true, //是否开启正则表达式匹配name，默认为false（自己添加的，修改代码在jquery.validate.js 1217行, 801行）
//	onfocusout: function(element) {   //失去焦点进行验证的正确开启方式，默认为false关闭，onkeyup等同理。
//		$(element).valid(); 
//	}, 
//	ignore:":hidden", //定义忽略的条件，默认是:hidden，包含display:none。可以指定某个class 如：.ignore
	errorPlacement: function($error, $element) { //错误信息位置设置方法
		$error.css("position","absolute");
		if($element.is(":hidden")){
			$error.appendTo($($element.data("error-view")).parent()); //这里的element是录入数据的对象
		}else {
			$error.appendTo($element.parent());
		}
	},
	rules:{ //rules里面的true或false仅是判断是否要执行验证
		"pureName":{
			required:true,
		},
		"releaseTimeStr":{
			required:true,
			dateFormat:true
		},
		"category":{
			categoryRequired:true
		},
		"totalEpisode":{
			required:function(element){
				if(movieCategory == 2){
					return true;
				}
				return false;
			}
		},
		"labels":{
			required:true
		},
		"locations":{
			required:true
		}
	},
	messages:{
		"pureName":{
			required:"请输入影片名称。",
		},
		"releaseTimeStr":{
			required:"请选择上映日期。",
			dateFormat:"时间格式必须为：yyyy-MM-dd。"
		},
		"category":{
			categoryRequired:"请选择电影类型。"
		},
		"totalEpisode":{
			required:"请输入该电视剧总集数。"
		},
		"labels":{
			required:"类型必填。例：科幻 / 喜剧 / 动作"
		},
		"locations":{
			required:"制品地区必填。例：中国大陆 / 美国"
		}
	}
});

//ICON上传组件
picUploadAll("iconPlace", "iconVal", 1);
//POSTER上传组件
var posterValObj = {};
picUploadAll("posterPlace_1", "posterVal", 2, posterValObj);
picUploadAll("posterPlace_2", "posterVal", 2, posterValObj);
picUploadAll("posterPlace_3", "posterVal", 2, posterValObj);
picUploadAll("posterPlace_4", "posterVal", 2, posterValObj);

/**
 * 初始化poster显示
 */
var uris = '${movie.busPoster}';
if(uris){
	uris = uris.split(",");
}
var $replaces = $("#posterTr").find(".replace");
$replaces.each(function(i,o){
	if(uris[i]){
		var placeId = $(this).attr("id");
		var $preview_img = $(this).find(".pic-preview img");
		$preview_img.attr("src", uris[i]);
		posterValObj[placeId] = uris[i];
	}
	var disStr = "";
	for(var i in posterValObj){
		disStr += (","+posterValObj[i]);
	}
	disStr = disStr.substring(1);
	$("#posterVal").val(disStr);
});

/**
 * 绑定select联动事件
 */
var movieCategory = '${movie.category}';
$("#categoryVal").bind("change", function(){
	movieCategory = $(this).val();
	if(movieCategory == 2){
		$(".tv").show();
	}else {
		$(".tv").hide();
	}
});
if(movieCategory > 0){
	$("#categoryVal").trigger("change");
}

$("#modiMovieForm").submit(function(e){
	if(uploadCount){
		toastr.info('待文件上传完成后方可提交！');
	  	e.preventDefault();
	}
	
	if(movieCategory != 2){
		$(".tv input[type=text]").val("");
	}
});

function picUploadAll(placeId, valId, fileType, disObj){
	picUpload({
		placeId:placeId,
		data:{"fileType":fileType},
		close:false,
		before: function(){
			uploadCount++;
		},
		success: function(data){
			if(!data.requestResult){
				return;
			}
			if(disObj){
				disObj[placeId] = data.uri;
				var disStr = "";
				for(var i in disObj){
					disStr += (","+disObj[i]);
				}
				disStr = disStr.substring(1);
				$("#"+valId).val(disStr);
			}else {
				$("#"+valId).val(data.uri);
			}
			uploadCount--;
		},
		error: function(data, status, e){
			if(disObj){
				delete disObj[placeId];
				var disStr = "";
				for(var i in disObj){
					disStr += (","+disObj[i]);
				}
				disStr = disStr.substring(1);
				$("#"+valId).val(disStr);
			}else {
				$("#"+valId).val("");
			}
			toastr.error(e);
			uploadCount--
		},
	});
}
</script>
</html>