//评论框
var html = [];
html.push('<div class="ds-replybox ds-inline-replybox" box="{%location}">');
html.push('	<a class="ds-avatar" href="{%link}" target="_blank" title="{%nickname}">');
html.push('		<img src="{%avatarUrl}" alt="{%nickname}">');
html.push('	</a>');
html.push('	<form method="post" message-post="" onsubmit="return false;" url="'+ctx+'/pushMessage" >');
html.push('		<input name="parentPrn" value="{%parentPrn}" type="hidden">');
html.push('		<input name="pkey" value="{%pkey}" type="hidden">');
html.push('		<input name="level" value="{%level}" type="hidden">');
html.push('		<input name="parentPrns" value="{%parentPrns}" type="hidden">');
html.push('		<input name="rootPrn" value="{%rootPrn}" type="hidden">');
html.push('		<div class="ds-textarea-wrapper ds-rounded-top">');
html.push('			<textarea message="" id="{%location}" name="message" _title="Ctrl+Enter快捷提交" placeholder="说点什么吧…"></textarea>');
html.push('			<pre class="ds-hidden-text"></pre>');
html.push('		</div>');
html.push('		<div class="ds-post-toolbar">');
html.push('			<div class="ds-post-options ds-gradient-bg">');
html.push('				<span class="ds-sync"><a style="color:#d32" target="_blank"href="http://www.sojson.com/tag_duoshuo.html">本评论系统实现方案</a></span>');
html.push('			</div>');
html.push('			<button class="ds-post-button" submit="{%location}">发布</button>');
html.push('			<div class="ds-toolbar-buttons">');
html.push('				<a class="ds-toolbar-button ds-add-emote" emote="{%location}" title="插入表情"></a>');
html.push('			</div>');
html.push('		</div>');
html.push('	</form>');
html.push('</div>');
//单个评论输出模块
var xhtml = [];
xhtml.push('<li class="ds-post "  {%none} data-post-prn="{%prn}" nickname="{%author_nickname}" >');
xhtml.push('	<div class="ds-post-self">');
xhtml.push('		<div class="ds-avatar" data-user-prn="{%author_prn}">');
xhtml.push('			<a rel="nofollow author" target="_blank" href="{%author_url}" title="{%author_nickname}" show-user="" data-user-prn="{%author_prn}">');
xhtml.push('				<img src="{%author_avatarUrl}" alt="{%author_nickname}">');
xhtml.push('			</a>');
xhtml.push('		</div>');
xhtml.push('		<div class="ds-comment-body">');
xhtml.push('			<div class="ds-comment-header">');
xhtml.push('				<a class="ds-user-name ds-highlight" show-user="" href="{%author_url}" rel="nofollow" target="_blank" data-user-prn="{%author_prn}">{%author_nickname}</a>');
xhtml.push('			</div>');
xhtml.push('			<p>{%message}</p>');
xhtml.push('			<div class="ds-comment-footer ds-comment-actions  {%likes_class}" level="{%level}" parentPrns="{%parentPrns}" data_prn="{%prn}" author_prn="{%author_prn}" rootPrn="{%rootPrn}" >');
xhtml.push('				<span class="ds-time" datetime="{%createdTimeZooe}" title="{%createdYMDHMSStr}">{%createdTimeFmt}</span>');
xhtml.push('				<a class="ds-post-reply" href="javascript:void(0);"><span class="ds-icon ds-icon-reply"></span>回复</a>');
xhtml.push('				<a class="ds-post-likes" href="javascript:void(0);"><span class="ds-icon ds-icon-like"></span>顶({%likes})</a>');
/*
xhtml.push('				<a class="ds-post-repost" href="javascript:void(0);"><span class="ds-icon ds-icon-share"></span>转发</a>');
xhtml.push('				<a class="ds-post-report" href="javascript:void(0);"></a>');
*/
xhtml.push('				<a class="ds-post-delete-a none" href="javascript:void(0);"><span class="ds-icon ds-icon-delete"></span>删除</a>');
xhtml.push('			</div>');
xhtml.push('		</div>');
xhtml.push('	</div>');
xhtml.push('</li>');
	
