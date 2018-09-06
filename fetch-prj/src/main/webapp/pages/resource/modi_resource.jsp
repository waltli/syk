<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/modi_resource.css" />
<script type="text/javascript" src="${ctxassets }/upload/upload.js"></script>
</head>
<body>
	<%@ include file="/pages/common/loading.jsp" %>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<form action="${ctx }/resource/modi-work" id="modiResourceForm" method="post">
					<div class="panel panel-resource">
			   			<div class="panel-heading">
			   				<span class="resourceCount"></span>
						</div>
			   			<div class="panel-body">
			   				<input type="hidden" name="resourceId" value="${resource.resourceId }">
			   				<input type="hidden" name="movieId" value="${resource.movieId }">
			   				<input type="hidden" name="pureName" value="${resource.pureName }">
			   				<input type="hidden" name="definition" value="${resource.definition }">
			   				<input type="hidden" name="season" value="${resource.season }">
			   				<table class="table">
					   			<tbody>
					   				<tr>
					   					<td class="title">下载地址<span class="required-start">*</span></td>
					   					<td colspan="3">
					   						<input type="text" name="busDownloadLink" value="${resource.busDownloadLink }" id="downloadLinkVal" class="form-control downloadLink" placeholder="Torrent文件通过右侧上传获取。">
					   						<a id="downloadLinkPlace"></a>
					   					</td>
					   				</tr>
					   				<tr>
					   					<td class="title">大小<span class="required-start">*</span></td>
					   					<td><input type="text" name="size" value="${resource.size }" id="sizeVal" class="form-control size" placeholder="资源大小。例：1.3G"></td>
					   					<td class="title">格式<span class="required-start">*</span></td>
					   					<td><input type="text" name="format" value="${resource.format }" id="formatVal" class="form-control format" placeholder="资源格式。例：avi"></td>
					   				</tr>
					   				<tr>
					   					<td class="title">制式<span class="required-start">*</span></td>
					   					<td><input type="text" name="quality" value="${resource.quality }" id="qualityVal" class="form-control" placeholder="资源制式。例：Web-DL"></td>
					   					<td class="title">分辨率<span class="required-start">*</span></td>
					   					<td><input type="text" name="resolution" value="${resource.resolution }" id="resolutionVal" class="form-control" placeholder="资源分辨率。例：720P"></td>
					   				</tr>
					   				<tr class="tv" style="${movie.category == 2 ? 'display:table-row;' : '' }">
					   					<td class="title">集数（开始）</td>
					   					<td><input type="text" name="episodeStart" value="${resource.episodeStart }" id="episodeStartVal" class="form-control" placeholder="集数开始（选填）"></td>
					   					<td class="title">集数（结束）<span class="required-start">*</span></td>
					   					<td><input type="text" name="episodeEnd" value="${resource.episodeEnd }" id="episodeEndVal" class="form-control" placeholder="集数结束（必填）"></td>
					   				</tr>
					   				<tr>
					   					<td class="title">字幕<span class="required-start">*</span></td>
					   					<td><input type="text" name="subtitle" value="${resource.subtitle }" id="subtitleVal" class="form-control" placeholder="字幕。例：中英双字"></td>
					   					<td class="title">资源来源</td>
					   					<td><input type="text" name="comeFromUrl" value="${resource.comeFromUrl }" class="form-control" placeholder="资源获取的网站。"></td>
					   				</tr>
					   				<tr id="photosTr">
					   					<td class="title">截图</td>
					   					<td colspan="3">
					   						<input type="hidden" name="busPhotos" id="photoVal">
					   						<a id="photoPlace1"></a>
					   						<a id="photoPlace2"></a>
					   						<a id="photoPlace3"></a>
					   						<a id="photoPlace4"></a>
					   					</td>
					   				</tr>
					   				<tr>
					   					<td class="title" colspan="3">
					   						<label class="fancy-checkbox" id="optimal">
					   							<c:choose>
					   								<c:when test="${movie.optimalResourceId == resource.resourceId}">
					   									<input type="checkbox" name="isOptimal" value="1" checked="checked" disabled="disabled">
					   								</c:when>
					   								<c:otherwise>
					   									<input type="checkbox" name="isOptimal">
					   								</c:otherwise>
					   							</c:choose>
												<span>标记为最佳</span>
											</label>
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

