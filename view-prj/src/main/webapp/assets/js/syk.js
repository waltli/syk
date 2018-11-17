var syk = {
	fmt:function(sources, fmtData){
		if(fmtData != null){
			for(var i in fmtData){
				var keyRegx = new RegExp("\{%"+i+"\}", "gm"); 
				var value = fmtData[i];
				sources = sources.replace(keyRegx, value);
			}
		}
		sources = sources.replace(/\{%.*?\}/g, "");
		return sources;
	},
	verify:function(data, foo){
		if(!data || !data.status){
			return layer.msg(data.error.join(",") || '服务器开小差了 - -!');
		}
		if(!this.user.isLogin(data)){
//			this.user.login();
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
			var appid="101519587";
			var backUrl = encodeURIComponent(ctx+"msg/step1");
			var state = "123456789abcdefg";
			var url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="+appid+"&redirect_uri="+backUrl+"&state="+state;
			layer.open({
			    type: 1,
			    shadeClose: true,
			    title: false,
			    closeBtn: [0, true],
			    shade: [0.8, '#000'],
			    offset: [h + 'px',''],
	  			area: ['630px', '380px'],
			    content:'<div style="width:630px;height:380px;overflow:hidden"><iframe frameborder="0"  scrolling="auto" src="'+url+'" width="100%" height="800"></iframe></div>',
			    end:function(){
					//用户自己关闭，调用回调方法。
				}
			});
		}
	},
	// 时间格式化
	date: {
		parseDate: function(e) {
			return e.parse("2011-10-28T00:00:00+08:00") &&
			function(t) {
				return new e(t);
			} || e.parse("2011/10/28T00:00:00+0800") &&
			function(t) {
				return new e(t.replace(/-/g, "/").replace(/:(\d\d)$/, "$1"));
			} || e.parse("2011/10/28 00:00:00+0800") &&
			function(t) {
				return new e(t.replace(/-/g, "/").replace(/:(\d\d)$/, "$1").replace("T", " "));
			} ||
			function(t) {
				return new e(t);
			};
		} (Date),
		fullTime: function(e) {
			var t = S.parseDate(e);
			return t.getFullYear() + "年" + (t.getMonth() + 1) + "月" + t.getDate() + "日 " + t.toLocaleTimeString()
		},
		elapsedTime: function(e) {
			var t = S.parseDate(e),
			s = new Date,
			a = (s - 0 - t) / 1e3;
			return 10 > a ? "刚刚": 60 > a ? Math.round(a) + "秒前": 3600 > a ? Math.round(a / 60) + "分钟前": 86400 > a ? Math.round(a / 3600) + "小时前": (s.getFullYear() == t.getFullYear() ? "": t.getFullYear() + "年") + (t.getMonth() + 1) + "月" + t.getDate() + "日"
		}
	},
	insert: {
		outTag:function(){
			return document.getElementsByTagName("head")[0] || document.getElementsByTagName("body")[0];
		},
		css:function(e,attr) {
			var s = document.createElement("link");
			s.type = "text/css",
			s.rel = "stylesheet",
			s.href = e,
			this.outTag().appendChild(s);
			attr && $(s).attr(attr);
		},
		js:function(a,option){
			var r = document.createElement("script");
			r.type = "text/javascript",
			r.src = a,
			r.charset = "utf-8";
			this.outTag().appendChild(r);
			if(option && typeof option =='object'){
				if(option.callback){//回调方法。
					r.onload=option.callback;
				}
				if(option.attr){
					$(r).attr(option.attr);
				}
			}
		}
	}
}

$(function(){
	$('img').lazyload({
		effect:'fadeIn'
	});
});
