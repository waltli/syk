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

