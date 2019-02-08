<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<link rel="shortcut icon" href="<c:url value='/resources/images/new/favicon.ico'/>">
<link href="<c:url value='/resources/js/lib/jquery/jqGrid/css/ui.jqgrid.css'/>" rel="stylesheet" type="text/css" >
<link href="<c:url value='/resources/js/lib/jquery/jquery-ui/jquery-ui-1.12.1.css'/>" rel="stylesheet" type="text/css" >
<link href="<c:url value='/resources/js/lib/jquery/jquery.datetimepicker.min.css'/>" rel="stylesheet" type="text/css" >
<link href="<c:url value='/resources/js/lib/amcharts/plugins/export/export.css'/>" rel="stylesheet" type="text/css" >
<link href="<c:url value='/resources/css/common.css'/>" rel="stylesheet" type="text/css" >
<script type="text/javascript">
	var _contextPath = '<c:url value="/"/>';
</script>
<script type="text/javascript" src="<c:url value="/resources/js/jbt/config.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jquery-3.2.1.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jquery-ui/jquery-ui-1.12.1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jquery.paging.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jquery.datetimepicker.full.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jqGrid/src/i18n/grid.locale-kr.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/jqGrid/js/jquery.jqGrid.min.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/sugar/sugar.min.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jquery/noConflict.js"/>"  charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jbt/component/Common.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jbt/component/Ajax.js" />"  charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jbt/component/popup/JBPopup.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jbt/component/popup/JBWindowPopup.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/amcharts/amcharts.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/amcharts/pie.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/amcharts/serial.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/amcharts/themes/light.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/amcharts/plugins/export/export.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/amcharts/lang/ko.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/amcharts/plugins/export/lang/ko.js'/>" charset="utf-8"></script>

<script type="text/javascript">
	jQuery(window).on("load", function() {
		jQuery(".systemName").html(JBCode.SYSTEM_NAME);
	});
</script>