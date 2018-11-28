//评论框
var html = [];
html.push('<div class="ds-replybox ds-inline-replybox" box="{%location}" style="{%style}">');
html.push('    <a class="ds-avatar" href="{%link}" target="_blank" title="{%nickname}">');
html.push('        <img src="{%avatarUri}" alt="{%nickname}">');
html.push('    </a>');
html.push('    <form method="post" message-post="" onsubmit="return false;" url="'+ctx+'/msg/push" >');
html.push('        <input name="parentPrn" value="{%parentPrn}" type="hidden">');
html.push('        <input name="pkey" value="{%pkey}" type="hidden">');
html.push('        <input name="msgLevel" value="{%msgLevel}" type="hidden">');
html.push('        <input name="prnLine" value="{%prnLine}" type="hidden">');
html.push('        <input name="rootPrn" value="{%rootPrn}" type="hidden">');
html.push('        <div class="ds-textarea-wrapper ds-rounded-top">');
html.push('            <textarea message="" id="{%location}" name="msgContent" _title="Ctrl+Enter快捷提交" placeholder="说点什么吧…"></textarea>');
html.push('            <pre class="ds-hidden-text"></pre>');
html.push('        </div>');
html.push('        <div class="ds-post-toolbar">');
html.push('            <div class="ds-post-options ds-gradient-bg">');
html.push('            </div>');
html.push('            <button class="ds-post-button" submit="{%location}">发布</button>');
html.push('            <div class="ds-toolbar-buttons">');
html.push('                <a class="ds-toolbar-button ds-add-emote" emote="{%location}" title="插入表情"></a>');
html.push('                <div class="ds-cmt-login-wrapper" open="{%location}" style="{%display}">');
html.push('                    <span class="ds-cmt-login-tip">使用社交账号登录</span>');
html.push('                    <a href="javascript:;" title="使用QQ账号登录" class="ds-cmt-icon-qq"></a>');
//html.push('                    <a href="javascript:;" title="使用微博账号登录" class="xei-cmt-icon-weibo"></a>');
html.push('                </div>');
html.push('            </div>');
html.push('        </div>');
html.push('    </form>');
html.push('</div>');
//单个评论输出模块
var xhtml = [];
xhtml.push('<li class="ds-post "  {%none} data-post-prn="{%prn}" nickname="{%author_nickname}" >');
xhtml.push('	<div class="ds-post-self">');
xhtml.push('		<div class="ds-avatar" data-user-prn="{%author_prn}">');
xhtml.push('			<a target="_blank" href="{%author_uri}" title="{%author_nickname}" show-user="" data-user-prn="{%author_prn}" data-prn="{%prn}" prnLine="{%prnLine}">');
xhtml.push('				<img src="{%author_avatarUri}" alt="{%author_nickname}">');
xhtml.push('			</a>');
xhtml.push('		</div>');
xhtml.push('		<div class="ds-comment-body">');
xhtml.push('			<div class="ds-comment-header">');
xhtml.push('				<a class="ds-user-name ds-highlight" show-user="" href="{%author_uri}" rel="nofollow" target="_blank" data-user-prn="{%author_prn}" data-prn="{%prn}" prnLine="{%prnLine}">{%author_nickname}</a>');
xhtml.push('			</div>');
xhtml.push('			<p class="contentArea">{%msgContent}</p>');
xhtml.push('			<div class="ds-comment-footer ds-comment-actions  {%likes_class}" msgLevel="{%msgLevel}" prnLine="{%prnLine}" data_prn="{%prn}" author_prn="{%author_prn}" rootPrn="{%rootPrn}" >');
xhtml.push('				<span class="ds-time" datetime="{%createTime}" title="{%createTimeFullStr}">{%createTimeCalc}</span>');
xhtml.push('				<a class="ds-post-reply" href="javascript:void(0);"><span class="ds-icon ds-icon-reply"></span>回复</a>');
xhtml.push('				<a class="ds-post-unreply" style="display:none;" href="javascript:void(0);"><span class="ds-icon ds-icon-reply"></span>取消</a>');
xhtml.push('				<a class="ds-post-likes" href="javascript:void(0);"><span class="ds-icon ds-icon-like"></span>顶({%likeCount})</a>');
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
_tips.push('  	<a href="{%uri}"  class="ds-avatar" target="_blank">');
_tips.push('  		<img src="{%avatarUri}" alt="{%nickname}">');
_tips.push('  	</a>');
_tips.push('  	<a href="{%uri}" class="ds-user-name ds-highlight" target="_blank">{%nickname}</a>');
_tips.push('  	<p class="ds-user-card-meta">');
_tips.push('  		<a href="{%uri}" target="_blank">');
_tips.push('  			<span class="ds-highlight">{%count}</span>条评论');
_tips.push('  		</a>');
_tips.push('		<a href="javascript:void(0);" more_message="显示多层对话" data-user-prn="{%prn}" data-prn="{%msgPrn}" prnLine="{%prnLine}">查看对话</a>');
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
_history.push('					<a rel="nofollow author" target="_blank" href="{%uri}" title="{%nickname}">');
_history.push('						<img src="{%avatarUri}" alt="{%nickname}">');
_history.push('					</a>');
_history.push('				</div>');
_history.push('				<div class="ds-ctx-body">');
_history.push('					<div class="ds-ctx-head">');
_history.push('						<a rel="nofollow author" target="_blank"  href="{%uri}">{%nickname}</a>');
_history.push('						<a href="javascript:void(0);"  rel="nofollow" class="ds-time"  datetime="{%createTime}"  title="{%createTimeFullStr}">{%createTimeCalc}</a>');
_history.push('					</div>');
_history.push('					<div class="ds-ctx-content">{%parent_message}</div>');
_history.push('				</div>');
_history.push('			</li>');
_history.push('		</ul>');
_history.push('		<div class="ds-bubble-footer">');
_history.push('			<a class="ds-ctx-open" href="javascript:void(0);" more_message="显示多层对话" data-user-prn="{%prn}" data-prn="{%msg_prn}" prnLine="{%prnLine}">查看对话</a>');
_history.push('		</div>');
_history.push('	 </div>');
_history.push('	 <div class="ds-arrow ds-arrow-down ds-arrow-border"></div>');
_history.push('	 <div class="ds-arrow ds-arrow-down"></div>');
_history.push('</div>');