$("#modiResourceForm").validate({
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
		"busDownloadLink":{
			required:true
		},
		"size":{
			required:true
		},
		"format":{
			required:true
		},
		"quality":{
			required:true
		},
		"resolution":{
			required:true
		},
		"episodeEnd":{
			required:true
		},
		"subtitle":{
			required:true
		}
	},
	messages:{
		"busDownloadLink":{
			required:"输入资源地址，或上传torrent文件。"
		},
		"size":{
			required:"请输入资源大小。例：1.3G"
		},
		"format":{
			required:"请输入资源格式。例：avi"
		},
		"quality":{
			required:"请输入资源制式。例：Web-DL"
		},
		"resolution":{
			required:"请输入资源分辨率。例：720p"
		},
		"episodeEnd":{
			required:"请输入当前集数，如果集数类似“1-42”或“全集”，请分别输入开始集数和结束集数。"
		},
		"subtitle":{
			required:"请输入资源是否含有字幕。例：中英双字"
		}
	}
});

fileUpload({
	placeId:"downloadLinkPlace",
	data:{"fileType":4},
	close:false,
	before: function(){
		uploadCount++;
	},
	success: function(data){
		if(!data.requestResult){
			return;
		}
		if(data.uri){
			$("#downloadLinkVal").val(data.uri);
		}else {
			$("#downloadLinkVal").val("");
		}
		if(data.size){
			$("#sizeVal").val(data.size);
		}else {
			$("#sizeVal").val("");
		}
		if(data.format){
			$("#formatVal").val(data.format);
		}else {
			$("#formatVal").val("");
		}
		if(data.quality){
			$("#qualityVal").val(data.quality);
		}else {
			$("#qualityVal").val("");
		}
		if(data.resolution){
			$("#resolutionVal").val(data.resolution);
		}else {
			$("#resolutionVal").val("");
		}
		if(data.episodeStart){
			$("#episodeStartVal").val(data.episodeStart);
		}else {
			$("#episodeStartVal").val("");
		}
		if(data.episodeEnd){
			$("#episodeEndVal").val(data.episodeEnd);
		}else {
			$("#episodeEndVal").val("");
		}
		if(data.subtitle){
			$("#subtitleVal").val(data.subtitle);
		}else {
			$("#subtitleVal").val("");
		}
		uploadCount--;
	},
	error: function(data, status, e){
		$("#downloadLinkVal").val("");
		$("#sizeVal").val("");
		$("#formatVal").val("");
		$("#qualityVal").val("");
		$("#resolutionVal").val("");
		$("#episodeStartVal").val("");
		$("#episodeEndVal").val("");
		$("#subtitleVal").val("");
		uploadCount--;
		toastr.error(e);
	}
});


var photoValObj = {};
picUploadAll("photoPlace1", "photoVal", 3, photoValObj);
picUploadAll("photoPlace2", "photoVal", 3, photoValObj);
picUploadAll("photoPlace3", "photoVal", 3, photoValObj);
picUploadAll("photoPlace4", "photoVal", 3, photoValObj);

/**
 * 初始化photo显示
 */
var uris = '${resource.busPhotos}';
if(uris){
	uris = uris.split(",");
}
var $replaces = $("#photosTr").find(".replace");
$replaces.each(function(i,o){
	if(uris[i]){
		var placeId = $(this).attr("id");
		var $preview_img = $(this).find(".pic-preview img");
		$preview_img.attr("src", uris[i]);
		photoValObj[placeId] = uris[i];
	}
	var disStr = "";
	for(var i in photoValObj){
		disStr += (","+photoValObj[i]);
	}
	disStr = disStr.substring(1);
	$("#photoVal").val(disStr);
});

var movieCategory = '${movie.category}';
$("#modiResourceForm").submit(function(e){
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