function fileUpload(op){
	op = $.extend({}, {
		"url":ctx+"/upload",
		
		
		"close":true,
		"maxSize":5*1024*1024
	}, op);
	if(!op || !op.placeId){
		return;
	}
	
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
	
	//预览区域
	var $preview = $('<a class="file-preview" target="_blank" style="display:none;"></a>');

	//上传入口
	var enterId = component_count+"_enter_"+new Date().getTime();
	var $enter = $('<span id="'+enterId+'" class="file-enter">点击上传</span>');
	
	//再次上传入口
	var again = component_count+"_again_"+new Date().getTime();
	var $again = $('<span id="'+enterId+'" class="file-again">重新上传</span>');
	
	
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
		
		
		
		if(typeof op.before == "function"){
			op.before();
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