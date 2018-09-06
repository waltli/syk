<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>jquery validate验证</title>
    <%@ include file="/pages/common/header.jsp" %>
    <link rel="stylesheet" href="${ctx }/res/css/form.css" />
    <script type="text/javascript">
    	$(function(){
    		
    		/* $.extend($.validator.messages, {
    			required: "你麻痹"
    		}); */
    		
    		
    		//让当前表单调用validate方法，实现表单验证功能
    		$("#ff").validate({
    			debug:true,	//调试模式，即使验证成功也不会跳转到目标页面
//    			regex:true, //是否开启正则表达式匹配name，默认为false（自己添加的，修改代码在jquery.validate.js 1217行, 801行）
//    			onfocusout: function(element) {   //失去焦点进行验证的正确开启方式，默认为false关闭，onkeyup同理。
//					$(element).valid(); 
//				},
    			ignore:".ignore",
    			errorPlacement: function(error, element) { //错误信息位置设置方法
    				if(element.is(":hidden")){
	    				error.appendTo($(element.data("error-view")).parent()); //这里的element是录入数据的对象
    				}else {
    					error.appendTo(element.parent());
    				}
    			},
    			rules:{		//配置验证规则，key就是被验证的dom对象，value就是调用验证的方法(也是json格式)
    				"users\\[\\d\\]\\.sname":{
    					required:true,	//必填。如果验证方法不需要参数，则配置为true
    					rangelength:[4,12],
    				},
    				"users\\[\\d\\]\\.spass":{
    					required:true,
    					rangelength:[6,16]
    				}
    			},
    			messages:{
    				"users\\[\\d\\]\\.sname":{
    					required:"请输入用户名",
    					rangelength:$.validator.format("用户名长度为{0}-{1}个字符"),
    					remote:"该用户名已存在！"
    				},
    				"users\\[\\d\\]\\.spass":{
    					required:"请输入密码",
    					rangelength:$.validator.format("密码长度为{0}-{1}个字符")
    				}
    			}
    		});
    	}); 
    </script>
  </head>
  
  <body>
  	<button id="asdf">asdf</button>
  	<form action="http://www.hao123.com" method="post" id="ff">
	    <fieldset>
	        <legend>jQuery-Validate表单校验验证</legend>
	        <div class="item">
	            <label for="username" class="item-label">用户名:</label>
	            <input type="text" id="username" name="users[0].sname" class="item-text" placeholder="设置用户名"
	            autocomplete="off" tip="请输入用户名">
	        </div>
	        <div class="item">
	            <label for="password" class="item-label">密码:</label>
	            <input type="password" id="password" name="users[0].spass" class="item-text" 
	            placeholder="设置密码" tip="长度为6-16个字符">
	        </div>
	        <!-- <div class="item">
	            <label for="password" class="item-label">确认密码:</label>
	            <input type="password" name="users[0].spass2" class="item-text" placeholder="设置确认密码">
	        </div>
	        <div class="item">
	            <label for="phone" class="item-label">手机号码:</label>
	            <input type="text" id="phone" name="users[0].sphone" class="item-text" placeholder="输入手机号码" tip="请输入手机号码">
	        </div> -->
	        
	        
	        
	        <!-- <div class="item">
	            <label for="username" class="item-label">用户名:</label>
	            <input type="text" name="users[1].sname" class="item-text" placeholder="设置用户名"
	            autocomplete="off" tip="请输入用户名">
	        </div>
	        <div class="item">
	            <label for="password" class="item-label">密码:</label>
	            <input type="password" name="users[1].spass" class="item-text" 
	            placeholder="设置密码" tip="长度为6-16个字符">
	        </div> -->
	        <!-- <div class="item">
	            <label for="password" class="item-label">确认密码:</label>
	            <input type="password" name="users[1].spass2" class="item-text" placeholder="设置确认密码">
	        </div>
	        <div class="item">
	            <label for="phone" class="item-label">手机号码:</label>
	            <input type="text" id="phone" name="users[1].sphone" class="item-text" placeholder="输入手机号码" tip="请输入手机号码">
	        </div> -->
	        
	        <!-- <div class="item">
	            <label for="saddress" class="item-label">所在地:</label>
	            <select name="saddress" class="item-select">
	    			<option value="">---请选择---</option>
	    			<option value="北京">北京</option>
	    			<option value="上海">上海</option>
	    			<option value="深圳">深圳</option>
	    		</select>
	        </div>
    		<div class="item">
	            <label for="slike" class="item-label">爱好：</label>
	                                   上网：<input type="checkbox" name="slike" value="上网"/>
    			唱歌：<input type="checkbox" name="slike" value="唱歌"/>
    			编程：<input type="checkbox" name="slike" value="编程"/>
    			书法：<input type="checkbox" name="slike" value="书法"/><br/>
	        </div>
	        <div class="item">
	            <label for="semail" class="item-label">邮箱:</label>
	            <input type="text" id="semail" name="semail" class="item-text" placeholder="设置邮箱" 
	            autocomplete="off" tip="请输入邮箱">
	        </div> -->
	        <!-- <div class="item">
	            <label for="simage" class="item-label">头像:</label>
	            <input type="button" id="simage" name="simage" class="item-file" value="wefwefe">
	            <input type="hidden" name="simage21" data-error-view="#simage" tip="你麻痹">
	        </div> -->
	        <div class="item">
	            fwqefw:<input type="submit" value="提交" class="item-submit">
	        </div>
	    </fieldset>
	</form>
  </body>
  <script type="text/javascript">
  
  		$("#asdf").click(function(){
  			$("#ff").append($('<input name="at" id="fe">'));
  			$("#fe").rules("add",{
  				required:true, 
  				messages:{required:"请选择省份"}
  			});  
  		});
  	  /* $.ajax({
  		  url:ctx+"test",
  		  data:{"name":1,"pass":2},
  		  dataType:"text",
  	  	  success : function(data,e,r,j){
  	  		  debugger;
  	  	  },
  	  	  error:function(data,a,b,c){
  	  		  debugger;
  	  	  }
  	  }); */
  </script>
</html>
