/**
 * 
 */

var dayCronTemplate = "${min} ${hour} * * *";
var weekCronTemplate = "${min} ${hour} * * ${week}";
var monthCronTemplate = "${min} ${hour} ${day} * *";
var yearCronTemplate = "${min} ${hour} ${day} ${month} *";
var repeatCronTemplate = {
		"1m" : "*/1 * * * *",
		"5m" : "*/5 * * * *",
		"10m" : "*/10 * * * *",
		"30m" : "*/30 * * * *",
		"1h" : "{min} */1 * * *",
		"3h" : "{min} */3 * * *",
		"6h" : "{min} */6 * * *",
		"12h" : "{min} */12 * * *"
}

var keyword = "";
var orderType = "LINK_NM";
var orderBy = "ASC";
var currPage = 1;
var viewSize = 10;
var pageSize = 5;

var logPage = 1;
var logView = 5;
var logPageSize = 5;
var logLinkSn = null;

var jqGridRow = 20;

jQuery(window).on("load", function() {
	
	jQuery(".linkName, .linkUseYn, .linkStatus IMG").addClass("cursor");
	
	selectListLinkData();
	
	jQuery('#linkSearchText').keyup(function (event) {
        var regexp =  /[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]/gi;
        var value = jQuery(this).val();
        if (regexp.test(value)) {
        	jQuery(this).val(value.replace(regexp, ''));
        }

    });
	
	jQuery("#linkSearchBtn").click(function() {
		keyword = jQuery("#linkSearchText").val();
		currPage = 1;
		selectListLinkData();
	});
	
	jQuery("#linkClearBtn").click(function() {
		jQuery("#linkSearchText").val("");
		keyword = "";
		currPage = 1;
		selectListLinkData();
	});
	
	setLinkChart();
});

function setLinkChart(){
	//연계데이터 차트생성
	ajax.call('manage/linkdata/linkDataCounts.do', 'GET', null, null, true, function(data) {
		var realTimeData = [{ cnt: 1, rslt_val: 'Y' }, { cnt: 1, rslt_val: 'Y' }, { cnt: 1, rslt_val: 'N' }];
		var agencyListData = [{ cnt: 5, cntc_org_nm: '기상청' }, { cnt: 2, cntc_org_nm: 'AFSO' }];
		var agencyDataData = [{ cntc_org_nm: '기상청', count: {sum: 54367}, tbl: 'TI_KMA_SFC_DY, TI_KMA_SFC_HR' }];
		
		jQuery.each(data.LIST, function(i, d) {
			if(i==0) {
				realTimeChart(d);
			} else if(i==1) {
				agencyListChart(d);
			} else if(i==2) {
				agencyDataChart(d);
			}
		});
		
	});
}

function setBeforeData(template, data) {
	var link_nm = data.link_nm;
	var link_org_nm = data.link_org_nm;
	var link_sn = data.link_sn;
	var link_time_text = data.link_time;
	var link_cron = data.link_cron;
	var link_use_yn = data.use_at;
	var link_url = data.link_url;
	var result_val = data.rslt_val;
	var result_date = data.rslt_dt;
	var cronData = JSON.parse(link_time_text);
	var cronType = cronData.cronType;
	var schemaName = data.link_table_schema_nm;
	var tableName = data.link_table_nm;
	
	jQuery("INPUT[name=link_name]", template).val(link_nm);
	jQuery("INPUT[name=link_org_name]", template).val(link_org_nm);
	jQuery("INPUT[name=link_url]", template).val(link_url);
	jQuery(".repeatRadio[cron-type="+cronType+"]", template).trigger("click");
	jQuery("INPUT[name=link_table_schema_nm]", template).val(schemaName);
	jQuery("INPUT[name=link_table_nm]", template).val(tableName);

	if(cronType == "repeat") {
		var val = cronData.value;
		var min = cronData.min;
		jQuery(".cycleSel.repeatSel", template).val(val);
		jQuery(".cycleSel.repeatSel", template).trigger("change");
		jQuery(".cycleSel.repeatMinSel", template).val(min);
	}else{
		var d = JSON.parse(data.link_time);
		
		var specType = d.specType;
		
		jQuery(".cycleSel.specificTypeSel", template).val(specType);
		jQuery(".cycleSel.specificTypeSel", template).trigger("change");
		
		var day = d.day;
		var hour = d.hour;
		var min = d.min;
		var month = d.month;
		var week = d.week;
		
		jQuery(".cycleSel.hourSel", template).val(hour);
		jQuery(".cycleSel.minSel", template).val(min);
		
		switch(specType) {
			case "day" :
				break;
			case "week" :
				jQuery(".subCycle.weekSel", template).val(week);
				break;
			case "month" :
				jQuery(".subCycle.monthSel", template).val(day);
				break;
			case "year" :
				jQuery(".cycleSel.monthDatepicker", template).val(month + "/" + day);
				break;
			default :
				
		}
	}
}

