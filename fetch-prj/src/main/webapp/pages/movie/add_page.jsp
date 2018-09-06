<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蝉影后台管理系统</title>
<%@ include file="/pages/common/header.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctxassets }/css/add_page.css" />
<script type="text/javascript" src="${ctxassets }/upload/upload.js"></script>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/pages/common/nav.jsp" %>
		<div class="main">
			<div class="main-content">
				<form action="${ctx }/movie/add-work" id="addForm" method="post">
					<div id="movieArea">
						<div class="panel panel-movie">
							<div class="panel-heading">
								<span class="panel-title">Movie Info</span>
								<label id="existedLabel" style="margin-left:10px; margin-bottom: 5px; font-weight: 700; display:none;">库中存在同名的影片，建议搜索比对后，再做添加！</label>
							</div>
							<div class="panel-body movie-body">
								<%@ include file="/pages/common/loading.jsp" %>
								<table class="table">
									<tbody>
										<tr>
											<td class="title">名称<span class="required-start">*</span></td>
											<td>
												<input type="text" name="pureName" id="pureNameVal" class="form-control pureName" placeholder="影片名称">
											</td>
											<td class="title">又名</td>
											<td><input type="text" name="anotherName" id="anotherNameVal" class="form-control anotherName" placeholder="其他地区取名"></td>
										</tr>
										<tr>
											<td class="title">导演</td>
											<td><input type="text" name="director" id="directorVal" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
											<td class="title">编剧</td>
											<td><input type="text" name="writers" id="writersVal" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
										</tr>
										<tr>
											<td class="title">主演</td>
											<td><input type="text" name="cast" id="castVal" class="form-control" placeholder="多个用 ' / '分隔。例：张三 / 李四"></td>
											<td class="title">类型<span class="required-start">*</span></td>
											<td><input type="text" name="labels" id="labelsVal" class="form-control" placeholder="多个用 ' / '分隔。例：科幻 / 动作"></td>
										</tr>
										<tr>
											<td class="title">制片国家/地区<span class="required-start">*</span></td>
											<td><input type="text" name="locations" id="locationsVal" class="form-control" placeholder="多个用 ' / '分隔。例：中国大陆 / 美国"></td>
											<td class="title">语言</td>
											<td><input type="text" name="language" id="languageVal" class="form-control" placeholder="电影默认使用的语言"></td>
										</tr>
										<tr>
											<td class="title">上映日期<span class="required-start">*</span></td>
											<td><input type="text" name="releaseTimeStr" id="releaseTimeStrVal" class="form-control" placeholder="上映日期"></td>
											<td class="title">年代</td>
											<td><input type="text" name="year" id="yearVal" maxlength="4" class="form-control" placeholder="出品年代"></td>
										</tr>
										<tr>
											<td class="title">片长</td>
											<td><input type="text" name="duration" id="durationVal" class="form-control" placeholder="影片片长。例：126分钟"></td>
											<td class="title">影片类别<span class="required-start">*</span></td>
											<td>
												<select class="form-control input-sm" name="category" id="categoryVal">
													<option >请选择..</option>
													<option value="1">电影</option>
													<option value="2">电视剧</option>
												</select>
											</td>
										</tr>
										<tr class="tv">
											<td class="title">季数</td>
											<td><input type="text" name="presentSeason" id="presentSeasonVal" class="form-control" placeholder="当前季数"></td>
											<td class="title">总集数</td>
											<td><input type="text" name="totalEpisode" id="totalEpisodeVal" class="form-control" placeholder="影片总集数"></td>
										</tr>
										<tr>
											<td class="title">豆瓣ID</td>
											<td><input type="text" name="doubanId" id="doubanIdVal" class="form-control" placeholder="豆瓣ID"></td>
											<td class="title">IMDB ID</td>
											<td><input type="text" name="imdbId" id="imdbIdVal" class="form-control" placeholder="IMDB ID"></td>
										</tr>
										<tr>
											<td class="title">豆瓣评分</td>
											<td><input type="text" name="doubanScore" id="doubanScoreVal" class="form-control" placeholder="豆瓣评分"></td>
											<td class="title">参评人数</td>
											<td><input type="text" name="attentionRate" id="attentionRateVal" class="form-control" placeholder="参评人数"></td>
										</tr>
										<tr>
											<td class="title">简介</td>
											<td colspan="3"><textarea name="summary" id="summaryVal" class="form-control" placeholder="影片简介" rows="4"></textarea></td>
										</tr>
										<tr>
											<td class="title">ICON</td>
											<td colspan="3">
												<input type="hidden" name="busIcon" id="iconVal">
												<a id="iconPlace"></a>
												
											</td>
										</tr>
										<tr>
											<td class="title">海报</td>
											<td colspan="3">
												<input type="hidden" name="busPoster" id="posterVal">
												<a id="posterPlace_1"></a>
												<a id="posterPlace_2"></a>
												<a id="posterPlace_3"></a>
												<a id="posterPlace_4"></a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div style="margin-bottom: 30px;"><input type="submit" value="Submit" class="btn btn-primary"></div>
					</div>
					
					<div id="resourceArea">
						<div class="panel panel-resource">
							<div class="panel-heading">
								<span class="panel-title">Resource Info</span>
								<a class="add" id="add-resource">
									<i class="fa fa-plus activity-icon"></i>
								</a>
							</div>
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

