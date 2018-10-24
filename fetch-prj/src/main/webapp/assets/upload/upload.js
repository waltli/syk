(function(){
	var head = document.getElementsByTagName("head")[0];
	
	var link = document.createElement("link");
	link.rel = "stylesheet";
	link.href=ctxassets+"/upload/upload.css";
	head.appendChild(link);
	
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.src = ctxassets+"/upload/ajaxfileupload.js";
	head.appendChild(script);
	
})()

var component_count = 0;

function preview(op, typeFile, preview){
	var fileName = typeFile.value;
	var suffix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
    try {
		var url = null;
		var file = typeFile.files[0];
	    if (window.createObjectURL != undefined) {
	        url = window.createObjectURL(file);
	    } else if (window.URL != undefined) {
	        url = window.URL.createObjectURL(file);
	    } else if (window.webkitURL != undefined) {
	        url = window.webkitURL.createObjectURL(file);
	    }
    	preview.innerHTML = '<img src="'+url+'" style="max-height:'+op.height+'; max-width:'+op.width+';" />';
     } catch (e) {
    	 typeFile.select();
    	 top.parent.document.body.focus();
         var src = document.selection.createRange().text;
         document.selection.empty();
         preview.innerHTML="";
         preview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
         preview.style.maxWidth = op.maxWidth;
         preview.style.maxHeight = op.maxHeight;
         preview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
     }
}

function cancelPreview(op, preview){
	preview.style.filter = "";
	preview.innerHTML = '<img style="max-height:'+op.height+'; max-width:'+op.width+';" />';
}

function upload(op){
	$.ajaxFileUpload({
		url:op.url,
		data:op.data,
		secureuri:false,                       //是否启用安全提交,默认为false
        fileElementId:op.fileElementId,        //单个文件选择框的id属性，如果多个请用数组
        dataType:'json',                       //服务器返回的格式,可以是json或xml等
        success:function(data, status){
    		op.success(data, status);
        },
        error:function(data, status, e){
    		op.error(data, status, e);
        }
	});
}

function fileUpload(op){
	if(!op || !op.placeId){
		return;
	}
	op = $.extend({}, {
		"url":ctx+"/upload",
		"close":true,
		"maxSize":5*1024*1024,
		"suffixes":["torrent"]
	}, op);
	component_count++;
	//位置标识者
	var $place = $("#"+op.placeId);
	var $prev = $place.prev(); //同级元素
	var $parent = $place.parent(); //父元素
	var replaceId = op.placeId //新建的replaceId
	var $replace = $('<div id="'+replaceId+'" class="replace"></div>');//新建group
	
	//遮蔽者
	var loadingId = component_count+"_loading_"+new Date().getTime();
	var $loading = $('<div id="'+loadingId+'" class="loading">'+
					 '    <i class="fa fa-spinner fa-spin"></i>'+
					 '</div>');
	//关闭者
	var closeId = component_count+"_close_"+new Date().getTime();
	var $close = $('<sup id="'+closeId+'" class="file-upload-del"></sup>');
	
	//上传入口
	var enterId = component_count+"_enter_"+new Date().getTime();
	var $enter = $('<span id="'+enterId+'" class="file-enter">点击上传</span>');
	
	//再次上传入口
	var againId = component_count+"_again_"+new Date().getTime();
	var $again = $('<span id="'+againId+'" class="file-again">重新上传</span>');
	
	//预览区域
	var $preview = $('<a class="file-preview" target="_blank" style="display:none;"></a>');
	
	//新建上传者
	var fileId = component_count+"_file_"+new Date().getTime();
	var $typeFile = $('<input type="file" class="file" name="file" id="'+fileId+'">');
	
	//加入新组件并删除标识者
	$replace.append($loading).append($enter).append($preview).append($again).append($close).append($typeFile);
	$place.remove();
	if($prev.length == 0){
		$parent.append($replace)
	}else {
		$prev.after($replace);
	}
	
	//对入口绑定点击时间触发上传者的点击事件
	$enter.unbind("click").bind("click", function(){
		$("#"+fileId).trigger("click");
	});
	
	$again.unbind("click").bind("click", function(){
		$("#"+fileId).trigger("click");
	});
	
	//对上传者绑定活体改变事件，必须是活体的，因为ajaxfileupload.js会对原元素重建
	$replace.on("change", "#"+fileId, function(){
		if(!$(this).val()){
			return;
		}
		
		var fileName = $typeFile[0].files[0].name;
		var suffixFix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
		if(op.suffixes){
			var suffixeFixStr = op.suffixes.join(",").toLowerCase();
			var suffixeFixArr = suffixeFixStr.split(",");
			if(suffixeFixArr.indexOf(suffixFix) == -1){
				if(typeof op.error == 'function'){
	        		op.error(null, "error", "支持的文件格式："+ suffixeFixStr);
	        	}
				return;
			}
		}
		
		var fileSize = $typeFile[0].files[0].size;
		if(fileSize > op.maxSize){
			if(typeof op.error == 'function'){
				var size = op.maxSize/1024/1024;
        		op.error(null, "error", "文件大小不得超过"+size+"M！");
        	}
			return;
		}
		
		//上传
		if(typeof op.before == "function"){
			op.before();
		}
		
		$("#"+loadingId).css({"display": "-webkit-flex", "display": "flex"});
		
		upload({
			url:op.url,
			data:op.data,
			fileElementId:fileId,
			success:function(data, status){
				if(typeof op.success == 'function'){
	        		op.success(data, status);
	        	}
				$("#"+loadingId).hide();
				if(data.requestResult){
					var val = data.uri;
					var fileName = val.substring(val.lastIndexOf("/")+1);
					$preview.text(fileName);
					$preview.attr("href",ctx+"/"+data.uri);
					$enter.hide();
					$preview.show();
					$again.show();
				}
			},
			error:function(data, status, e){
	        	if(typeof op.error == 'function'){
	        		op.error(data, status, e);
	        	}
	        	$("#"+loadingId).hide();
	        }
		});
	});
	
	$close.unbind("click").bind("click", function(){
		if(typeof op.del == "function"){
			op.del();
		}
		$replace.remove();
	});
	
	if(!op.close){
		$close.hide();
	}
	
}