//单个提示
var _tips = [];
_tips.push('<div id="ds-bubble" style="display:none;">');
_tips.push('  <div class="ds-bubble-content" id="ds-user-card">');
_tips.push('  	<a href="{%url}"  class="ds-avatar" target="_blank">');
_tips.push('  		<img src="{%avatarUrl}" alt="{%nickname}">');
_tips.push('  	</a>');
_tips.push('  	<a href="{%url}" class="ds-user-name ds-highlight" target="_blank">{%nickname}</a>');
_tips.push('  	<p class="ds-user-card-meta">');
_tips.push('  		<a href="{%url}" target="_blank">');
_tips.push('  			<span class="ds-highlight">{%count}</span>条评论');
_tips.push('  		</a>');
_tips.push('  	</p>');
_tips.push('  </div>');
_tips.push('  <div class="ds-arrow ds-arrow-down ds-arrow-border"></div>');
_tips.push('</div>');



//多层回复，单个显示
var _history=[];
_history.push('<div id="ds-bubble" style="display:none;">');
_history.push('	 <div class="ds-bubble-content" id="ds-ctx-bubble" >');
_history.push('		<ul id="ds-ctx">');
_history.push('			<li class="ds-ctx-entry" >');
_history.push('				<div class="ds-avatar">');
_history.push('					<a rel="nofollow author" target="_blank" href="{%url}" title="{%nickname}">');
_history.push('						<img src="{%avatarUrl}" alt="{%nickname}">');
_history.push('					</a>');
_history.push('				</div>');
_history.push('				<div class="ds-ctx-body">');
_history.push('					<div class="ds-ctx-head">');
_history.push('						<a rel="nofollow author" target="_blank"  href="{%url}">{%nickname}</a>');
_history.push('						<a href="javascript:void(0);"  rel="nofollow" class="ds-time"  datetime="{%createdTimeZooe}"  title="{%createdYMDHMSStr}">{%createdTimeFmt}</a>');
_history.push('					</div>');
_history.push('					<div class="ds-ctx-content">{%parent_message}</div>');
_history.push('				</div>');
_history.push('			</li>');
_history.push('		</ul>');
_history.push('		<div class="ds-bubble-footer">');
_history.push('			<a class="ds-ctx-open" href="javascript:void(0);" more_message="显示多层对话" data-user-prn="{%prn}" data-post-prn="{%message_prn}" parentPrns="{%parentPrns}">查看对话</a>');
_history.push('		</div>');
_history.push('	 </div>');
_history.push('	 <div class="ds-arrow ds-arrow-down ds-arrow-border"></div>');
_history.push('	 <div class="ds-arrow ds-arrow-down"></div>');
_history.push('</div>');

//多层记录查看[显示多层对话]
var historys = [],historys_body=[];
historys.push('<li class="ds-ctx-entry" data-post-prn="{%prn}">');
historys.push('	<div class="ds-avatar">');
historys.push('		<a rel="nofollow author" target="_blank" href="{%url}" title="{%nickname}">');
historys.push('			<img src="{%avatarUrl}" alt="{%nickname}"/>');
historys.push('		</a>');
historys.push('	</div>');
historys.push('	<div class="ds-ctx-body">');
historys.push('		<div class="ds-ctx-head">');
historys.push('			<a rel="nofollow author" target="_blank" href="{%url}">{%nickname}</a>');
historys.push('			<a href="javascript:void(0);" target="_blank"  class="ds-time"  datetime="{%createdTimeZooe}"  title="{%createdYMDHMSStr}">{%createdTimeFmt}</a>');
historys.push('			<div class="ds-ctx-nth" >{%no}楼</div>');
historys.push('		</div>');
historys.push('		<div class="ds-ctx-content">{%message}</div>');
historys.push('	</div>');
historys.push('</li>');