$("#add").addClass("active");

var uploadCount = 0;

var oldPureName = "";
$("#pureNameVal").blur(function(){
	var pureName = $(this).val();
	if(!pureName){
		return;
	}
	if(pureName == oldPureName){
		return;
	}
	oldPureName = pureName;
	$.ajax({
		url:ctx+"/movie/exists",
		data:{"pureName":pureName},
		success:function(data){
			if(data.requestResult){
				if(data.exists){
					$("#existedLabel").show();
					return;
				}
			}
			$("#existedLabel").hide();
		},
		error : function(data){
			toastr.error("pureName异步错误！");
			$("#existedLabel").hide();
		}
	});
	
});

$("#addForm").validate({
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
 * 绑定select联动事件
 */
var movieCategory = 0;
$("#categoryVal").bind("change", function(){
	movieCategory = $(this).val();
	if(movieCategory == 2){
		$(".tv").show();
	}else {
		$(".tv").hide();
	}
});

$("#addForm").submit(function(e){
	if(uploadCount){
		toastr.info('待文件上传完成后方可提交！');
	  	e.preventDefault();
	}
	
	if(movieCategory != 2){
		$(".tv input[type=text]").val("");
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


var doubanId = '${doubanId}';
if(doubanId){
	$(".cover").show();
	$.ajax({
		url:ctx+"/movie/fetch-detail",
		data:{"doubanId" : doubanId},
		success:function(data){
			if(!data.requestResult){
				$(".cover").hide();
				return;
			}
			assignMovieInfo(data.movie);
			$(".cover").hide();
			
		},
		error : function(data){
			$(".cover").hide();
		}
	});
}

function assignMovieInfo(movie){
	if(movie.pureName){
		$("#pureNameVal").val(movie.pureName);
	}
	if(movie.anotherName){
		$("#anotherNameVal").val(movie.anotherName);
	}
	if(movie.director){
		$("#directorVal").val(movie.director);
	}
	if(movie.writers){
		$("#writersVal").val(movie.writers);
	}
	if(movie.cast){
		$("#castVal").val(movie.cast);
	}
	if(movie.labels){
		$("#labelsVal").val(movie.labels);
	}
	if(movie.locations){
		$("#locationsVal").val(movie.locations);
	}
	if(movie.language){
		$("#languageVal").val(movie.language);
	}
	if(movie.releaseTimeStr){
		$("#releaseTimeStrVal").val(movie.releaseTimeStr);
	}
	if(movie.year){
		$("#yearVal").val(movie.year);
	}
	if(movie.duration){
		$("#durationVal").val(movie.duration);
	}
	if(movie.category){
		$("#categoryVal").val(movie.category);
		$("#categoryVal").trigger("change");
	}
	if(movie.presentSeason){
		$("#presentSeasonVal").val(movie.presentSeason);
	}
	if(movie.totalEpisode){
		$("#totalEpisodeVal").val(movie.totalEpisode);
	}
	if(movie.doubanId){
		$("#doubanIdVal").val(movie.doubanId);
	}
	if(movie.imdbId){
		$("#imdbIdVal").val(movie.imdbId);
	}
	if(movie.doubanScore){
		$("#doubanScoreVal").val(movie.doubanScore);
	}
	if(movie.attentionRate){
		$("#attentionRateVal").val(movie.attentionRate);
	}
	if(movie.summary){
		$("#summaryVal").val(movie.summary);
	}
	if(movie.iconUrl){
		var $td = $("#iconVal").parents("td");
		var $loading = $td.find(".loading");
		var $preview_img = $td.find(".pic-preview img");
		uploadCount++;
		$loading.css({"display": "-webkit-flex", "display": "flex"});
		$.ajax({
			url:ctx+"/movie/download-icon",
			data:{"url":movie.iconUrl},
			success:function(data){
				if(data.requestResult){
					$preview_img.attr("src", ctx+"/"+data.uri);
					$("#iconVal").val(data.uri);
				}
				uploadCount--;
				$loading.hide();
			},
			error:function(data){
				uploadCount--
				$loading.hide();
			}
		});
	}
	if(movie.posterPageUrl){
	    var $td = $("#posterVal").parents("td");
		var $loadings = $td.find(".loading");
		var $replaces = $td.find(".replace");
		$loadings.css({"display": "-webkit-flex", "display": "flex"});
		var iconName = "";
		uploadCount++;
		if(movie.iconUrl){
			var i = movie.iconUrl.lastIndexOf("/")+1;
			iconName = movie.iconUrl.substring(i);
		}
		$.ajax({
			url:ctx+"/movie/download-posters",
			data:{"pageUrl":movie.posterPageUrl, "iconName":iconName},
			success:function(data){
				if(data.requestResult){
					var uris = data.uris;
					$replaces.each(function(i,o){
						if(uris[i]){
							var placeId = $(this).attr("id");
							var $preview_img = $(this).find(".pic-preview img");
							$preview_img.attr("src", ctx+"/"+uris[i]);
							posterValObj[placeId] = uris[i];
						}
					});
				}
				var disStr = "";
				for(var i in posterValObj){
					disStr += (","+posterValObj[i]);
				}
				disStr = disStr.substring(1);
				$("#posterVal").val(disStr);
				uploadCount--;
				$loadings.hide();
			},
			error:function(data){
				uploadCount--;
				$loadings.hide();
			}
		});
	}
} 

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