function picUpload(op){
	if(!op || !op.placeId){
		return;
	}
	op = $.extend({}, {
		"url":ctx+"/upload",
		"width": "89px",
		"height": "89px",
		"close":true,
		"clear":true,
		"maxSize":5*1024*1024,
		"suffixes":["jpg","png","gif"]
	}, op);
	
	component_count++;
	//位置标识者
	var $place = $("#"+op.placeId);
	var previewUri = $place.attr("preview") || "";
	var $prev = $place.prev(); //同级元素
	var $parent = $place.parent(); //父元素
	var replaceId = op.placeId //新建的replaceId
	var $replace = $('<div id="'+replaceId+'" class="replace"></div>');//新建group
	//遮蔽者
	var loadingId = component_count+"_loading_"+new Date().getTime();
	var $loading = $('<div id="'+loadingId+'" class="loading" style="height:'+op.height+';width:'+op.width+';">'+
					 '    <i class="fa fa-spinner fa-spin"></i>'+
					 '</div>');
	//关闭者
	var closeId = component_count+"_close_"+new Date().getTime();
	var $close = $('<sup id="'+closeId+'" class="pic-upload-del"></sup>');
	
	var clearId = component_count+"_clear_"+new Date().getTime();
	var $clear = $('<sup id="'+clearId+'" class="pic-upload-clear"></sup>');
	
	//预览区域
	var $preview = $('<div class="pic-preview" style="height:'+op.height+';width:'+op.width+';">'+
					 '    <img style="max-height:'+op.height+'; max-width:'+op.width+';" src="'+previewUri+'">'+
					 '</div>');
	//新建上传者
	var fileId = component_count+"_file_"+new Date().getTime();
	var $typeFile = $('<input type="file" class="file" name="file" id="'+fileId+'">');
	
	//加入新组件并删除标识者
	$replace.append($loading).append($clear).append($close).append($preview).append($typeFile);
	$place.remove();
	if($prev.length == 0){
		$parent.append($replace)
	}else {
		$prev.after($replace);
	}
	
	//对预览者绑定点击时间触发上传者的点击事件
	$preview.unbind("click").bind("click", function(){
		$("#"+fileId).trigger("click");
	});
	
	//对上传者绑定活体改变事件，必须是活体的，因为ajaxfileupload.js会对原元素重建
	$replace.on("change", "#"+fileId, function(){
		if(!$(this).val()){
			return;
		}
		
		var fileName = $("#"+fileId)[0].files[0].name;
		var suffixFix = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
		if(op.suffixes){
			var suffixeFixStr = op.suffixes.join(",").toLowerCase();
			var suffixeFixArr = suffixeFixStr.split(",");
			if(suffixeFixArr.indexOf(suffixFix) == -1){
				if(typeof op.error == 'function'){
	        		op.error(null, "error", "支持的文件格式："+ suffixeFixStr);
	        	}
				return;
			}
		}
		
		var fileSize = $("#"+fileId)[0].files[0].size;
		if(fileSize > op.maxSize){
			if(typeof op.error == 'function'){
				var size = op.maxSize/1024/1024;
				op.error(null, "error", "文件大小不得超过"+size+"M！");
			}
			return;
		}
		
		//上传前的预览
		preview(op, this, $preview[0]);
		
		if(typeof op.before == "function"){
			op.before();
		}
		
		//上传
		$("#"+loadingId).css({"display": "-webkit-flex", "display": "flex"});
		upload({
			url:op.url,
			data:op.data,
			fileElementId:fileId,
			success:function(data, status){
				if(typeof op.success == 'function'){
	        		op.success(data, status);
	        	}
				$("#"+loadingId).hide();
			},
			error:function(data, status, e){
        		cancelPreview(op, $preview[0]);
	        	if(typeof op.error == 'function'){
	        		op.error(data, status, e);
	        	}
	        	$("#"+loadingId).hide();
	        }
		});
	});
	
	$close.unbind("click").bind("click", function(){
		if(typeof op.del == "function"){
			op.del();
		}
		$replace.remove();
	});
	
	$clear.unbind("click").bind("click", function(){
		if(typeof op.clear == "function"){
			op.clear();
		}
		$preview.find("img").attr("src","");
	});
	
	if(!op.close){
		$close.hide();
	}
	
}