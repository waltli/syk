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
			layer.msg(data.error.join(",") || '服务器开小差了 - -!');
			return false;
		}
		if(!this.user.isLogin(data)){
			return false;
		}
		if(typeof foo == 'function'){
			foo();
		}
		return true;
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
		login:function(loginType, foo){
			//普通登录调用
		},
		openLogin:function(openType, foo){
			var layerIndex;
			this.openBack = function(data){
				syk.verify(data, function(){
					layer.close(layerIndex);
					if(typeof foo == 'function'){
						foo(data.obj);
					}
				});
			};
			$.ajax({
				url:ctx+"/pre",
				data:{"openType":openType},
				type:"get",
				success:function(data){
					debugger;
					syk.verify(data, function(){
						var authorizeUrl = data.obj.authorizeUrl;
						var h = ($(window).height() - 480 )/2 - 20;
						layerIndex = layer.open({
						    type: 1,
						    shadeClose: true,
						    title: false,
						    closeBtn: [0, true],
						    shade: [0.8, '#000'],
						    offset: [h + 'px',''],
				  			area: ['630px', '380px'],
						    content:'<div style="width:630px;height:380px;overflow:hidden"><iframe frameborder="0"  scrolling="auto" src="'+authorizeUrl+'" width="100%" height="800"></iframe></div>',
						    end:function(){
						    	//用户自己点击关闭弹出层
						    }
						});
					});
				},
				error : function(data){
					console.log(data);
				}
			});
		},
		logout:function(token, foo){
			$.ajax({
				url:ctx+"/logout",
				data:{"username":token.username},
				type:"post",
				success:function(data){
					syk.verify(data, function(){
						if(typeof foo == 'function'){
							foo(data);
						}
					});
				},
				error : function(data){
					console.log(data);
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
