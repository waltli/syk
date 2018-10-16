$(function(){
	$('img').lazyload({
		effect:'fadeIn'
	});
	
	$(document).keyup(function(event){
		if(event.keyCode ==13){
			if($("#query").is(":focus")){
				$("#searchDouban").trigger("click");
			}
		}
	});
	
	toastr.options = {
			  "closeButton": false,
			  "debug": false,
			  "positionClass": "toast-top-center",
			  "onclick": null,
			  "showDuration": "300",
			  "hideDuration": "1000",
			  "timeOut": "5000",
			  "extendedTimeOut": "1000",
			  "showEasing": "swing",
			  "hideEasing": "linear",
			  "showMethod": "fadeIn",
			  "hideMethod": "fadeOut"
	}
	
});

function picUploadAll(placeId, fileType, valId, valName, valStrId){
	picUpload({
		placeId:placeId,
		data:{"fileType":fileType},
		close:false,
		before: function(){
			uploadCount++;
		},
		clear: function(){
			$("#"+valId).val("");
			finalVal(valName, valStrId);
		},
		success: function(data){
			if(!data.status){
				return;
			}
			$("#"+valId).val(data.obj.subDir);
			finalVal(valName, valStrId);
			uploadCount--;
		},
		error: function(data, status, e){
			toastr.error(e);
			uploadCount--
		}
	});
}

function finalVal(valName, valStrId){
	if(valStrId){
		var subDirStr = "";
		var hasVal = false;
		$("[name="+valName+"]").each(function(i,o){
			subDirStr += (","+$(o).val());
			if($(o).val()){
				hasVal = true;
			}
		});
		if(hasVal){
			subDirStr = subDirStr.substring(1);
			$("#"+valStrId).val(subDirStr);
		}else {
			$("#"+valStrId).val("");
		}
	}
}

