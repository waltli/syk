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
				<form action="${ctx }/resource/add-work" id="addResourceForm" method="post">
					<div class="resourceArea">
						<div class="panel panel-resource">
							<div class="panel-heading">
								<span class="panel-title">为 [${movie.pureName }] 添加资源</span>
								<input type="hidden" name="movieId" value="${movie.movieId }">
								<a class="add" id="add-resource">
									<i class="fa fa-plus activity-icon"></i>
								</a>
							</div>
						</div>
						<div style=""><input type="submit" value="Submit" class="btn btn-primary"></div>
					</div>
				</form>
				
			</div>
		</div>
		<div class="clearfix"></div>
		<%@ include file="/pages/common/footer.jsp" %>
	</div>
</body>
<script type="text/javascript">

$("#add").addClass("active");

var uploadCount = 0;
var movieCategory = '${movie.category}';

$("#addResourceForm").validate({
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
	}
});


$("#addResourceForm").submit(function(e){
	if(uploadCount){
		toastr.info('待文件上传完成后方可提交！');
	  	e.preventDefault();
	}
});

/**
 * 初始化resouceInfo
 */
var resource_count = 0;
$("#add-resource").click(function(){
	var downloadLinkPlaceId = resource_count+'_downloadLinkPlace';
	var photoPlaceId1 = resource_count+'_photoPlace_1';
	var photoPlaceId2 = resource_count+'_photoPlace_2';
	var photoPlaceId3 = resource_count+'_photoPlace_3';
	var photoPlaceId4 = resource_count+'_photoPlace_4';
	
	var downloadLinkValId = resource_count+'_downloadLinkVal';
	var sizeValId = resource_count+'_sizeVal';
	var formatValId = resource_count+'_formatVal';
	var qualityValId = resource_count+'_qualityVal';
	var resolutionValId = resource_count+'_resolutionVal';
	var episodeStartValId = resource_count+'_episodeStartVal';
	var episodeEndValId = resource_count+'_episodeEndVal';
	var subtitleValId = resource_count+'_subtitleVal';
	var photoValId = resource_count+'_photoVal';
	
	var closeId = resource_count+'_close';
	
	var html = '<div class="panel panel-resource" style="display:none;">'+
			   '	<div class="panel-heading">'+
			   '		<span class="resourceCount">'+(resource_count+1)+'</span>';
    if(resource_count > 0){
	    html +='		<span style="position:absolute; right:5px; cursor:pointer;" id="'+closeId+'"><i class="fa fa-times" style="font-size: 1.5em;" aria-hidden="true"></i></span>';
    }
	html +=    '	</div>'+
			   '	<div class="panel-body">'+
			   '		<table class="table">'+
			   '			<tbody>'+
			   '				<tr>'+
			   '					<td class="title">下载地址<span class="required-start">*</span></td>'+
			   '					<td colspan="3">'+
			   '						<input type="text" name="resources['+resource_count+'].busDownloadLink" id="'+downloadLinkValId+'" class="form-control downloadLink" placeholder="Torrent文件通过右侧上传获取。">'+
			   '						<a id="'+downloadLinkPlaceId+'"></a>'+
			   '					</td>'+
			   '				</tr>'+
			   '				<tr>'+
			   '					<td class="title">大小<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].size" id="'+sizeValId+'" class="form-control pureName" placeholder="资源大小。例：1.3G"></td>'+
			   '					<td class="title">格式<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].format" id="'+formatValId+'" class="form-control anotherName" placeholder="资源格式。例：avi"></td>'+
			   '				</tr>'+
			   '				<tr>'+
			   '					<td class="title">制式<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].quality" id="'+qualityValId+'" class="form-control" placeholder="资源制式。例：Web-DL"></td>'+
			   '					<td class="title">分辨率<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].resolution" id="'+resolutionValId+'" class="form-control" placeholder="资源分辨率。例：720P"></td>'+
			   '				</tr>'+
			   '				<tr class="tv" '+(movieCategory == 2 ? "style=\"display:table-row;\"" : "")+'>'+
			   '					<td class="title">集数（开始）</td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].episodeStart" id="'+episodeStartValId+'" class="form-control" placeholder="集数开始（选填）"></td>'+
			   '					<td class="title">集数（结束）<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].episodeEnd" id="'+episodeEndValId+'" class="form-control" placeholder="集数结束（必填）"></td>'+
			   '				</tr>'+
			   '				<tr>'+
			   '					<td class="title">字幕<span class="required-start">*</span></td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].subtitle" id="'+subtitleValId+'" class="form-control" placeholder="字幕。例：中英双字"></td>'+
			   '					<td class="title">资源来源</td>'+
			   '					<td><input type="text" name="resources['+resource_count+'].comeFromUrl" class="form-control" placeholder="资源获取的网站。"></td>'+
			   '				</tr>'+
			   '				<tr>'+
			   '					<td class="title">截图</td>'+
			   '					<td colspan="3">'+
			   '						<input type="hidden" name="resources['+resource_count+'].busPhotos" id="'+photoValId+'">'+
			   '						<a id="'+photoPlaceId1+'"></a>'+
			   '						<a id="'+photoPlaceId2+'"></a>'+
			   '						<a id="'+photoPlaceId3+'"></a>'+
			   '						<a id="'+photoPlaceId4+'"></a>'+
			   '					</td>'+
			   '				</tr>'+
			   '			</tbody>'+
			   '		</table>'+
			   '	</div>'+
			   '</div>';
	var $html = $(html);
	$(this).parents(".panel-resource").after($html);
	$html.slideDown();
	
	$("#"+downloadLinkValId).rules("add",{
		required:true, 
		messages:{required:"输入资源地址，或上传torrent文件。"}
	});
	$("#"+sizeValId).rules("add",{
		required:true, 
		messages:{required:"请输入资源大小。例：1.3G"}
	});
	$("#"+qualityValId).rules("add",{
		required:true, 
		messages:{required:"请输入资源制式。例：Web-DL"}
	});
	$("#"+resolutionValId).rules("add",{
		required:true, 
		messages:{required:"请输入资源分辨率。例：720p"}
	});
	$("#"+episodeEndValId).rules("add",{
		required:true, 
		messages:{required:"请输入当前集数，如果集数类似“1-42”或“全集”，请分别输入开始集数和结束集数。"}
	});
	$("#"+subtitleValId).rules("add",{
		required:true, 
		messages:{required:"请输入资源是否含有字幕。例：中英双字"}
	});
    $("#"+formatValId).rules("add",{
    	required:true,
    	messages:{required:"请输入资源格式。例：avi"}
    });

	fileUpload({
		placeId:downloadLinkPlaceId,
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
				$("#"+downloadLinkValId).val(data.uri);
			}else {
				$("#"+downloadLinkValId).val("");
			}
			if(data.size){
				$("#"+sizeValId).val(data.size);
			}else {
				$("#"+sizeValId).val("");
			}
			if(data.format){
				$("#"+formatValId).val(data.format);
			}else {
				$("#"+formatValId).val("");
			}
			if(data.quality){
				$("#"+qualityValId).val(data.quality);
			}else {
				$("#"+qualityValId).val("");
			}
			if(data.resolution){
				$("#"+resolutionValId).val(data.resolution);
			}else {
				$("#"+resolutionValId).val("");
			}
			if(data.episodeStart){
				$("#"+episodeStartValId).val(data.episodeStart);
			}else {
				$("#"+episodeStartValId).val("");
			}
			if(data.episodeEnd){
				$("#"+episodeEndValId).val(data.episodeEnd);
			}else {
				$("#"+episodeEndValId).val("");
			}
			if(data.subtitle){
				$("#"+subtitleValId).val(data.subtitle);
			}else {
				$("#"+subtitleValId).val("");
			}
			uploadCount--;
		},
		error: function(data, status, e){
			$("#"+downloadLinkValId).val("");
			$("#"+sizeValId).val("");
			$("#"+formatValId).val("");
			$("#"+qualityValId).val("");
			$("#"+resolutionValId).val("");
			$("#"+episodeStartValId).val("");
			$("#"+episodeEndValId).val("");
			$("#"+subtitleValId).val("");
			uploadCount--;
			toastr.error(e);
		}
	});
	
	
	var photoValObj = {};
	picUploadAll(photoPlaceId1, photoValId, 3, photoValObj);
	picUploadAll(photoPlaceId2, photoValId, 3, photoValObj);
	picUploadAll(photoPlaceId3, photoValId, 3, photoValObj);
	picUploadAll(photoPlaceId4, photoValId, 3, photoValObj);
	
	$("#"+closeId).unbind("click").bind("click", function(){
		var $panelResource = $(this).parents(".panel-resource");
		var $countAll = $panelResource.prevAll().find(".resourceCount");
		$panelResource.fadeTo("fast", 0.01, function(){
			$(this).slideUp(400, function() {
				$(this).remove();
				resource_count--;
			});
		});
		$countAll.each(function(i,o){
			var text = $(this).text();
			$(this).text(text-1);
		});
	});
	
	resource_count++;
	
});
//触发上述事件创建第一个resourceInfo
$("#add-resource").trigger("click");

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