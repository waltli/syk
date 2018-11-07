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
	}
}

$(function(){
	$('img').lazyload({
		effect:'fadeIn'
	});
});
