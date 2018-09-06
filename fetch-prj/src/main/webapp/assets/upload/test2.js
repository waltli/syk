function picUpload(op){
	op = $.extend({}, {
		"url":ctx+"/upload",
		"width": "89px",
		"height": "89px",
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
	var previewUri = $place.attr("preview") || "";
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
	
	//预览区域
	var $preview = $('<div class="pic-preview" style="height:'+op.height+';width:'+op.width+';">'+
					 '    <img style="max-height:'+op.height+'; max-width:'+op.width+';" src="'+previewUri+'">'+
					 '</div>');
	
	
	
	
	
	
	
	
	//新建上传者
	var fileId = component_count+"_file_"+new Date().getTime();
	var $typeFile = $('<input type="file" class="file" name="file" id="'+fileId+'">');
	
	//加入新组件并删除标识者
	$replace.append($loading).append($close).append($preview).append($typeFile);
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
		//上传前的预览
		preview(op, this, $preview[0]);
		
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
	
	if(!op.close){
		$close.hide();
	}
	
}