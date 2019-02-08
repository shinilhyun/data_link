var MOVE_URL = {
	MEMBER_MANAGE : _contextPath + "manage/member/memberList.do",		//운영자 관리
	LINKDATA_MANAGE : _contextPath + "link/linkdataManage.do",
	ANALYSIS_MANAGE : _contextPath + "link/analysisManage.do",
	KMA_SFC_DAY : _contextPath + "manage/data/kma/kmaDayList.do",
	KMA_SFC_MONTH : _contextPath + "manage/data/kma/kmaMonthList.do",
	GDD_AWS_HR : _contextPath + "manage/data/gdd/gddHrList.do",
	RES_DAY : _contextPath + "manage/data/res/resList.do",
	WEATHER_ANALYSIS : _contextPath + "manage/statistics/weatherAnalysis.do",
	RESERVOIR_ANALYSIS : _contextPath + "manage/statistics/reservoirAnalysis.do",
}

jQuery("SECTION NAV .menuGroupItem[menu_id="+currNavMenuGroup+"]").addClass("on");
jQuery("SECTION NAV .menuItemList[menu_id="+currNavMenuGroup+"]").addClass("on");
jQuery("SECTION NAV .menuItem[menu_id="+currNavMenuItem+"]").addClass("on").attr("tabindex", "-1");

jQuery(window).on("load", function() {
	jQuery("SECTION NAV .menuGroupItem").click(function() {
		var jThis = jQuery(this);
		changeMenuGroupItem(jThis);
	});
	
	jQuery("SECTION NAV .menuItem").click(function() {
		var jThis = jQuery(this);
		changeMenuItem(jThis);
	});
	
	jQuery("BODY").keyup(function(e) {
		var jThis = jQuery(e.target);
		if(e.keyCode == 9) 	{			//Tab Button
			if(jThis.hasClass("menuGroupItem")) {
				changeMenuGroupItem(jThis);
			}
		}else if(e.keyCode == 13) { 	//Enter Button
			if(jThis.hasClass("menuItem")) {
				changeMenuItem(jThis);
			}
		}
	});
	
	jQuery("#manualArea").click(function() {
		var target = jQuery('<A>');
		target.attr("download", "");
		target.attr("href", _contextPath + 'manage/sample/downloadManageManual.do');
		jQuery("BODY").append(target);
		target[0].click();
		target.remove();
	});
	
	jbSystemPopup = new JBPopup({
		isUseMask : true,
		zIndex : 10000,	  // isUseMask == true 일 경우 zIndex 바로 밑에 Mask 가 생김 
		loadImgSrc : _contextPath + 'resources/js/jbt/component/popup/images/loading.gif',
		prefixId : 'JBT_SYSTEM', // Atribute Prefix
		popupDiv : jQuery('BODY'),
		isAutoResize : true
	});
	
	jbWindowPopup = new JBWindowPopup();
});

function changeMenuGroupItem(jThis) {
	var menu_id = jThis.attr("menu_id");
	if(currNavMenuGroup == menu_id) return;
	jQuery("SECTION NAV .menuItemList").slideUp();
	jQuery("SECTION NAV .menuGroupItem .menuStatusIcon")
		.attr('src', _contextPath + "resources/images/new/layerpopup_open.png")
		.attr('alt', "메뉴열기")
		.attr('title', "메뉴열기");
	jQuery(".menuStatusIcon", jThis)
		.attr('src', _contextPath + "resources/images/new/layerpopup_close.png")
		.attr('alt', "메뉴닫기")
		.attr('title', "메뉴닫기");
	jQuery("SECTION NAV .menuItemList[menu_id=" + menu_id + "]").slideDown();
	currNavMenuGroup = menu_id;
}

function changeMenuItem(jThis) {
	var menu_id = jThis.attr("menu_id");
	var url = MOVE_URL[menu_id];
	get_goto(url);
}/**
 * 
 */