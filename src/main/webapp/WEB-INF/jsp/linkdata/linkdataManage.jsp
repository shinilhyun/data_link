<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/piece/Common.jsp" %>
<link href="<c:url value="/resources/js/lib/jquery/jquery-ui/jquery-ui-1.12.1.css"/>" rel="stylesheet" type="text/css" >
<link href="<c:url value="/resources/css/common.css"/>" rel="stylesheet" type="text/css" >
<link href="<c:url value="/resources/css/manage/manage.css"/>" rel="stylesheet" type="text/css" >

<script type="text/javascript">
	var currNavMenuGroup = "LINKDATA";
	var currNavMenuItem = "LINKDATA_MANAGE";
</script>

<title>농정데이터 분석시스템 - 연계시스템</title>
</head>
<body>
	<div id="ajaxLoading" style="display:none; position: absolute;top: 0px;left: 0px;bottom: 0px;right: 0px;margin: 0px;padding: 0px;background: rgba(0, 0, 0, 0.3);z-index: 1000;">
		<iframe style="position:absolute; top:0px; left:0px; width:100%; height:100%; z-index:0;" frameborder="0"></iframe>
		<div style="width: 100%;height: 100%;text-align: center;">
			<span style="display: inline-block;vertical-align: middle;height: 100%;"></span>
			<img alt="loading" src="<c:url value="/resources/images/common/loading.gif"/>" style="vertical-align: middle;">
		</div>
	</div>
	<header>
		<c:import url="../manageHeader.jsp"></c:import>
	</header>
	<section class="mainContent">
		<nav>
			<c:import url="../nav.jsp"></c:import>
		</nav>
		<script type="text/javascript" src="<c:url value="/resources/js/navi.js"/>"></script>
		<div class="container">
			<div style="width: 1500px; height: 800px;">
				<section class="linkChartArea" style="padding-left: 10px; padding-top: 10px;">
					<div style="height: 401px; margin-bottom: 10px;">
						<div class="subContent realTime">
							<div class="mainTitle" style="padding-top: 26px;padding-left: 22px;">실시간 연계현황</div>
							<div style="border-bottom: 1px solid #d1d5d7; margin: 16px 22px;"></div>							
							<div class="linkContent">
								<div id="realTimeChart" style="height: 100%;"></div>
							</div>
						</div>
						<div class="subContent agencyList">
							<div class="mainTitle" style="padding-top: 26px;padding-left: 22px;">기관별 연계 항목 건수</div>
							<div style="border-bottom: 1px solid #d1d5d7; margin: 16px 22px;"></div>	
							<div class="linkContent">
								<div id="agencyListChart" style="height: 100%;"></div>
							</div>
						</div>
					</div>
					<div class="subContent agencyData">
						<div class="mainTitle" style="padding-top: 26px;padding-left: 22px;">기관별 연계 데이터 건수</div>
						<div style="border-bottom: 1px solid #d1d5d7; margin: 16px 22px;"></div>	
						<div class="linkContent">
							<div id="agencyDataChart" style="height: 100%;"></div>
						</div>
					</div>
				</section>
				<section class="linkDataArea">
					<div class="subContent info" style="height:764px;">
						<div class="mainTitle" style="padding-top: 26px;padding-left: 22px;">연계 정보 항목</div>
						<div style="border-bottom: 1px solid #d1d5d7; margin: 16px 22px;"></div>
						<div style="padding: 0 22px;">
							<div class="subTitle searchBox" style="line-height: 58px;">
								<div style="padding-left: 20px;">
									<input type="text" id="linkSearchText" style="height: 28px; vertical-align: middle; width:372px; border:1px solid #e8e8e8;" maxlength="50">
									<button type="button" id="linkSearchBtn" class="submitBtn btn">검색</button>
									<button type="button" id="linkClearBtn" class="clearBtn btn">초기화</button>
								</div>
							</div>
							<div style="height: 20px;margin-bottom: 10px;color: #5c5c5c;">
						    	<div id="leftPageInfo" style="float: left;">
						    		<div id="tatalPageInfo" style="display: inline-block;"></div>
						    		<div id="currPageInfo" style="display: inline-block;"></div>
						    	</div>
						    	<div id="rightPageInfo" style="float: right;"></div>
					    	</div>
							<div class="link_list_content">
								<table class="listTable listVerticalTable">
									<colgroup>
										<col width="20%"><col width="30%"><col width="15%"><col width="15%"><col width="10%"><col width="10%">
									</colgroup>
									<thead>
										<tr>
											<th class="last" style="width:200px;">연계기관</th>
											<th class="last" style="width:calc(100% - 890px); min-width : 150px;">연계데이터</th>
											<th class="last" style="width:190px;">최근 갱신일시</th>
											<th class="last" style="width:200px;">연계주기</th>
											<th class="last" style="width:100px;">연계상태</th>
											<th class="last" style="width:100px;">자동연계</th>
										</tr>
									</thead>
									<tbody>
										<tr class="linkListTemplate" style="display:none;">
											<td class="linkOrg"></td>
											<td class="linkName"></td>
											<td class="linkLastDate" style="position: relative;">
												<div style="width : 100%; height : 0px; position: absolute; top: 0px; right :0px;">
													<img class="refresh" title="수동 연계" alt="refresh" src="<c:url value="/resources/images/common/refresh.png"/>" style="position: absolute; top: 4px; right: 2px; z-index:1; cursor: pointer;" >
												</div>
											</td>
											<td class="linkStep"></td>
											<td class="linkStatus">
												<img alt="연계실패" src="<c:url value="/resources/images/link/link_error.png"/>"/>
											</td>
											<td class="linkUseYn">
												<img alt="자동연계" src="<c:url value="/resources/images/new/radio_on.png"/>" />
											</td>
										</tr>
										<tr class="linkListNoData">
											<td colspan="7" class="last">
												※ 검색결과가 존재하지 않습니다.
											</td>
										</tr>
									</tbody>
								</table>
								<div class="pageDiv">
								</div>
							</div>
						</div>
						<div class="template linkLogPopup">
							<table class="listTable listVerticalTable">
								<thead>
									<tr>
										<th class="last" style="width : 100px;">연계결과</th>
										<th class="last" style="width : 150px;">등록일시</th>
										<th class="last" style="width : 850px;">메시지</th>
									</tr>
								</thead>
								<tbody>
									<tr class="noData">
										<td class="last" colspan="3">
											※ 로그가 존재하지 않습니다.
										</td>
									</tr>
									<tr class="linkLog template">
										<td class="linkStatus">
											<img alt="연계실패" src="<c:url value="/resources/images/link/link_error.png"/>"  style="cursor: pointer;" />
										</td>
										<td class="linkDate"></td>
										<td class="linkMessage last">
											<div style="width:850px; text-overflow:ellipsis; white-space:nowrap; word-wrap:normal; overflow:hidden;font-size: 13px; max-height:54px;">
											
											</div>
										</td>
									</tr>
								</tbody>
							</table>
							<div class="pageDiv">
							
							</div>
						</div>
						
						<div class="template linkRegistPopup">
							<table class="viewTable listTable listLeftTable">
								<tbody>
									<tr>
										<th>
											연계 명칭</th>
										<td style="width:400px;">
											<input type="text" name="link_name">
										</td>
									</tr>
									<tr>
										<th>
											연계 기관</th>
										<td style="width:400px;">
											<input type="text" name="link_org_name">
										</td>
									</tr>
									<tr>
										<th>
											연계 요청 URL</th>
										<td style="width:400px;">
											<input type="text" name="link_url">
										</td>
									</tr>
									<tr>
										<th>
											데이터 조회 스키마 명</th>
										<td style="width:400px;">
											<input type="text" name="link_table_schema_nm">
										</td>
									</tr>
									<tr>
										<th>
											데이터 조회 테이블 명</th>
										<td style="width:400px;">
											<input type="text" name="link_table_nm">
										</td>
									</tr>
									<tr>
										<th>
											연계 주기</th>
										<td style="width:400px;">
											<span class="repeatRadio on" cron-type="repeat" style="cursor:pointer;">
												<img style="margin-left:10px; margin-bottom: -6px;" class="radioBtn" alt="반복 시간" src="<c:url value="/resources/images/new/radio_on.png"/> ">
												반복 시간
											</span>
											<span class="repeatRadio" cron-type="spec"  style="cursor:pointer;">
												<img style="margin-left:10px; margin-bottom: -6px;" class="radioBtn" alt="반복 시간" src="<c:url value="/resources/images/new/radio_off.png"/> ">
												특정 시간
											</span>
											<div style="margin-top:15px;">
												<div class="repeat selArea" style="width: calc(100% - 20px);">
													<select class="cycleSel repeatSel selectIcon" style="width:177px;">
														<option value="1m">매 1분</option>
														<option value="5m">매 5분</option>
														<option value="10m">매 10분</option>
														<option value="30m">매 30분</option>
														<option value="1h">매 1시간</option>
														<option value="3h">매 3시간</option>
														<option value="6h">매 6시간</option>
														<option value="12h">매 12시간</option>
													</select>
													<select class="cycleSel repeatMinSel selectIcon" style="width:176px; display: none;">
														<option value="0">0분</option>
														<option value="1">1분</option>
														<option value="2">2분</option>
														<option value="3">3분</option>
														<option value="4">4분</option>
														<option value="5">5분</option>
														<option value="6">6분</option>
														<option value="7">7분</option>
														<option value="8">8분</option>
														<option value="9">9분</option>
														<option value="10">10분</option>
														<option value="11">11분</option>
														<option value="12">12분</option>
														<option value="13">13분</option>
														<option value="14">14분</option>
														<option value="15">15분</option>
														<option value="16">16분</option>
														<option value="17">17분</option>
														<option value="18">18분</option>
														<option value="19">19분</option>
														<option value="20">20분</option>
														<option value="21">21분</option>
														<option value="22">22분</option>
														<option value="23">23분</option>
														<option value="24">24분</option>
														<option value="25">25분</option>
														<option value="26">26분</option>
														<option value="27">27분</option>
														<option value="28">28분</option>
														<option value="29">29분</option>
														<option value="30">30분</option>
														<option value="31">31분</option>
														<option value="32">32분</option>
														<option value="33">33분</option>
														<option value="34">34분</option>
														<option value="35">35분</option>
														<option value="36">36분</option>
														<option value="37">37분</option>
														<option value="38">38분</option>
														<option value="39">39분</option>
														<option value="40">40분</option>
														<option value="41">41분</option>
														<option value="42">42분</option>
														<option value="43">43분</option>
														<option value="44">44분</option>
														<option value="45">45분</option>
														<option value="46">46분</option>
														<option value="47">47분</option>
														<option value="48">48분</option>
														<option value="49">49분</option>
														<option value="50">50분</option>
														<option value="51">51분</option>
														<option value="52">52분</option>
														<option value="53">53분</option>
														<option value="54">54분</option>
														<option value="55">55분</option>
														<option value="56">56분</option>
														<option value="57">57분</option>
														<option value="58">58분</option>
														<option value="59">59분</option>
													</select>
												</div>
												<div class="noDisplay selArea spec">
													<div class="specificCycleDiv" style="width:65px; margin-right: 15px; float:left;">
														<select class="cycleSel specificTypeSel selectSmallIcon" style="width:68px;">
															<option value="day">매일</option>
															<option value="week">매주</option>
															<option value="month">매월</option>
															<option value="year">매년</option>
														</select>
													</div>
													<div class="specificCycleDiv" style="width:100px; height: 1px; margin-right: 10px; float:left;">
														<select class="cycleSel subCycle weekSel selectIcon" style="width: 100%; display: none;">
															<option value="1">월요일</option>
															<option value="2">화요일</option>
															<option value="3">수요일</option>
															<option value="4">목요일</option>
															<option value="5">금요일</option>
															<option value="6">토요일</option>
															<option value="7">일요일</option>
														</select>
														<select class="cycleSel subCycle monthSel selectIcon" style="width: 100%; display: none;">
															<option value="1">1일</option>
															<option value="2">2일</option>
															<option value="3">3일</option>
															<option value="4">4일</option>
															<option value="5">5일</option>
															<option value="6">6일</option>
															<option value="7">7일</option>
															<option value="8">8일</option>
															<option value="9">9일</option>
															<option value="10">10일</option>
															<option value="11">11일</option>
															<option value="12">12일</option>
															<option value="13">13일</option>
															<option value="14">14일</option>
															<option value="15">15일</option>
															<option value="16">16일</option>
															<option value="17">17일</option>
															<option value="18">18일</option>
															<option value="19">19일</option>
															<option value="20">20일</option>
															<option value="21">21일</option>
															<option value="22">22일</option>
															<option value="23">23일</option>
															<option value="24">24일</option>
															<option value="25">25일</option>
															<option value="26">26일</option>
															<option value="27">27일</option>
															<option value="28">28일</option>
															<option value="29">29일</option>
															<option value="30">30일</option>
															<option value="31">31일</option>
														</select>
														<span class="subCycle yearSel" style="width: 100%; display: none;">
															<input type="text" class="cycleSel monthDatepicker" style="width: 100%; height:28px;" readonly="readonly">
														</span>
													</div>
													<div class="specificCycleDiv" style="width:170px; float:left;    text-align: right;">
														<span style="font-weight: bold;">시간</span>
														<select class="cycleSel hourSel selectSmallIcon" style="width:60px;">
															<option value="00">00</option>
															<option value="01">01</option>
															<option value="02">02</option>
															<option value="03">03</option>
															<option value="04">04</option>
															<option value="05">05</option>
															<option value="06">06</option>
															<option value="07">07</option>
															<option value="08">08</option>
															<option value="09">09</option>
															<option value="10">10</option>
															<option value="11">11</option>
															<option value="12">12</option>
															<option value="13">13</option>
															<option value="14">14</option>
															<option value="15">15</option>
															<option value="16">16</option>
															<option value="17">17</option>
															<option value="18">18</option>
															<option value="19">19</option>
															<option value="20">20</option>
															<option value="21">21</option>
															<option value="22">22</option>
															<option value="23">23</option>
														</select>
														<span style="font-weight: bold;">:</span>
														<select class="cycleSel minSel selectSmallIcon" style="width:60px;">
															<option value="00">00</option>
															<option value="01">01</option>
															<option value="02">02</option>
															<option value="03">03</option>
															<option value="04">04</option>
															<option value="05">05</option>
															<option value="06">06</option>
															<option value="07">07</option>
															<option value="08">08</option>
															<option value="09">09</option>
															<option value="10">10</option>
															<option value="11">11</option>
															<option value="12">12</option>
															<option value="13">13</option>
															<option value="14">14</option>
															<option value="15">15</option>
															<option value="16">16</option>
															<option value="17">17</option>
															<option value="18">18</option>
															<option value="19">19</option>
															<option value="20">20</option>
															<option value="21">21</option>
															<option value="22">22</option>
															<option value="23">23</option>
															<option value="24">24</option>
															<option value="25">25</option>
															<option value="26">26</option>
															<option value="27">27</option>
															<option value="28">28</option>
															<option value="29">29</option>
															<option value="30">30</option>
															<option value="31">31</option>
															<option value="32">32</option>
															<option value="33">33</option>
															<option value="34">34</option>
															<option value="35">35</option>
															<option value="36">36</option>
															<option value="37">37</option>
															<option value="38">38</option>
															<option value="39">39</option>
															<option value="40">40</option>
															<option value="41">41</option>
															<option value="42">42</option>
															<option value="43">43</option>
															<option value="44">44</option>
															<option value="45">45</option>
															<option value="46">46</option>
															<option value="47">47</option>
															<option value="48">48</option>
															<option value="49">49</option>
															<option value="50">50</option>
															<option value="51">51</option>
															<option value="52">52</option>
															<option value="53">53</option>
															<option value="54">54</option>
															<option value="55">55</option>
															<option value="56">56</option>
															<option value="57">57</option>
															<option value="58">58</option>
															<option value="59">59</option>
														</select>
													</div>
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" style="text-align: right; padding-top:10px;">
											<button type="button" class="submitBtn" style="height: 27px;width: 60px;">등록</button>
											<button type="button" class="cancelBtn" style="height: 27px;width: 60px; color: #636363;">취소</button>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</section>
			</div>
		</div>
	</section>
	<script type="text/javascript">
		var ajax = new Ajax({
			contextPath : _contextPath,
			callCount : 5,
			timeout : 1000*60*60*3,
			loadComponent : jQuery("#ajaxLoading")
		});
	</script>
	<script type="text/javascript" src="<c:url value="/resources/js/linkdata/linkdataChart.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/linkdata/linkdataManage.js"/>"></script>
</body>
</html>