//多层记录查看[显示多层对话]
var historys = [],historys_body=[];
historys.push('<li class="ds-ctx-entry" data-post-prn="{%prn}">');
historys.push('	<div class="ds-avatar">');
historys.push('		<a rel="nofollow author" target="_blank" href="{%uri}" title="{%nickname}">');
historys.push('			<img src="{%avatarUri}" alt="{%nickname}"/>');
historys.push('		</a>');
historys.push('	</div>');
historys.push('	<div class="ds-ctx-body">');
historys.push('		<div class="ds-ctx-head">');
historys.push('			<a rel="nofollow author" target="_blank" href="{%uri}">{%nickname}</a>');
historys.push('			<a href="javascript:void(0);" target="_blank"  class="ds-time"  datetime="{%createTime}"  title="{%createTimeFullStr}">{%createTimeCalc}</a>');
historys.push('			<div class="ds-ctx-nth" >{%no}楼</div>');
historys.push('		</div>');
historys.push('		<div class="ds-ctx-content">{%msgContent}</div>');
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
box.push('		<div class="ds-toolbar" style="display: none;">');
box.push('			<div class="ds-account" style="display: none;">');
box.push('				<div class="ds-account-control">');
box.push('					<span class="ds-icon ds-icon-settings"></span>'); 
box.push('					<span>个人中心</span>');
box.push('					<ul>');
//box.push('						<li><a target="_blank"rel="nofollow" href="http://www.sojson.com/admin.shtml">个人资料</a></li>');
box.push('						<li><a href="javascript:void(0);" ds-logout style="border-bottom: none">退出登录</a></li>');
box.push('					</ul>');
box.push('				</div>');
box.push('				<div class="ds-account-name">');
box.push('					<a token_nickname="" href="javascript:void(0);">nickname</a>');
box.push('				</div>');
box.push('			</div>');
box.push('			<div class="ds-visitor">');
box.push('				<a class="ds-visitor-name"  href="javascript:void(0);">请登录</a>');
box.push('			</div>');
box.push('		</div>');
//<#--统计，排序-->
box.push('		<div class="ds-comments-info" id="ds-order-menu" style="display: none;">');
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
box.push('		<ul class="ds-comments" id="ds-body-message">');
box.push('			<li class="ds-post ds-post-placeholder">正在加载评论... ... </li>');
box.push('		</ul>');
box.push('	</div>');
box.push('</div>');