function linkRegistPopup(data) {
	var template = jQuery(".template.linkRegistPopup").clone();
	template.removeClass("template");
	
	var year = new Date().getFullYear()
	
	jQuery(".monthDatepicker", template).datepicker({
		dateFormat: "mm/dd",
		changeMonth : true,
//	    showOtherMonths: true,
	    showMonthAfterYear: true,
	    yearSuffix : "년 ",
	    yearRange: year+":"+year,
	    monthNames: [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12' ],
	    monthNamesShort : [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12' ],
	    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	    changeMonth: true,
		changeYear: true
	});
	
	jQuery(".subCycle.yearSel IMG", template).click(function() {
		var jThis = jQuery(this);
		jQuery("INPUT", jThis.parent()).focus();
	});
	
	jQuery(".cancelBtn", template).click(function() {
		jbSystemPopup.removePopup("LinkRegistPopup");
	});
	
	jQuery(".specificTypeSel", template).change(function() {
		var jThis = jQuery(this);
		var val = jThis.val();
		jQuery(".subCycle", template).hide();
		jQuery(".subCycle."+val+"Sel", template).show();
		clearInputValue(template);
	});
	
	//jQuery(".specificTypeSel").trigger('change');
	
	jQuery(".repeatSel.cycleSel", template).change(function() {
		var jThis = jQuery(this);
		var parent = jThis.parent();
		var val = jThis.val();
		var target = jQuery(".cycleSel.repeatMinSel", parent);
		target.val("0");
		if(val.indexOf("h") != -1) {
			console.log(val);
			target.show();
		}else{
			target.hide();
		}
	});
	
	jQuery(".repeatRadio", template).click(function() {
		var jThis = jQuery(this);
		var selId = jThis.attr("cron-type");
		var parent = jThis.parent();
		
		clearInputValue(template, true);
		
		jQuery(".repeatRadio", parent).removeClass("on");
		jThis.addClass("on");
		jQuery(".repeatRadio IMG", parent).attr('src', _contextPath + "resources/images/new/radio_off.png");
		jQuery(".selArea", parent).addClass("noDisplay");
		jQuery("IMG", jThis).attr('src', _contextPath + "resources/images/new/radio_on.png");
		jQuery(".selArea."+selId, parent).removeClass("noDisplay");
		
	});
	
	jQuery(".submitBtn", template).click(function() {
		var jThis = jQuery(this);
		var parent = jThis.parents(".linkRegistPopup");
		var name = jQuery("INPUT[name=link_name]", template).val();
		if(name == "") {
			alert("연계 명칭을 입력하시기 바랍니다.");
			return;
		}
		var org = jQuery("INPUT[name=link_org_name]", template).val();
		if(org == "") {
			alert("연계 기관을 입력하시기 바랍니다.");
			return;
		}
		var url = jQuery("INPUT[name=link_url]", template).val();
		if(url == "") {
			alert("연계 요청 URL을 입력하시기 바랍니다.");
			return;
		}

		var schemaName = jQuery("INPUT[name=link_table_schema_nm]", template).val();
		if(schemaName == "") {
			alert("데이터 조회를 위한 스키마명을 입력하시기 바랍니다.");
			return;
		}

		var tableName = jQuery("INPUT[name=link_table_nm]", template).val();
		if(tableName == "") {
			alert("데이터 조회를 위한 테이블명을 입력하시기 바랍니다.");
			return;
		}
		
		var cronType = jQuery(".repeatRadio.on", template).attr("cron-type");
		var cronText = {};
		cronText.cronType = cronType;
		
		var cron = null;
		if(cronType == "repeat") {
			var repeatCron = jQuery(".cycleSel.repeatSel", template).val();
			var repeatMin = jQuery(".cycleSel.repeatMinSel", template).val();
			cron = repeatCronTemplate[repeatCron];
			cron = cron.replace("{min}", repeatMin);
			cronText.value = repeatCron;
			cronText.min = repeatMin;
		}else{
			var specificType = jQuery(".cycleSel.specificTypeSel", template).val();
			var hourValue = jQuery(".cycleSel.hourSel", template).val();
			var minuteValue = jQuery(".cycleSel.minSel", template).val();
			
			var weekValue = jQuery(".subCycle.weekSel", template).val();
			var dayValue = jQuery(".subCycle.monthSel", template).val();
			var fullValue = jQuery(".cycleSel.monthDatepicker", template).val();
			var monthValue = null;
			if(fullValue != "") {
				var split = fullValue.split("/");
				dayValue = split[1];
				monthValue = split[0];
			}
			cronText.specType = specificType;
			cronText.hour = hourValue;
			cronText.min = minuteValue;
			cronText.week = weekValue;
			cronText.day = dayValue;
			cronText.month = monthValue;
			
			switch(specificType) {
				case "day" :
					cron = dayCronTemplate;
					break;
				case "week" :
					cron = weekCronTemplate;
					break;
				case "month" :
					cron = monthCronTemplate;
					break;
				case "year" :
					cron = yearCronTemplate;
					break;
				default :
					cron = null;
			}
			
			if(cron != null) {
				for(var key in cronText) {
					var val = cronText[key];
					if(!isNaN(val)) {
						val = parseInt(val);
					}
					cron = cron.replace("${"+key+"}", val);
				}
			}
		}
		
		cronText = JSON.stringify(cronText);
		
		var d = parent.data();
		
		if(d && d.link_sn) {
			updateLinkData(d.link_sn, name, org, url, cron, cronText, schemaName, tableName);
		}else{
			insertLinkData(name, org, url, cron, cronText, schemaName, tableName);
		}
	});
	
	var title = "연계기능 등록";
	
	if(data) {
		title = "연계기능 수정";
		jQuery(".submitBtn", template).html("수정");
		template.data(data);
		setBeforeData(template, data);
	}
	
	jbSystemPopup.createPopup("LinkRegistPopup", title, template, {
		position : JBPopup.CODE.POSITION.CENTER_MIDDLE,
		close : function() {
		}
	});
}

function clearInputValue(template, isRadioBtn) {
	if(isRadioBtn) {
		jQuery(".cycleSel.specificTypeSel").val("day");
		jQuery(".cycleSel.specificTypeSel").change();
		jQuery(".cycleSel.repeatSel").val("1m");
		jQuery(".cycleSel.repeatMinSel").val("0");
	}
	
	jQuery(".monthDatepicker", template).val("");
	jQuery(".subCycle.monthSel", template).val(1);
	jQuery(".subCycle.weekSel", template).val(1);
	jQuery(".cycleSel.hourSel", template).val("00");
	jQuery(".cycleSel.minSel", template).val("00");
}

function updateLinkData(id, name, org, url, cron, text, schemaName, tableName) {
	var param = {
			link_sn : id,
			name : name,
			org : org,
			url : url,
			cron : cron,
			text : text,
			schemaName : schemaName,
			tableName : tableName
	}
	
	ajax.call("manage/linkdata/updateLinkData.do", "POST", null, param, true, function(data) {
		selectListLinkData();
		
		setLinkChart();
		
		jbSystemPopup.removePopup("LinkRegistPopup");
	}, function(error) {
		
	});
}

function insertLinkData(name, org, url, cron, text, schemaName, tableName) {
	var param = {
			name : name,
			org : org,
			url : url,
			cron : cron,
			text : text,
			schemaName : schemaName,
			tableName : tableName,
			link_type : 'L'
	}
	ajax.call("manage/linkdata/insertLinkData.do", "POST", null, param, true, function(data) {
		selectListLinkData();
		jbSystemPopup.removePopup("LinkRegistPopup");
	}, function(error) {
		
	});
	
}

function selectListLinkData() {

	var template = jQuery(".linkListTemplate");
	var target = jQuery(".link_list_content TABLE");
	var noData = jQuery(".linkListNoData");
	
	jQuery(".linkData").remove();
	
	var param = {
			keyword : keyword,
			order_type : orderType,
			orderby : orderBy,
			page : currPage,
			limit : viewSize,
			link_type : 'L'
	}
	
	ajax.call("manage/linkdata/selectListLinkData.do", "POST", null, param, true, function(data) {
		var count = data.COUNT;
		var list = data.LIST;
		
		if(count && list) {
			var max = Math.ceil(count/viewSize);
			jQuery(".link_list_content .pageDiv").paging({
				current:currPage, 
				max:max,
				format : "{0}",
				length : pageSize,
				next : "<div class='paging-btn-img next'></div>",
				prev : "<div class='paging-btn-img prev'></div>",
				first : "<div class='paging-btn-img first'></div>",
				last : "<div class='paging-btn-img last'></div>",
				onclick:function(e, page) {
				if(currPage != page) {
					currPage = page;
					selectListLinkData();
				}
			}});
			
			//현재 페이지 / 전체 페이지 표시
			jQuery("#currPageInfo").html(", 페이지 수 : "+ currPage + '/' + max);
			
			noData.hide();
			for(var i=0,cnt=list.length; i<cnt; i++) {
				var d = list[i];
				var link_nm = d.link_nm;
				var link_org_nm = d.link_org_nm;
				var link_sn = d.link_sn;
				var link_time_text = d.link_time;
				var link_cron = d.link_cron;
				var link_use_yn = d.use_at;
				var link_url = d.link_url;
				var result_val = d.rslt_val;
				var result_date = d.rslt_dt;
				
				if(!result_date) {
					result_date = "-";
				}
				
				var temp = template.clone();
				
				if(i % 2 > 0) {
					temp.addClass('odd');
				}
				temp.removeClass("linkListTemplate").addClass("linkData");
				temp.data(d);
				jQuery(".linkOrg", temp).html(highlightText(link_org_nm, keyword));
				jQuery(".linkName", temp).html(highlightText(link_nm, keyword));
				jQuery(".linkLastDate", temp).append(result_date);
				
				jQuery(".linkLastDate .refresh", temp).click(function() {
					var jThis = jQuery(this);
					var parent = jThis.parents(".linkData");
					var dd = parent.data();
					var linkSn = dd.link_sn;
					/*jbSystemPopup.showMask();*/
					ajax.call("manage/linkdata/requestCustomProcess.do", "POST", null, {
						linkSn : linkSn
					}, true, function(data) {
						alert("수동 연계 처리를 완료하였습니다.");
						selectListLinkData();
						/*jbSystemPopup.hideMask();*/
					}, function(error) {
						alert("수동 연계 처리에 실패하였습니다.");
						/*jbSystemPopup.hideMask();*/
					});
				});
				
				jQuery(".linkStep", temp).html(getLinkTimeText(link_time_text));
				
				if(userRole == 'ADMIN'){
					jQuery(".linkName", temp).click(function() {
						var jThis = jQuery(this);
						var dd = jThis.parents(".linkData").data();
						
						linkRegistPopup(dd);
					});
				}
				
				var auto_img = "on";
				var auto_text = "수동연계";
				
				if(link_use_yn != "Y") {
					auto_img = "off";
					auto_text = "자동연계";
				}
				
				jQuery(".linkUseYn IMG", temp).attr("src", _contextPath + "resources/images/common/"+auto_img+".png").attr("alt", auto_text).attr("title", auto_text);
				
				if(userRole == 'ADMIN'){
					jQuery(".linkUseYn IMG", temp).click(function() {
						var jThis = jQuery(this);
						var dd = jThis.parents(".linkData").data();
						changeAutoLink(jThis, dd);
					});
				}
				
				jQuery(".dataSearchBtn", temp).click(function() {
					var jThis = jQuery(this);
					var dd = jThis.parents(".linkData").data();
				});
				
				var img = "ok";
				var imgText = "연계성공";
				if(!result_val) {
					img = "empty";
					imgText = "연계결과 없음";
				}else if(result_val == "Y") {
					img = "ok";
					imgText = "연계성공";
				}else{
					img = "error";
					imgText = "연계실패";
				}
				
				if(userRole == 'ADMIN'){
					jQuery(".linkStatus IMG", temp).click(function() {
						var jThis = jQuery(this);
						var dd = jThis.parents(".linkData").data();
						searchDataLogPopup(dd);
					});
				}
				
				jQuery(".linkStatus IMG", temp).attr("src", _contextPath + "resources/images/link/link_"+img+".png").attr("alt", imgText).attr("title", imgText);
				target.append(temp);
				temp.show();
			}
			
			// 전체 검색 결과
			jQuery("#tatalPageInfo").html("전체 : " + data.COUNT);
			
			jQuery("#rightPageInfo").html(data.DATA+'기준');
		}else{
			noData.show();
		}
	}, function(error) {
		
	});
}

function searchDataLogPopup(data) {
//	alert("Log : " + data.link_nm);
	//selectListLinkDataLog

	var template = jQuery(".template.linkLogPopup").clone();
	template.removeClass("template");
	template.data(data);
	logLinkSn = data.link_sn;
	logPage = 1;
	selectListLinkDataLog(template);
	
	jbSystemPopup.createPopup("LinkLogPopup", "[" + data.link_nm + "] 연계 결과", template, {
		position : JBPopup.CODE.POSITION.CENTER_MIDDLE,
		close : function() {
			logLinkSn = null;
		}
	});
}
function selectListLinkDataLog(template) {
	
	if(logLinkSn == null) {
		return;
	}
	
	var param = {
			link_sn : logLinkSn,
			page : logPage,
			limit : logView
	}

	var popup = jQuery(".JBPopup .linkLogPopup");
	var sync = true;
	if(template) {
		popup = template;
		sync = false;
	}
	
	var table = jQuery("TABLE", popup);
	var noData = jQuery(".noData", table);
	var paging = jQuery(".pageDiv", popup);
	
	ajax.call("manage/linkdata/selectListLinkDataLog.do", "POST", null, param, sync, function(data) {
		var count = data.COUNT;
		var list = data.LIST;
		
		jQuery(".linkLog:not(.template)").remove();
		
		if(count && list) {
			noData.hide();
			for(var i=0,cnt=list.length; i<cnt; i++) {
				var d = list[i];
				var temp = jQuery(".linkLog.template", popup).clone();
				var result_val = d.rslt_val;
				var msg = d.rslt_msg;
				var date = d.reg_dt;
				temp.removeClass("template");

				var img = "ok";
				var imgText = "연계성공";
				if(result_val == "Y") {
					img = "ok";
					imgText = "연계성공";
				}else{
					img = "error";
					imgText = "연계실패";
				}
				
				if(!msg) {
					msg = "-";
				}else{
					jQuery(".linkMessage DIV", temp).css({
						'text-align' : 'left'
					});
				}
				
				jQuery(".linkStatus IMG", temp).attr("src", _contextPath + "resources/images/link/link_"+img+".png").attr("alt", imgText);
				jQuery(".linkDate", temp).html(date);
				jQuery(".linkMessage DIV", temp).text(msg);
				jQuery(".linkMessage DIV", temp).attr('title', msg);
				jQuery(".linkMessage DIV", temp).bind("contextmenu",function(e){
					console.log("right click");
					copyToClipboard(e.currentTarget);
			    });
				table.append(temp);
			}

			var max = Math.ceil(count/logView);
			paging.paging({
				current:logPage, 
				max:max,
				length : logPageSize,
				format : "{0}",
				next : "<div class='paging-btn-img next'></div>",
				prev : "<div class='paging-btn-img prev'></div>",
				first : "<div class='paging-btn-img first'></div>",
				last : "<div class='paging-btn-img last'></div>",
				onclick:function(e, page) {
				if(logPage != page) {
					logPage = page;
					selectListLinkDataLog();
				}
			}});
		}else{
			noData.show();
		}
		
	}, function(error) {
		
	});
}

/*function searchDataPopup(data) {
	var param = {
			link_sn : data.link_sn
	}
	var linkName = data.link_nm;
	ajax.call("manage/linkdata/selectListLinkDataColumnInfo.do", "POST", null, param, true, function(data) {
		var list = data.LIST;
		if(list.length > 0) {
			var template = jQuery(".template.linkRowDataPopup").clone();
			template.removeClass("template");
			
			var width = jQuery("BODY").width() - 100;
			var height = jQuery("BODY").height() - 150;
			
			template.css({
				"width" : width + "px",
				"height" : height + "px"
			})
			
			var searchBtn = jQuery(".submitBtn", template);
			jQuery(".clearBtn", template).click(function() {
				var jThis = jQuery(this);
				jQuery(".searchColumn", jThis.parent()).val("");
				jQuery(".searchComparison", jThis.parent()).val("");
				jQuery(".searchKeyword", jThis.parent()).val("");
				
				searchBtn.trigger("click");
			});
			
			var tableId = "table_" + new Date().getTime();
			searchBtn.click(function() {
				var jThis = jQuery(this);
				var column = jQuery(".searchColumn", jThis.parent()).val();
				var comparison = jQuery(".searchComparison", jThis.parent()).val();
				var keyword = jQuery(".searchKeyword", jThis.parent()).val();
				var colData = jQuery(".searchColumn OPTION:selected", jThis.parent()).data();
				var data_type = colData.data_type;
				if(data_type) {
					data_type = data_type.toLowerCase();
				}
				
				var newParam = {
						link_sn : param.link_sn
				}

				if(keyword) {
					if(comparison == "like") {
						keyword = "%" + keyword + "%";
					}
				}
				
				if(column) {
					if(data_type.indexOf("date") != -1 || data_type.indexOf("time") != -1) {
						column = "TO_CHAR(" + column + ", 'YYYY-MM-DD HH24:MI:SS')";
					}
					if(comparison == "like") {
						column = column + "::character varying";
					}
				}

				if(keyword && column && comparison) {
					newParam.val = keyword;
					newParam.column = column;
					newParam.columnStyle = data_type;
					newParam.comparison = comparison;
				}

				var table = jQuery("#"+tableId);
				table.setGridParam({ postData: null });
				table.setGridParam({ postData: newParam });
				table.trigger("reloadGrid", [{ page: 1 }]);
			});
			
			jbSystemPopup.createPopup("LinkRowDataPopup", linkName + " 데이터 조회", template, {
				position : JBPopup.CODE.POSITION.CENTER_MIDDLE,
				close : function() {
					logLinkSn = null;
				}
			});
			
			var colNames = [];
//			var colModels = [];
			var colModels = [];
			var sortName = null;
			var table = jQuery(".viewDataArea TABLE", template);
			
			var searchColumn = jQuery(".searchColumn", template);
			
			for(var i=0,cnt=list.length; i<cnt; i++) {
				var d = list[i];
				var name = typeof d.description == "undefined" ? d.column_name : d.description;
				var option = jQuery("<option>");
				option.val(d.column_name);
				option.html(name);
				option.data(d);
				searchColumn.append(option);
//				colModels.push(d.column_name);
				colNames.push(name);
				var model = {
						name : d.column_name,
						index : d.column_name,
						sortable : true,
						align : 'center'
				}
				
				if(d.is_pk) {
					model.frozen = true;
					if(sortName == null) {
						sortName = d.column_name;
					}
				}
				
				if(d.data_type.indexOf("date") != -1) {
					model.formatter = function (cellvalue, options, rowObject) {
						var date = new Date(cellvalue);
						return date.format("{yyyy}-{MM}-{dd}");
					}
				}else if(d.data_type.indexOf("timestamp") != -1) {
					model.formatter = function (cellvalue, options, rowObject) {
						var date = new Date(cellvalue);
						return date.format("{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}");
					}
				}else if(d.data_type.indexOf("time") != -1) {
					model.formatter = function (cellvalue, options, rowObject) {
						var date = new Date(cellvalue);
						return date.format("{HH}:{mm}:{ss}");
					}
				}
				
				colModels.push(model);
			}
			var pageId = "page_" + new Date().getTime();
			table.attr('id', tableId);
			jQuery(".paging", template).attr("id", pageId);
			var checkHeight = table.parents(".viewDataArea").height() - 60;
			
			table.jqGrid({
				url : _contextPath + "manage/linkdata/selectListLinkRowData.do",
				mtype:"POST",
				datatype: "json",
				postData : param,
				height : checkHeight,
				autowidth : true,
				shrinkToFit: true,
				multiSort : true,
				colNames : colNames,
				colModel : colModels,
				sortname : sortName,
				pager : "#" + pageId,
				page:1,
				rowNum: jqGridRow,
				loadComplete : function() {
					
				},
				loadBeforeSend : function(jqXHR) {
					var _csrf = jQuery("META[name='_csrf']").attr("content");
					var _csrf_header = jQuery("META[name='_csrf_header']").attr("content");
					if(!_csrf.isBlank() && !_csrf_header.isBlank()) {
						jqXHR.setRequestHeader(_csrf_header, _csrf);
//						headers[_csrf_header] = _csrf;
					}
				},
				jsonReader: {
					total:function(data) {
						return Math.ceil(data.COUNT/jqGridRow);
					},
					records: "COUNT",
					root:  "LIST",
				}
			});
			
//			var checkHeight = table.parents(".viewDataArea").height() - 60;
//			
//			jQuery(".ui-jqgrid-bdiv", table.parents(".viewDataArea")).css({
//				height : checkHeight + "px"
//			});
			
//			table.jqGrid("setGridHeight", checkHeight).trigger("resize");
//			table.jqGrid("setFrozenColumns");
		}else{
			alert(linkName + "의 스키마/테이블 명이 잘못되었습니다.");
		}
	}, function(error) {
		alert(linkName + "의 스키마/테이블 명이 잘못되었습니다.");
	});
}*/

function changeAutoLink(target, data) {
	var param = {
			link_sn : data.link_sn,
			use_at : data.use_at == "Y" ? "N" : "Y"
	}
	ajax.call("manage/linkdata/updateAutoLinkAT.do", "POST", null, param, true, function(data) {
		selectListLinkData();
	}, function(error) {
		
	});
}

function getLinkTimeText(link_time_text) {
	var output = "";
	var data = JSON.parse(link_time_text);
	var cronType = data.cronType;
	
	if(cronType == "repeat") {
		var val = data.value;
		var min = data.min;
		
		if(val.indexOf("m") != -1 && val.indexOf("h") != -1) {
			output = "연계주기 없음";
		}else{
			output = val.replace("m", "분").replace("h", "시간") + " 주기";
			if(val.indexOf("h") != -1) {
				output += "로 " + min + "분에 실행";
			}
		}
	}else{
		var specType = data.specType;
		var day = data.day;
		var hour = data.hour;
		var min = data.min;
		var month = data.month;
		var week = data.week;
		
		switch(specType) {
			case "day" :
				output = "매일 "+hour+"시 "+min+"분";
				break;
			case "week" :
				output = "매주 ";
				switch(week) {
					case "1" :
						output += "월요일 ";
						break;
					case "2" :
						output += "화요일 ";
						break;
					case "3" :
						output += "수요일 ";
						break;
					case "4" :
						output += "목요일 ";
						break;
					case "5" :
						output += "금요일 ";
						break;
					case "6" :
						output += "토요일 ";
						break;
					case "7" :
						output += "일요일 ";
						break;
					default :
				}
				output += hour+"시 "+min+"분";
				break;
			case "month" :
				output = "매월 "+day+"일 "+hour+"시 "+min+"분";
				break;
			case "year" :
				output = "매년 "+month+"월 "+day+"일 "+hour+"시 "+min+"분";
				break;
			default :
				
		}
	}
	
	return output;
}

function highlightText(text, keyword) {
	return text.split(keyword).join("<span style='color:red;font-weight:bold;'>" + keyword + "</span>");
}