historys_body.push('<div id="ds-wrapper" style="display: none;">');
historys_body.push('	<div class="ds-dialog" id="ds-reset" style="width: 600px;">');
historys_body.push('		<div class="ds-dialog-inner ds-rounded">');
historys_body.push('			<div class="ds-dialog-body" style="max-height: 350px; overflow-y: auto; padding-top: 10px;">');
historys_body.push('				<h2>查看对话</h2>');
historys_body.push('				<ol id="ds-ctx">{%historys_body}</ol>');
historys_body.push('			</div>');
historys_body.push('			<div class="ds-dialog-footer">');
historys_body.push('				<a href="http://www.sojson.com/tag_duoshuo.html" target="_blank" class="ds-logo"></a>');
historys_body.push('				<span>社会化评论框</span>');
historys_body.push('			</div>');
historys_body.push('			<a class="ds-dialog-close" href="javascript:void(0);" title="关闭"></a>');
historys_body.push('		</div>');
historys_body.push('	</div>');
historys_body.push('</div>');


var box = [];
box.push('<div id="ds-reset">');
box.push('		<div class="ds-rounded" id="ds-hot-posts" style="display: none;">');
box.push('			<div class="ds-header ds-gradient-bg">被顶起来的评论</div>');
box.push('			<ul></ul>');
box.push('		</div>');
box.push('		<div class="ds-alert red">多说使用将于<code>2017年6月1日</code>停止，本站已经迁移，迁移方案详见：<a href="http://www.sojson.com/tag_duoshuo.html">http://www.sojson.com/tag_duoshuo.html</a></div>');
box.push('		<div class="ds-toolbar" style="display: none;">');
box.push('			<div class="ds-account-control">');
box.push('				<span class="ds-icon ds-icon-settings"></span>'); 
box.push('				<span>个人中心</span>');
box.push('				<ul>');
box.push('					<li><a target="_blank"rel="nofollow" href="http://www.sojson.com/admin.shtml">个人资料</a></li>');
box.push('					<li><a  href="javascript:void(0);" onclick="logout();" style="border-bottom: none">退出登录</a></li>');
box.push('				</ul>');
box.push('			</div>');
box.push('			<div class="ds-visitor">');
box.push('				<a class="ds-visitor-name"   token_nickname=""  href="javascript:void(0);">请登录</a>');
box.push('			</div>');
box.push('		</div>');
//<#--统计，排序-->
box.push('		<div class="ds-comments-info" id="orderMenu" style="display: none;">');
box.push('			<div class="ds-sort">');
box.push('				<a class="ds-order-desc ds-current">最新</a>');
box.push('				<a class="ds-order-asc">最早</a>');
box.push('				<a class="ds-order-hot">最热</a>');
box.push('			</div>');
box.push('			<ul class="ds-comments-tabs">');
box.push('				<li class="ds-tab">');
box.push('					<a class="ds-comments-tab-duoshuo ds-current" href="javascript:void(0);"><span class="ds-highlight" len="">0</span>条评论</a>');
box.push('				</li>');
box.push('			</ul>');
box.push('		</div>');
//<#--/统计，排序-->
box.push('		<ul class="ds-comments" id="body_message">');
box.push('			<li class="ds-post ds-post-placeholder">正在加载评论... ... </li>');
box.push('		</ul>');
box.push('	</div>');
box.push('</div>');



//时间格式化
var b = 0;
var S = {
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
			a = (s - b - t) / 1e3;
			return 10 > a ? "刚刚": 60 > a ? Math.round(a) + "秒前": 3600 > a ? Math.round(a / 60) + "分钟前": 86400 > a ? Math.round(a / 3600) + "小时前": (s.getFullYear() == t.getFullYear() ? "": t.getFullYear() + "年") + (t.getMonth() + 1) + "月" + t.getDate() + "日"
		}
};

