/**
 * 
 */

function realTimeChart(data) {
	// 실시간 연계현황
	var statusData = data;
	var realTimeChart = jQuery('#realTimeChart');
	for(var i=0; i<statusData.length; i++) {
		var cate = "성공";
		var labelColor = "#676767";
		var color = "#EFEFEF";
		if(statusData[i].rslt_val == "N") {
			cate = "실패";
			labelColor = "#FFFFFF";
			color = "#FD9369";
		}
		statusData[i].category = cate;
		statusData[i].labelColor = labelColor;
		statusData[i].color = color;
	}
//	if(realTimeChart) {
//		realTimeChart.clear();
//		realTimeChart = null;
//	}
	realTimeChart = AmCharts.makeChart("realTimeChart",
			{
				"type": "pie",
				"theme": "light",
				"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]건</b></span>",
				"labelRadius": -35,
				"fontSize": 17,
				"labelFunction": function(item) {
					return Math.round(item.percents) + "%";
				},
				"startDuration":0,
				"labelColorField": "labelColor",
				"autoMargins": false,
				"marginTop": 0,
				"marginBottom": 0,
				"marginLeft": 0,
				"marginRight": 0,
				"pullOutRadius": 0,
				"titleField": "category",
				"valueField": "cnt",
				"colorField": "color",
				"legend": {
					"enabled": true,
					"align": "center",
					"markerType": "square",
					"valueText": "",
					"fontSize":11,
				},
				"balloon": {
					"fontSize":11
				},
				"dataProvider": statusData
			}
		);
} 

function agencyListChart(data) {
	var agencyListChart = jQuery('#agencyListChart');
	var chartData = data;
	// 연계항목건수
//	if(agencyListChart) {
//		agencyListChart.clear();
//		agencyListChart = null;
//	}
	agencyListChart = AmCharts.makeChart("agencyListChart",
		{
			"type": "pie",
			"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]건</b></span>",
			"labelRadius": -35,
			"fontSize": 13,
			"labelFunction": function(item) {
				return "";
			},
			"startDuration":0,
			"labelColorField": "labelColor",
			"autoMargins": false,
			"marginTop": 0,
			"marginBottom": 0,
			"marginLeft": 0,
			"marginRight": 0,
			"pullOutRadius": 0,
			"titleField": "link_org_nm",
			"valueField": "cnt",
			"colorField": "color",
			"legend": {
				"enabled": true,
				"divId":"contactCountChartLegend",
				"align": "center",
				"markerType": "square",
				"valueText": "",
				"fontSize":11,
			},
			"balloon": {
				"fontSize":11
			},
			"titles": [],
			"dataProvider": chartData
				}
		);
}

function agencyDataChart(data) {
	var agencyDataChart = jQuery('#agencyDataChart');
	var chartData = data;
//	jQuery.each(data, function(i, v) {
//		var chartObj = {};
//		chartObj.category = v.link_org_nm;
//		chartObj.count = v.count.sum;
//		chartData.push(chartObj);
//	});
//	if(agencyDataChart) {
//		agencyDataChart.clear();
//		agencyDataChart = null;
//	}
	agencyDataChart = AmCharts.makeChart("agencyDataChart",
			{
				"type": "serial",
				"theme": "light",
				"categoryField": "link_org_nm",
				"startDuration": 0,
				"categoryAxis": {
					"gridPosition": "start",
					"labelRotation": 20
				},
				"chartCursor": {
					"enabled": true
				},
				"trendLines": [],
				"graphs": [{
                	 "type": "line",
                	 "title":"전체",
                	 "lineColor":"#000000",
                	 "lineAlpha": 0,
                	 "valueField": "cnt",
                	 "bulletAlpha": 0,
                	 "showBalloon": false,
                	 "visibleInLegend": false,
                	 "labelText" : "[[value]]"
                 }, {
						"balloonText": "[[category]]:[[value]]",
						"fillAlphas": 1,
						"type": "column",
						"valueField": "cnt"
//						"lineColor": "#FD9369"
					}
				],
				"guides": [],
				"valueAxes": [
					{
						"id": "ValueAxis-1",
						"title": ""
					}
				],
				"allLabels": [],
				"balloon": {},
				"dataProvider": chartData
			}
		);
}