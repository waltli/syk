$(function(){
	$('img').lazyload({
		effect:'fadeIn'
	});
});

function strFmt(sources, fmtData){
	for(var i in fmtData){
		var keyRegx = new RegExp("\{%"+i+"\}", "g"); 
		var value = fmtData[i];
		sources = sources.replace(keyRegx, value);
	}
	return sources;
}