(function($){
	// QQ表情插件
	$.fn.selectContents=function(){ 
		$(this).each(function(i){ 
			var node = this; 
			var selection, range, doc, win; 
			if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof win.getSelection != 'undefined' && typeof doc.createRange != 'undefined' && (selection = window.getSelection()) && typeof selection.removeAllRanges != 'undefined'){ 
				range = doc.createRange(); 
				range.selectNode(node); 
				if(i == 0){ 
					selection.removeAllRanges(); 
				} 
				selection.addRange(range); 
			} else if (document.body && typeof document.body.createTextRange != 'undefined' && (range = document.body.createTextRange())){ 
				range.moveToElementText(node); 
				range.select(); 
			} 
		}); 
	}, 
	$.fn.setCaret=function(){
		var msie = /msie/.test(navigator.userAgent.toLowerCase());
		if(!msie) return; 
		var initSetCaret = function(){ 
			var textObj = $(this).get(0); 
			textObj.caretPos = document.selection.createRange().duplicate(); 
		}; 
		$(this).click(initSetCaret).select(initSetCaret).keyup(initSetCaret); 
	}, 

	$.fn.insertAtCaret=function(textFeildValue){ 
		var textObj = $(this).get(0); 
		if(document.all && textObj.createTextRange && textObj.caretPos){ 
			var caretPos=textObj.caretPos; 
			caretPos.text = caretPos.text.charAt(caretPos.text.length-1) == '' ? 
			textFeildValue+'' : textFeildValue; 
		} else if(textObj.setSelectionRange){ 
			var rangeStart=textObj.selectionStart; 
			var rangeEnd=textObj.selectionEnd; 
			var tempStr1=textObj.value.substring(0,rangeStart); 
			var tempStr2=textObj.value.substring(rangeEnd); 
			textObj.value=tempStr1+textFeildValue+tempStr2; 
			textObj.focus(); 
			var len=textFeildValue.length; 
			textObj.setSelectionRange(rangeStart+len,rangeStart+len); 
			textObj.blur(); 
		}else{ 
			textObj.value+=textFeildValue; 
		} 
	},
	$.fn.qqFace = function(options){
		var defaults = {
			id : 'facebox', 
			assign:'main', 
			path:ctxassets+'/message/img/emote/',
			tip : 'em_'
		};
		
		var option = $.extend(defaults, options);
		var assign = $('#'+option.assign);
		var id = option.id;
		var path = option.path;
		var tip = option.tip;
		var self = $(this);
		
		if(assign.length<=0){
			alert('缺少表情赋值对象。');
			return false;
		}
		self.click(function(e){
			var strFace, labFace;
			if($('#'+id).length<=0){
				strFace = '<div id="'+id+'" style="position:absolute;display:none;z-index:1000;" class="qqFace">' +
							  '<table border="0" cellspacing="0" cellpadding="0"><tr>';
				for(var i=1; i<=75; i++){
					labFace = '['+tip+i+']';
					strFace += '<td><img src="'+path+i+'.gif" '+ assign.attr('id') +'="'+ labFace+'" /></td>';
					if( i % 15 == 0 ) strFace += '</tr><tr>';
				}
				strFace += '</tr></table></div>';
			}
			$('body').append(strFace);
			//位置
			$('#'+id).css({top:self.offset().top + 22,left:self.offset().left-5}).show(200);
			e.stopPropagation();
		});
		
		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};
	
	$('body').on('click','[small]',function(){
		var labFace = $(this).attr('small');
		$("#small").setCaret();
		$("#small").insertAtCaret(labFace);
	});
	$('body').on('click','[main]',function(){
		var labFace = $(this).attr('main');
		$("#main").setCaret();
		$("#main").insertAtCaret(labFace);
	});
})(jQuery);

