//ui-search define
$.fn.UiSearch = function(){
	// body...
	var ui = $(this);
	$('.ui-search-select',ui).on('click',function () {
		$('.ui-search-select-list').show();
		return false;
		// body...
	});
	$('.ui-search-select-list a',ui).on('click',function () {
		$('.ui-search-select',ui).text($(this).text());
		$('.ui-search-select-list',ui).hide();
		return false;
		// body...
	});
	$('body').on('click',function () {
		$('.ui-search-select-list').hide();
		// body...
	});
	$('.ui-search-select-list').hide();

};



$.fn.UiBackTop = function () {
	// body...
	var ui = $(this);
	var el = $('<a herf="0>up</a>');
	ui.append(el);

	var windowHeight = $(window).height();
	$(window).on('sccroll',function () {
		// body...
		var top = $('body').sccrollTop();
		if(top>= windowHeight){
			$(el).show();
		}
		else{
			$(el).hide();
		}
	})
}



//logic
$(function () {
	// body...
	$('.ui-search').UiSearch();
	$('body').UiBackTop();
})