syk.insert.css(ctxassets+'/message/css/embed.default.css',{id:"message-css"});
//syk.insert.js(ctxassets+'/message/js/emote.js');
(function($){
	//初始化表情
	var emotes = ['微笑','撇嘴','色','发呆','大哭','害羞','闭嘴','睡','流泪','尴尬','发怒','调皮','呲牙','惊讶','难过','冷汗',
		'抓狂','吐','偷笑','可爱','白眼','傲慢','饥饿','困','惊恐','流汗','憨笑','大兵','奋斗','咒骂','疑问','嘘...','晕',
		'折磨','衰','敲打','再见','擦汗','抠鼻','糗大了','坏笑','左哼哼','右哼哼','哈欠','鄙视','委屈','快哭了','阴险','亲亲',
		'吓','可怜','拥抱','月亮','太阳','炸弹','骷髅','菜刀','猪头','西瓜','咖啡','饭','爱心','强','弱','握手','胜利','抱拳',
		'勾引','OK','NO','玫瑰','凋谢','示爱','爱情','飞吻'];
	
	var Defaults = {
		id : 'facebox', 
		assign:'main', 
		path:ctxassets+'/message/img/emote',
		tip : '/'
	};
	
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
	};
	$.fn.setCaret=function(){
		var msie = /msie/.test(navigator.userAgent.toLowerCase());
		if(!msie) return; 
		var initSetCaret = function(){ 
			var textObj = $(this).get(0); 
			textObj.caretPos = document.selection.createRange().duplicate(); 
		}; 
		$(this).click(initSetCaret).select(initSetCaret).keyup(initSetCaret); 
	};

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
//			textObj.blur(); 
		}else{ 
			textObj.value+=textFeildValue; 
		} 
	};
	$.fn.qqFace = function(options){
		Defaults = $.extend(Defaults, options);
		var $trigger = $(this);
		$trigger.attr("assign",Defaults.assign);
		var id = Defaults.id;
		var path = Defaults.path;
		var tip = Defaults.tip;
		
		if($('#'+Defaults.assign).length<=0){
			console.log('缺少表情赋值对象。');
			return false;
		}
		
		$trigger.click(function(e){
			var $self = $(this);
			$(".qqFace").remove();
			var assign = $self.attr("assign");
			var strFace, labFace;
			strFace = '<div id="'+id+'" style="position:absolute;display:none;z-index:1000;" class="qqFace">' +
						  '<table border="0" cellspacing="0" cellpadding="0"><tr>';
			
				for(var i=0; i<emotes.length; i++){
					var name = emotes[i];
					labFace = '['+tip+name+']';
					var src = 
					strFace += '<td><img title="'+name+'" src="'+path+"/"+name+'.gif" emote="'+ labFace+'" assign="'+assign+'" /></td>';
					if( (i+1) % 15 == 0 ) strFace += '</tr><tr>';
				}
			
			strFace += '</tr></table></div>';
			$('body').append(strFace);
			//位置
			$('#'+id).css({top:$self.offset().top + 22,left:$self.offset().left-5}).show(200);
			$("#"+assign).focus();
			e.stopPropagation();
		});
		
		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};
	$.faceShow = function(str){
		var patt = /\[\/(.*?)\]/g;
		var r;
		while(r = patt.exec(str)) {
			if(emotes.indexOf(r[1]) > -1){
				str = str.replace(r[0],'<img src="'+Defaults.path+"/"+r[1]+'.gif" border="0" />');
			}
		}
		return str;
	};
	
	$('body').on('click','[emote]',function(){
		var labFace = $(this).attr('emote');
		var assign = $(this).attr('assign');
		$("#"+assign).setCaret();
		$("#"+assign).insertAtCaret(labFace);
	});
})(jQuery);

