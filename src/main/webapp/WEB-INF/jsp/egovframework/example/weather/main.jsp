<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="egovframework.example.sample.service.WeatherType"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>?†Ïî® ?ïÎ≥¥</title>
<link rel="stylesheet" href="/css/egovframework/bootstrap.min.css">
<script src="/js/common/jquery.slim.js"></script>
<script src="/js/common/jquery-latest.min.js"></script>
<script src="/js/common/popper.min.js"></script>
<script src="/js/common/bootstrap.bundle.min.js"></script>
<script async
	src="https://maps.googleapis.com/maps/api/js?key={your-api-key}&callback=console.debug&libraries=maps,marker&v=beta"
	defer></script>
<style>
.flex-nowrap::-webkit-scrollbar {
	width: 3px; /* ?§ÌÅ¨Î°§Î∞î???àÎπÑ */
}

.flex-nowrap::-webkit-scrollbar-thumb {
	background: #217af4;
	border-radius: 10px;
}

.flex-nowrap::-webkit-scrollbar-track {
	background: rgba(33, 122, 244, .1);
}
</style>
</head>
<body>
	<div class="container-fluid mt-3">
		<div class="header">
			<button class='btn btn-primary' type="button"
				onclick="click_set_geolocation()">???ÑÏπò ÏßÄ??/button>
		</div>
		<div class="row flex-nowrap"
			style="height: 400px; overflow-x: scroll;">
			<c:forEach var="weather" items="${weatherList}" varStatus="status">
				<div class="card m-2" style="min-height: 350px; min-width: 300px;">
					<c:choose>
						<c:when test="${weather.type eq WeatherType.SUN}">
							<img class="card-img-top" src="/images/weather/sun.png" alt="ÎßëÏùå"
								width="200px" height="200px">
						</c:when>
						<c:when test="${weather.type eq WeatherType.RAIN}">
							<img class="card-img-top" src="/images/weather/rainy.png" alt="Îπ?
								width="200px" height="200px">
						</c:when>
						<c:when test="${weather.type eq WeatherType.SNOW}">
							<img class="card-img-top" src="/images/weather/snowy.png" alt="??
								width="200px" height="200px">
						</c:when>
						<c:when test="${weather.type eq WeatherType.CLOUDY}">
							<img class="card-img-top" src="/images/weather/cloud.png"
								alt="?êÎ¶º" width="200px" height="200px">
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
					<div class="card-body">
						<c:choose>
							<c:when test="${weather.type eq WeatherType.SUN}">
								<h3 class="card-title">ÎßëÏùå</h3>
							</c:when>
							<c:when test="${weather.type eq WeatherType.RAIN}">
								<h3 class="card-title">Îπ?/h3>
							</c:when>
							<c:when test="${weather.type eq WeatherType.SNOW}">
								<h3 class="card-title">??/h3>
							</c:when>
							<c:when test="${weather.type eq WeatherType.CLOUDY}">
								<h3 class="card-title">?êÎ¶º</h3>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<p class="card-text">
							?úÍ∞Ñ : <span class="date"><c:out value="${weather.date}" /></span>/<span
								class="hour"><c:out value="${weather.time}" /></span>
						</p>
						<p class="card-text">Í∏∞Ïò® : ${weather.temp}¬∞C</p>
					</div>
				</div>
			</c:forEach>
		</div>
		<hr />
		<div id="weather-forecast">
			<h3>?ºÍ∏∞ ?ÑÎßù</h3>
			<div>${forecast}</div>
		</div>
		<hr />
		<div id="weekly-forecast" class="row">
			<div class="col">
				<h3>10?ºÍ∞Ñ ?ÑÎßù</h3>
				<c:forEach var="condition" items="${midterm.conditionList}"
					varStatus="status">
					<div>
						<span>${status.index+3}????/span>
						<!-- ?§Ï†Ñ -->
						<c:choose>
							<c:when test="${condition.amCondition eq 'ÎßëÏùå'}">
								<img src="/images/weather/sun.png" alt="ÎßëÏùå" width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.amCondition, 'Îπ?)}">
								<img src="/images/weather/rainy.png" alt="Îπ? width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.amCondition, '??)}">
								<img src="/images/weather/snowy.png" alt="?? width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.amCondition, 'Íµ¨Î¶ÑÎß?)}">
								<img src="/images/weather/cloud.png" alt="?êÎ¶º" width="50px"
									height="50px">
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						/
						<!-- ?§ÌõÑ -->
						<c:choose>
							<c:when test="${condition.pmCondition eq 'ÎßëÏùå'}">
								<img src="/images/weather/sun.png" alt="ÎßëÏùå" width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.pmCondition, 'Îπ?)}">
								<img src="/images/weather/rainy.png" alt="Îπ? width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.pmCondition, '??)}">
								<img src="/images/weather/snowy.png" alt="?? width="50px"
									height="50px">
							</c:when>
							<c:when test="${fn:contains(condition.pmCondition, 'Íµ¨Î¶ÑÎß?)}">
								<img src="/images/weather/cloud.png" alt="?êÎ¶º" width="50px"
									height="50px">
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<span><c:out
								value="${midterm.temperatureList[status.index].min}" />/<c:out
								value="${midterm.temperatureList[status.index].max}" /></span>
					</div>
				</c:forEach>
			</div>
			<div id="dust-area" class="col" style="max-width: 50%;">
				<h3>ÎØ∏ÏÑ∏Î®ºÏ?</h3>
				<p>${dust.pm10Value}(pm10)</p>
				<div class="progress">
					<div class="progress-bar" role="progressbar"
						style="width : ${dust.pm10Value}%"
						aria-valuenow="${dust.pm10Value}" aria-valuemin="0"
						aria-valuemax="1000"></div>
				</div>
				<h3 class="mt-3">Í∏∞ÏÉÅ?πÎ≥¥</h3>
				<div class="card-list row flex-nowrap"
					style="overflow-x: scroll;">
					<c:forEach var="warn" items="${warning}">
						<div class="card col m-2 text-wrap"
							style="min-width: 150px; min-height: 180px; max-width: 150px; max-height: 180px; overflow-y : scroll;">
							<c:out value="${warn.t2}"/>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<hr />
		<div style="height: 500px;">
			<h3>?ÑÏû¨ ?ÑÏπò Ï£ºÎ?(Í∏∞Î≥∏Í∞?: ?úÏö∏??</h3>
			<div id="gmap" style="height: 100%;"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	window.onload = () => {
		const hours = document.getElementsByClassName('hour');
		const dates = document.getElementsByClassName("date");
		const gmap = document.getElementById("gmap");
		var x = null;
		var y = null;
		var map;
		
		for(var i = 0; i < hours.length; i++) {
			var hr = hours[i].innerText;
			hours[i].innerText = convert_time_format(hr);
		}
		
		for(var i = 0; i < dates.length; i++) {
			var dt = dates[i].innerText;
			dates[i].innerText = convert_date_format(dt);
		}
		
		const urlParams = new URL(window.location.href).searchParams;
		if(urlParams.get('x')) {
			x = urlParams.get('x')
		}
		
		if(urlParams.get('y')) {
			y = urlParams.get('y')
		}
		
		if(x !== null && x !== '' && y !== null && y !== '') {
			map = new google.maps.Map(document.getElementById("gmap"), {
			    center: { lat: Number(x), lng: Number(y)},
			    zoom: 16,
			});
		} else {
			map = new google.maps.Map(document.getElementById("gmap"), {
			    center: { lat: 37.5549, lng: 126.9708 },//Ï¥àÍ∏∞Í∞??úÏö∏??
			    zoom: 16,
			});
		}
	}

	function click_set_geolocation() {
		if (navigator.geolocation) {
			var positionOptions = {
				enableHighAccuracy : true,
				maximumAge : 0,
				timeout : (1000 * 60 * 30)
			};
			navigator.geolocation.getCurrentPosition(onGeolocationSuccess,
					onGeolocationFail, positionOptions);
		}
	}
	
	function onGeolocationSuccess(position) {
		// Ï¢åÌëú Ï∂úÎ†•
		console.log("lat: " + position.coords.latitude + ", lon: "
				+ position.coords.longitude);
		window.location.href = "/main.do?x=" + position.coords.latitude + "&y="
				+ position.coords.longitude;
	}

	function onGeolocationFail(error) {
		// ?êÎü¨ Ï∂úÎ†•
		console.log("Error Code: " + error.code + ", Error Description: "
				+ error.message);
	}
	
	function convert_date_format(date) {
		var hour = date.substr(4, 4);
		var month = hour.substr(0, 2);
		var day = hour.substr(2, 2);
		
		month = parseInt(month);
		day = parseInt(day);
		
		return month+"."+day
	}
	
	function convert_time_format(time) {
		var hour = time.substr(0, 2);
		var hour_int = parseInt(hour);
		
		if(0 <= hour_int && hour_int < 12)
			return hour_int + ":00 AM";
		else
			return hour + ":00 PM";
	}
</script>
</html>