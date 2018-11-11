(function($){
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