<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="paginator-heavy" class="paginator"></div>
<script type="text/javascript">
laypage({
	cont: 'paginator-heavy',
	pages: '${pagingResult.totalPage}',
	curr: '${pagingResult.currentPage}',
	skin: '#E3B81F',
	first: 1, //将首页显示为数字1,。若不显示，设置false即可
	last: false, //将尾页显示为总页数。若不显示，设置false即可
	skip: true,
	prev: '<', 
	next: '>',
	jump: function(e, first){
		if(!first){
			var url = '${ctx}/movie/list?page='+e.curr;
			if(availableParams){
				url += ('&'+availableParams);
			}
			location.href = encodeURI(url);
		}
	}
});
</script>