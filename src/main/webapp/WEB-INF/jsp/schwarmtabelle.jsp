<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="de">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Dividenden-Pirat - Schwarm-Kennzahlen</title>
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
	
	<!-- Custom styles for this template -->
    <link href="/css/divisammler.css" rel="stylesheet">

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript">
    	$(function() {
			$("select[name='pe']").val("${weight.peFactor}");
			$("select[name='payback']").val("${weight.paybackTime}");
			$("select[name='performance']").val("${weight.growthFactor}");
			$("select[name='robustness']").val("${weight.robustnessFactor}");
			$("select[name='size']").val("${weight.sizeFactor}");
    	})
	</script>     
</head>
<body>
	<jsp:include page="nav.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<h1 class="page-header">Schwarm-Kennzahlen</h1>
				<p class="lead">Aus den Kennzahlen einer Aktie ergeben sich je eine Position für KGV, Performance, Robustheit etc. 
				Die Sortierung der Tabelle bestimmt sich über die Gewichtung (0-3) der verschiedenen Positionen. Je kleiner 
				die Position (bzw. die Summe) um so besser.</p>

				<div class="alert alert-danger">
					<span class="glyphicon glyphicon-exclamation-sign"></span>
					<strong>Achtung:</strong> Die Bewertung der Aktien stellt keine Kaufempfehlung dar. Alle Angaben sind ohne Gewähr.
				</div>
				<div class="table-responsive">
					<ul class="nav nav-tabs">
						<li <c:if test="${weight.name == 'default'}">class="active"</c:if>><a href="/schwarmtabelle">Normale Gewichtung</a></li>
						<li <c:if test="${weight.name == 'alternative'}">class="active"</c:if>><a href="/schwarmtabelle/alternative">Alternative Gewichtung</a></li>
						<li <c:if test="${weight.name == 'myWeight'}">class="active"</c:if>><a data-toggle="modal" data-target="#myModal" href="">Meine Gewichtung <span class="glyphicon glyphicon-cog"></span></a></li>
					</ul>
					<table class="table table-striped">
						<thead>
							<tr>
								<th></th>
								<th style="text-align:right">Gewichtung:</th>
								<th>x ${weight.peFactor}</th>
								<th>x ${weight.paybackTime}</th>
								<th>x ${weight.growthFactor}</th>
								<th>x ${weight.robustnessFactor}</th>
								<th>x ${weight.sizeFactor}</th>
								<th>= Summe</th>
							</tr>
							<tr>
								<th>Platz</th>
								<th>Name</th>
								<th>KGV</th>
								<th>Payback-Time</th>
								<th>Performance</th>
								<th>Robustheit</th>
								<th>Größe</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${entries}" var="e" varStatus="i">
								<tr>
									<td><strong>${i.index+1}.</strong></td>
									<td><a href="/aktie/${e.urlEncodedName}">${e.name}</a></td>
									<td <c:if test="${e.pePosition < 15}">class="success"</c:if>>${e.pePosition}.</td>
									<td <c:if test="${e.paybackTime < 15}">class="success"</c:if>>${e.paybackTime}.</td>
									<td <c:if test="${e.growthPosition < 15}">class="success"</c:if>>${e.growthPosition}.</td>
									<td <c:if test="${e.robustnessPosition < 15}">class="success"</c:if>>${e.robustnessPosition}.</td>
									<td <c:if test="${e.sizePosition < 15}">class="success"</c:if>>${e.sizePosition}.</td>
									<td><strong>${e.combinedValue}</strong></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="myWeightDialog.jsp" />	
	
    <script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>