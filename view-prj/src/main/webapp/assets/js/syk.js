var syk = {
	fmt:function(sources, fmtData){
		for(var i in fmtData){
			var keyRegx = new RegExp("\{%"+i+"\}", "gm"); 
			var value = fmtData[i];
			sources = sources.replace(keyRegx, value);
		}
		return sources;
	},
	verify:function(data, foo){
		if(!data || !data.status){
			return layer.msg(result.message || '操作失败！');
		}
		if(!this.user.isLogin(data)){
			this.user.login();
			return;
		}
		if(typeof foo == 'function'){
			foo();
		}
	},
	user: {
		isLogin:function(data){
			//没有登录，请登录
			if(data.code === 300){
				return layer.msg(data.message || '请先登录。'),!1;
			}
			return !0;
			throw "data is undefined!";
		},
		login:function(){
			var h = ($(window).height() - 480 )/2 - 20;
			layer.open({
			    type: 1,
			    shadeClose: true,
			    title: false,
			    closeBtn: [0, true],
			    shade: [0.8, '#000'],
			    offset: [h + 'px',''],
	  			area: ['630px', '380px'],
			    content:'<div style="width:630px;height:380px;overflow:hidden"><iframe frameborder="0"  scrolling="auto" src="" width="100%" height="800"></iframe></div>',
			    end:function(){
					//用户自己关闭，调用回调方法。
				}
			});
			
			
			
//			var load = layer.load();
			//noinspection JSAnnotator
//			document.domain = 'sojson.com';
//			so.ajax({
//				url: "/user/open/getQQUrl.shtml?" + new Date().getTime(),
//				dataType: "json",
//				type: "post",
//				success: function(i) {
//					layer.close(load);
//					var h = ($(window).height() - 480 )/2 - 20;
//					layer.open({
//					    type: 1,
//					    shadeClose: true,
//					    title: false,
//					    closeBtn: [0, true],
//					    shade: [0.8, '#000'],
//					    offset: [h + 'px',''],
//			  			area: ['630px', '380px'],
//					    content:'<div style="width:630px;height:380px;overflow:hidden"><iframe frameborder="0"  scrolling="auto" src="'+ i.url +'" width="100%" height="800"></iframe></div>',
//					    end:function(){
//							loginBack();//用户自己关闭，调用回调方法。
//						}
//					});
//				}
//			});
		}
	}
}

$(function(){
	$('img').lazyload({
		effect:'fadeIn'
	});
});