//main方法
var loadMessage = function(){
	//加载css
//	so.insert.css('//cdn.sojson.com/css/common/message/embed.default.css',{id:"message-css"});
	$.extend({
		unselectContents: function(){ 
			if(window.getSelection){
				window.getSelection().removeAllRanges(); 
			}else if(document.selection){ 
				document.selection.empty(); 
			}
		}
	}); 
	
	//输出评论结构层
	$('#ds-thread').html(box.join(''));;
	
	//获取用户信息
	var token ={};
	var pkey = location.pathname;//评论数据Key
	
	//评论输出box
	var body = $("#body_message");
	//单个评论输出
	var outSingle = function(o){
		
		var single = xhtml.join("");
		var author = o.author;
		var likes_class = "";//选择的class元素
		
		if(o.liked){//如果已赞。
			likes_class ="ds-post-liked";//点亮
		}
		
		//构造单条信息的需要的参数
		return syk.fmt(single,$.extend(o,{
			author_prn:author.prn,
			author_url:author.url,
			author_nickname:author.nickname,
			author_avatarUrl:author.avatarUrl,
			likes_class:likes_class,
			createdTimeFmt:S.elapsedTime(o.createdTimeZooe)//时间格式化表示
			}))
	};
	var init_message = function(orderMarker){
		//判断有没有排名标示
		var args =  orderMarker?  {key:pkey,orderMarker:orderMarker}:{key:pkey};
		//清空信息框
		body.empty();
		//根据key获取评论信息
		$.getJSON(ctxassets+"/message/me.json",function(result){
			token = result.message.token;//赋值token
			var fmtData = {};
			if(token && !$.isEmptyObject(token)){
				$("[token_nickname]").text(token.nickname).attr('href',token.link);
			 	$(".ds-toolbar").show(200);
				fmtData = $.extend(token,{"parentPrn":0,location:"main",level:1,parentPrns:'0',rootPrn:'',pkey:pkey});
			}else{
				fmtData = token = {"parentPrn":0,location:"main",level:1,parentPrns:'0',rootPrn:'',nickname:"请登录",avatarUrl:ctxassets+"/message/img/default_avatar_50.gif",prn:"-1",url:"http://www.sojson.com/admin.shtml",link:"http://www.sojson.com/admin.shtml",pkey:pkey};
			}
			//初次加载把评论框加上
			if($('[box="main"]').length === 0){
				$("#orderMenu").before(syk.fmt(html.join(''), fmtData).replace('ds-inline-replybox',''));
			}
			//加载emote
			$('[emote="main"]').qqFace({assign:'main'});
			$('#orderMenu').show(100);
			//多少条评论
			$('span[len]').text(result.message.data.length);
			//没有评论
			if(!result.message.data.length ||result.message.data.length == 0){
				return body.html('<li class="ds-post ds-post-placeholder">还没有评论，沙发等你来抢</li>'),!1;
			}
			
			//热评
			if(result && result.message.hotData && result.message.hotData.length){
				var outArray = [];
				$.each(result.message.hotData,function(){
					
					//获取到单条的HTML
					var outHtml = syk.fmt(outSingle(this), {none:""});
					outArray.push(outHtml);
				});
				$("#ds-hot-posts >ul").html(outArray.join(''));
				$("#ds-hot-posts").show(100);
			}else{
				$("#ds-hot-posts").remove();
			}
			
			var users = {};//装用户信息
			if(result && result.message && result.message.data && result.message.data.length){
				$.each(result.message.data,function(i,v){
					//用户信息
					users[this.author.prn] = this.author;
					
					//获取到单条的HTML
					if(this.level > 5){//层级大于5
						this.message = '{%parent_info}' + this.message;
					}
					var outHtml = syk.fmt(outSingle(this), {none:""});
					//判断是否父类，如果有父类往下叠加,实现子父类关系
					var li =  body.find("[data-post-prn='"+ this.parentPrn +"']");
					if(li.length>0){
						if(this.level > 5){
							var info = '<a class="ds-comment-context" parentPrns="'+ this.parentPrns  +'" data-user-prn="'+ this.authorPrn +'"  more=""data-post-prn="'+this.prn +'" data-parent-prn="'+ this.parentPrn +'">回复 '+ li.attr('nickname') +': </a>';
							var t = syk.fmt(outHtml, {parent_info:info});
							li.parent().children('li:last').after(t);
						}else{
							var children = li.children('ul.ds-children');
							if(children.length > 0){
								//已经有子评论了
								children.find('li').eq(0).before(outHtml);
							}else{
								//第一个子评论
								li.append('<ul class="ds-children">'+outHtml +'</ul>');
							}
						}
					}else{
						body.append(outHtml);
					}
				});
			}else{
				body.append('<li class="ds-post ds-post-placeholder">还没有评论，沙发等你来抢</li>');
			}
			//用户条数信息
			if(result && result.message && result.message.userTips){
				var userTips=result.message.userTips;
				for(var i in userTips){//把count数量放到用户信息中去
					users[i]['count'] =userTips[i];
				}
				$._message = {},$._message.tokens = users;
			};
		});
		
		//时间格式化
		setInterval(function() {
			$(".ds-time").each(function() {
				var e = $(this).attr("datetime");
				var x = S.elapsedTime(e);
				e && (this.innerHTML = S.elapsedTime(e));
			});
		},
		2e4);
	};
	//加载信息
	init_message();
	
	//用户tips信息显示="{%author_prn}"
	$("#ds-thread").on("mouseover",'[show-user]',function(event){
		if($('#ds-bubble').length){
			return;
		}
		var self = $(this);
		var parent= $("#ds-reset").offset(),pleft=parent.left,ptop=parent.top;//父DIV的偏移量
		var left = self.offset().left-pleft-15,top=self.offset().top-ptop-72;//计算出当前偏移量
		var uprn = self.attr('data-user-prn');//用户prn
		$("#ds-reset").append(syk.fmt(_tips.join(''), $._message.tokens[uprn]));//用户数据显示
		$('#ds-bubble').css({top:top ,left:left}).show(200);
	});
	//用户tips信息显示
	$("#ds-thread").on("mouseover",'[more]',function(){
		if($('#ds-bubble').length){
			return;
		}
		var self = $(this);
		var parent= $("#ds-reset").offset(),pleft=parent.left,ptop=parent.top;//父DIV的偏移量
		var left = self.offset().left-pleft-15,top=self.offset().top-ptop-97;//计算出当前偏移量
		var uprn = self.attr('data-user-prn');//用户prn
		var parentPrn =self.attr('data-parent-prn');
		var pt = $('li[data-post-prn="'+ parentPrn +'"] span.ds-time') ;
		var createdTimeZooe =$.trim(pt.attr('datetime'));
		var createdYMDHMSStr = pt.attr('title');
		var createdTimeFmt = $.trim(pt.text());
		var pmessage =$.trim($('li[data-post-prn="'+ parentPrn +'"] p').html());
		var parentPrns =  self.attr('parentPrns');//用来取到各个message，然后显示到列表
		var data = $.extend($._message.tokens[uprn],{createdTimeZooe:createdTimeZooe,createdYMDHMSStr:createdYMDHMSStr,createdTimeFmt:createdTimeFmt,parent_message:pmessage,message_prn:self.attr('data-post-prn'),parentPrns:parentPrns});
		$("#ds-reset").append(syk.fmt(_history.join(''), data ));//用户数据显示
		$('#ds-bubble').find('.ds-ctx-content>.ds-comment-context').remove().end().css({top:top ,left:left}).show(200);
	});
	//移动鼠标隐藏
	$("#ds-thread").mousemove(function(e) {
		e= e.target;
		if(!($('[data-user-prn]').is(e) || $("#ds-bubble").is(e) || $('[data-user-prn]').find(e).length || $('#ds-bubble').find(e).length)){
			var target = $("#ds-reset >#ds-bubble").hide(300).remove();
		}
	});
	//显示对话层【历史记录】
	$("#ds-thread").on('click','[more_message]',function(){
		
		var self =$(this);
		//获取父类ID串，用来拼接信息楼层关系
		var parentPrns = self.attr('parentPrns'),parentPrns = parentPrns.split(',');
		var xh = [];//拼接楼层message
		for(var i=1;parentPrns.length>i+1;i++){
			//取到用户ID，用来获取用户信息
			var uprn = $('[data-post-prn="'+ parentPrns[i] +'"] >div>div').attr('data-user-prn');
			//取到message
			var message = $('[data-post-prn="'+ parentPrns[i] +'"] >div>div>p').html();
			
			//每个的时间处理
			var pt = $('[data_prn="'+ parentPrns[i] +'"]>span.ds-time'),
			createdTimeZooe = pt.attr('datetime'),
			createdYMDHMSStr = pt.attr('title'),
			createdTimeFmt = $.trim(pt.text());
			var data = {message:message,no:i,createdTimeZooe:createdTimeZooe,createdYMDHMSStr:createdYMDHMSStr,createdTimeFmt:createdTimeFmt};
			//取到用户信息，合并信息
			//拼接单个message
			data = $.extend($._message.tokens[uprn],data);
			//html拼接信息
			xh.push(syk.fmt(historys.join(""), data));
		}
		//拼接大的弹框信息
		xh = syk.fmt(historys_body.join(''), {historys_body:xh.join("")});
		//输出到body
		$('body').append(xh);
		//删除 <a class="ds-comment-context" parentPrns=",3309,3310," data-user-prn="1" more="" data-post-prn="3310" data-parent-prn="3309">回复 在线工具: </a>
		$('#ds-ctx div.ds-ctx-content>a.ds-comment-context').remove();
		$('#ds-wrapper').show(300);//显示
		
		//上面的弹出框关闭
		$(document).on('click',function(e){
			e = e.target;
			if(!($('[more_message]').is(e) || $('#ds-wrapper').find(e).length >0)){
				$('#ds-wrapper').remove();
				$(document).unbind('click');
			}
		});
	});
	//上面的弹出框关闭
	$('body').on('click','a.ds-dialog-close',function(){
		$('#ds-wrapper').hide(300);
		setTimeout(function(){
			$('#ds-wrapper').remove();
		},300);//再删除
	});
	//删除按钮
	$("#ds-thread").on("mouseover mouseout",'.ds-account-control',function(event){
		var self = $(this);
		if(event.type == "mouseover"){
			self.addClass('ds-active');
		}else if(event.type == "mouseout"){
			self.removeClass('ds-active');
		}
	});
	//删除按钮
	$("#ds-thread").on("mouseover mouseout",'.ds-comment-actions',function(event){
		var self = $(this);
		var author_prn = self.attr('author_prn');
		if(author_prn == token.prn || token.prn == 1){
			if(event.type == "mouseover"){
			 	$(this).find('.ds-post-delete-a').removeClass('none');
			}else if(event.type == "mouseout"){
				$(this).find('.ds-post-delete-a').addClass('none');
			}
		}
	});
	
	//删除按钮
	$("#ds-thread").on('click',"a.ds-post-delete-a",function(){
		var self = $(this);
		layer.confirm('确认删除?',{
			btn: ['确认','取消']
			},function(){
				var p = self.parent('[data_prn]');
				var prn = p.attr('data_prn');
				var parentPrns=p.attr('parentPrns');
				var data = {level:p.attr('level'),parentPrns:parentPrns,prn:prn,pkey:pkey};
				$.post("/message/deleteMessage.shtml",data,function(result){
					
					syk.verify(data, function(){
						//获取当前数据
						var pli = p.parents('li[data-post-prn="'+ prn +'"]');
						pli.hide(500);//先动态隐藏
						var hides = [];//需要删除的项
						$.each($('[parentPrns^="'+ parentPrns+'"]'),function(){
							var s = $('li[data-post-prn="'+ $(this).attr('data-post-prn') +'"]').hide(500);//先隐藏。后删除
							hides.push(s);//添加数组，方便下面去定时删除
						});
						
						setTimeout(function(){
							var ul = pli.parent('ul');
							if(ul.children('li').length == 1){
								ul.remove();//如果只有一个li，直接删除ul
							}else{
								pli.remove();//多个li，直接删除当前li即可
							}
							//上面隐藏，下面删除
							$.each(hides,function(){
								$(this).remove();
							});
							//多少条评论
							var len = ~~$('span[len]').text()-hides.length;
							$('span[len]').text(len);
							if(len === 0){//如果是0条要显示提示信息
								$('#orderMenu').after('<ul class="ds-comments" id="body_message" style="display:none;"><li class="ds-post ds-post-placeholder">还没有评论，沙发等你来抢</li></ul>');
								$("#body_message").show(300);
							}
							hides=null;
						},600);//再删除
						return layer.msg('删除成功!'),!1;
					});
				},'json');
			}
		);
	});
	//最新
	$("#ds-thread").on('click',"a.ds-order-desc",function(){
		$(this).siblings('a.ds-current').removeClass('ds-current').end().addClass('ds-current');
		init_message('desc');
	});
	//最早
	$("#ds-thread").on('click',"a.ds-order-asc",function(){
		$(this).siblings('a.ds-current').removeClass('ds-current').end().addClass('ds-current');
		init_message('asc');
	});
	//最热
	$("#ds-thread").on('click',"a.ds-order-hot",function(){
		$(this).siblings('a.ds-current').removeClass('ds-current').end().addClass('ds-current');
		init_message('hot');
	});
	//单条消息点击回复
	$("#ds-thread").on('click',"a.ds-post-reply",function(){
		$("div[box='small']").remove();
		var xhtml = html.join('');
		var p =  $(this).parent("div[data_prn]");
		var parentPrn = p.attr('data_prn');
		var level=  1+~~p.attr('level');
		var parentPrns=  p.attr('parentPrns');
		var rootPrn= p.attr('rootPrn');
		debugger;
		$(this).parent().after(syk.fmt(xhtml, $.extend(token,{"parentPrn":parentPrn,location:"small",level:level,parentPrns:parentPrns,rootPrn:rootPrn})));
		$('[emote="small"]').qqFace({assign:'small'});
	});
	//顶
	$("#ds-thread").on('click',"a.ds-post-likes",function(){
		var self =$(this);
		var prn = self.parents('li[data-post-prn]').attr('data-post-prn');
		var load = layer.load();
		$.post('/message/like.shtml',{prn:prn,pkey:pkey},function(data){
			layer.close(load);
			syk.verify(data, function(){
				self.html(syk.fmt('<span class="ds-icon ds-icon-like"></span>顶({%likes})', data));
				//♥显示
				if(data.marker){
					self.parent('div.ds-comment-footer').addClass('ds-post-liked');
				}else{
					self.parent('div.ds-comment-footer').removeClass('ds-post-liked');
				}
				self.attr("like",data.marker);
			});
		},'json');
		
		
	});
	//提交
	$("#ds-thread").on('click',"[submit]",function(){
		var self = $(this);
		var form = self.parents('form');
		var message = form.find("[message]");
		if($.trim(message.val()) == ''){
			return layer.msg("请输入内容"),!1;
		}
		if(message.val().length >2000){
			return layer.msg("内容超过2000"),!1;
		}
		var parentPrnv =form[0].parentPrn.value;
		var pkeyv =pkey || form[0].pkey.value;//评论加载Key
		var levelv =form[0].level.value;//层级
		var parentPrnsv = form[0].parentPrns.value;//父prn串
		var rootPrnv = form[0].rootPrn.value;//根节点prn
		debugger;
		var messagev =form[0].message.value;//评论内容
		var args = {msgContent:messagev,parentPrns:parentPrnsv,rootPrn:rootPrnv,level:levelv,pkey:pkeyv,parentPrn:parentPrnv};//form.serialize()
		var load = layer.load();
		$.post(form.attr('url'),args,function(result){
			layer.close(load);
			syk.verify(result, function(){
				var data = result.obj;
				if(data.level > 5){//层级大于5，特殊处理
					data.message = '{%parent_info}' + data.message;
	 			}
				var h = syk.fmt(outSingle(data), {none:"style='display:none'"});;
				var box = self.attr('submit');
				if(box == 'main'){//主评论框
					$("#body_message").children('li').eq(0).before(h);
					$("#body_message").find('li.ds-post-placeholder').remove();
					message.val('');
				}else{
					var li =  $("#body_message").find("[data-post-prn='"+ data.parentPrn +"']");
					if(data.level > 5){
					var info = '<a class="ds-comment-context"  parentPrns="'+ data.parentPrns  +'" data-user-prn="'+ data.authorPrn +'"  more="" data-post-prn="'+data.prn +'" data-parent-prn="'+ data.parentPrn +'">回复 '+ li.attr('nickname') +': </a>';
	 					var t = syk.fmt(h, {parent_info:info});
	 					li.parent().children('li:last').after(t);
				 	}else{
						var children = li.children('ul.ds-children');
						if(children.length > 0){
							//已经有子评论了
							children.find('li').eq(0).before(h);
						}else{
							//第一个子评论
							li.append('<ul class="ds-children">'+h +'</ul>');
						}
				 	}
				 	$('div[box="small"]').remove();//删除评论框
				 }
				 //增加评论数
				 $('span[len]').text(1+~~( $('span[len]').text()));
				 $('li.ds-post').show(500);
			});
		},'json');
	});
	
};
loadMessage();