//main方法
var loadMessage = function(){
	//加载css
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
	var pkey = $('#ds-thread').attr("pkey");//评论数据Key
	if(pkey == null || pkey == ""){
		pkey = location.href;
	}
	
	//评论输出box
	var body = $("#ds-body-message");
	//单个评论输出
	var outSingle = function(o){
		
		var single = xhtml.join("");
		var author = o.author;
		var likes_class = "";//选择的class元素
		o.msgContent = $.faceShow(o.msgContent);
		if(o.liked){//如果已赞。
			likes_class ="ds-post-liked";//点亮
		}
		
		//构造单条信息的需要的参数
		return syk.fmt(single,$.extend(o,{
			author_prn:author.prn,
			author_uri:author.uri,
			author_nickname:author.nickname,
			author_avatarUri:author.avatarUri,
			likes_class:likes_class,
			}))
	};
	var init_message = function(orderMarker){
		//判断有没有排名标示
		var args =  orderMarker?  {pkey:pkey,orderMarker:orderMarker}:{pkey:pkey};
		//清空信息框
		body.empty();
		//根据key获取评论信息
		$.getJSON(ctx+"/msg/gets", args,function(result){
			var data = result.obj;
			token = data.token;//赋值token
			$(".ds-toolbar").show(200);
			var fmtData = {};
			if(token && !$.isEmptyObject(token)){
				$(".ds-visitor").hide();
				$(".ds-account").show();
				$("[token_nickname]").text(token.nickname).attr('href',token.link);
				fmtData = $.extend(token,{parentPrn:0,location:'ds-main-replybox',msgLevel:1,prnLine:'',rootPrn:'',pkey:pkey,display:'display:none'});
			}else{
				fmtData = token = {parentPrn:0,location:'ds-main-replybox',msgLevel:1,prnLine:'',rootPrn:'',nickname:"请登录",avatarUri:ctxassets+"/message/img/default_avatar_50.gif",prn:"-1",uri:"",link:"",pkey:pkey,display:''};
			}
			//初次加载把评论框加上
			if($('[box="ds-main-replybox"]').length === 0){
				$("#ds-order-menu").before(syk.fmt(html.join(''), fmtData).replace('ds-inline-replybox',''));
			}
			//加载emote
			$('[emote="ds-main-replybox"]').qqFace({assign:'ds-main-replybox'});
			$('#ds-order-menu').show(100);
			//多少条评论
			$('span[len]').text(data.messages.length);
			//没有评论
			if(!data.messages.length ||data.messages.length == 0){
				return body.html('<li class="ds-post ds-post-placeholder">还没有评论，沙发等你来抢</li>'),!1;
			}
			
			//热评
			if(data && data.hotMessages && data.hotMessages.length){
				var outArray = [];
				$.each(data.hotMessages,function(){
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
			if(data && data.messages && data.messages.length){
				$.each(data.messages,function(i,v){
					//用户信息
					users[this.author.prn] = this.author;
					
					//获取到单条的HTML
					if(this.msgLevel > 5){//层级大于5
						this.msgContent = '{%parent_info}' + this.msgContent;
					}
					var outHtml = syk.fmt(outSingle(this), {none:""});
					//判断是否父类，如果有父类往下叠加,实现子父类关系
					var li =  body.find("[data-post-prn='"+ this.parentPrn +"']");
					if(li.length>0){
						if(this.msgLevel > 5){
							var info = '<a class="ds-comment-context" prnLine="'+ this.prnLine  +'" data-user-prn="'+ this.authorPrn +'" more="" data-post-prn="'+this.prn +'" data-parent-prn="'+ this.parentPrn +'">回复 '+ li.attr('nickname') +': </a>';
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
			if(data && data.userTips){
				var userTips=data.userTips;
				for(var i in userTips){//把count数量放到用户信息中去
					users[i]['count'] =userTips[i];
				}
				$._message = {},$._message.tokens = users;
			};
		});
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
		var msgPrn = self.attr('data-prn');
		var prnLine = self.attr('prnLine');
		var data = $.extend($._message.tokens[uprn], {msgPrn:msgPrn, prnLine:prnLine});
		$("#ds-reset").append(syk.fmt(_tips.join(''), data));//用户数据显示
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
		var createTime =$.trim(pt.attr('datetime'));
		var createTimeFullStr = pt.attr('title');
		var createTimeCalc = $.trim(pt.text());
		var pmessage =$.trim($('li[data-post-prn="'+ parentPrn +'"] p').html());
		var prnLine =  self.attr('prnLine');//用来取到各个message，然后显示到列表
		var data = $.extend($._message.tokens[uprn],{createTime:createTime,createTimeFullStr:createTimeFullStr,createTimeCalc:createTimeCalc,parent_message:pmessage,msg_prn:self.attr('data-prn'),prnLine:prnLine});
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
		var prnLine = self.attr('prnLine'),prnLine = prnLine.split(',');
		var xh = [];//拼接楼层message
		for(var i=1;prnLine.length>i+1;i++){
			//取到用户ID，用来获取用户信息
			var uprn = $('#ds-body-message [data-post-prn="'+ prnLine[i] +'"] >div>div').attr('data-user-prn');
			//取到message
			var message = $('[data-post-prn="'+ prnLine[i] +'"] >div>div>p').html();
			//每个的时间处理
			var pt = $('#ds-body-message [data_prn="'+ prnLine[i] +'"]>span.ds-time'),
			createTime = pt.attr('datetime'),
			createTimeFullStr = pt.attr('title'),
			createTimeCalc = $.trim(pt.text());
			var data = {msgContent:message,no:i,createTime:createTime,createTimeFullStr:createTimeFullStr,createTimeCalc:createTimeCalc};
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
		//删除 <a class="ds-comment-context" prnLine=",3309,3310," data-user-prn="1" more="" data-post-prn="3310" data-parent-prn="3309">回复 在线工具: </a>
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
				var msgPrn = p.attr('data_prn');
				var prnLine=p.attr('prnLine');
				var params = {msgPrn:msgPrn};
				$.post(ctx+"/msg/delete",params,function(result){
					syk.verify(result, function(){
						//获取当前数据
						var pli = p.parents('li[data-post-prn="'+ msgPrn +'"]');
						pli.hide(500);//先动态隐藏
						var hides = [];//需要删除的项
						$.each($('.ds-comment-footer[prnLine^="'+ prnLine+'"]'),function(){
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
								$('#ds-order-menu').after('<ul class="ds-comments" id="ds-body-message" style="display:none;"><li class="ds-post ds-post-placeholder">还没有评论，沙发等你来抢</li></ul>');
								$("#ds-body-message").show(300);
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
		init_message('late');
	});
	//最早
	$("#ds-thread").on('click',"a.ds-order-asc",function(){
		$(this).siblings('a.ds-current').removeClass('ds-current').end().addClass('ds-current');
		init_message('early');
	});
	//最热
	$("#ds-thread").on('click',"a.ds-order-hot",function(){
		$(this).siblings('a.ds-current').removeClass('ds-current').end().addClass('ds-current');
		init_message('hot');
	});
	//单条消息点击回复
	$("#ds-thread").on('click',"a.ds-post-reply",function(){
		$("div[box='ds-small-replybox']").hide(200, function(){
			$("div[box='ds-small-replybox']:hidden").remove();
		});
		$("a.ds-post-unreply").hide();
		$("a.ds-post-reply").show();
		$(this).hide();
		$(this).next(".ds-post-unreply").show();
		var xhtml = html.join('');
		var p =  $(this).parent("div[data_prn]");
		var parentPrn = p.attr('data_prn');
		var msgLevel=1+~~p.attr('msgLevel');
		var prnLine=p.attr('prnLine');
		var rootPrn= p.attr('rootPrn');
		var data = $.extend(token,{style:"display:none;","parentPrn":parentPrn,location:"ds-small-replybox",msgLevel:msgLevel,prnLine:prnLine,rootPrn:rootPrn, pkey:pkey, display:"display:none"});
		var $xhtml = $(syk.fmt(xhtml, data));
		$(this).parent().after($xhtml);
		$xhtml.show(200);
		$('[emote="ds-small-replybox"]').qqFace({assign:'ds-small-replybox'});
	});
	//取消单条消息回复
	$("#ds-thread").on('click',"a.ds-post-unreply",function(){
		$("div[box='ds-small-replybox']").hide(200, function(){
			$("div[box='ds-small-replybox']:hidden").remove();
		});
		$(this).hide();
		$(this).prev(".ds-post-reply").show();
	});
	//顶
	$("#ds-thread").on('click',"a.ds-post-likes",function(){
		var self =$(this);
		var msgPrn = self.parents('li[data-post-prn]').attr('data-post-prn');
		var load = layer.load();
		$.post(ctx+'/msg/like',{msgPrn:msgPrn},function(result){
			layer.close(load);
			syk.verify(result, function(){
				var data = result.obj;
				self.html(syk.fmt('<span class="ds-icon ds-icon-like"></span>顶({%likeCount})', data));
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
		var pkeyv = pkey || form[0].pkey.value;//评论加载Key
		var msgLevelv =form[0].msgLevel.value;//层级
		var prnLinev = form[0].prnLine.value;//父prn串
		var rootPrnv = form[0].rootPrn.value;//根节点prn
		var msgContentv =form[0].msgContent.value;//评论内容
		var args = {msgContent:msgContentv,prnLine:prnLinev,rootPrn:rootPrnv,msgLevel:msgLevelv,pkey:pkeyv,parentPrn:parentPrnv};//form.serialize()
		var load = layer.load();
		$.post(form.attr('url'),args,function(result){
			layer.close(load);
			syk.verify(result, function(){
				var data = result.obj;
				if(data.msgLevel > 5){//层级大于5，特殊处理
					data.msgContent = '{%parent_info}' + data.msgContent;
	 			}
				var h = syk.fmt(outSingle(data), {none:"style='display:none'"});;
				var box = self.attr('submit');
				if(box == 'ds-main-replybox'){//主评论框
					$("#ds-body-message").children('li').eq(0).before(h);
					$("#ds-body-message").find('li.ds-post-placeholder').remove();
					message.val('');
				}else{
					var li =  $("#ds-body-message").find("[data-post-prn='"+ data.parentPrn +"']");
					if(data.msgLevel > 5){
					var info = '<a class="ds-comment-context"  prnLine="'+ data.prnLine  +'" data-user-prn="'+ data.authorPrn +'"  more="" data-post-prn="'+data.prn +'" data-parent-prn="'+ data.parentPrn +'">回复 '+ li.attr('nickname') +': </a>';
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
				 	$('div[box="ds-small-replybox"]').remove();//删除评论框
				 }
				 //增加评论数
				 $('span[len]').text(1+~~( $('span[len]').text()));
				 $('li.ds-post').show(500);
			});
		},'json');
	});
	
	
	$("#ds-thread").on('click',".ds-cmt-icon-qq",function(){
		syk.user.openLogin(1, function(user){
			$("#ds-thread .ds-visitor").hide();
			$("#ds-thread .ds-account").show();
			$("#ds-thread [token_nickname]").text(user.nickname).attr('href',user.link);
			$("#ds-thread .ds-replybox > a.ds-avatar").attr("title", user.nickname)
				.find("img").attr("src", user.avatarUri).attr("alt",user.nickname);
			$("#ds-thread [open=ds-main-replybox]").hide();
			token = user;
		});
	});
	
	$("#ds-thread").on("click", '[ds-logout]', function(event){
		syk.user.logout(token, function(){
			$("#ds-thread .ds-account").hide();
			$("#ds-thread .ds-visitor").show();
			$("#ds-thread [token_nickname]").text('nickname').attr('href','');
			$("#ds-thread .ds-replybox > a.ds-avatar").attr("title", "请登录")
				.find("img").attr("src", ctxassets+"/message/img/default_avatar_50.gif").attr("alt","请登录");
			$("#ds-thread [open=ds-main-replybox]").show();
		});
	});
	
};
loadMessage();
