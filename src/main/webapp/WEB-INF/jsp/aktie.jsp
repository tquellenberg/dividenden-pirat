<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="de">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Dividenden-Pirat - Schwarm-Kennzahlen - ${entry.name}</title>
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
	
	<!-- Custom styles for this template -->
    <link href="/css/divisammler.css" rel="stylesheet">
    <link href="/css/chart.css" rel="stylesheet">
    
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" src="/webjars/flot/0.8.3/jquery.flot.js"></script>
    <script type="text/javascript" src="/webjars/flot/0.8.3/jquery.flot.threshold.js"></script>
    
    <script type="text/javascript">
	$(function() {
		var d1 = [];
		var d2 = [];
	<c:forEach var="e" items="${entry.entries}">
		<c:if test="${e.forwardPE != '0'}">
			d1.push([${e.year}, ${e.forwardPE}]);
			d2.push([${e.year}, ${entry.medianForwardPE}]);
		</c:if>
	</c:forEach>
		$.plot("#placeholder", [{
			data: d1,
			color: "rgb(200, 20, 30)",
			threshold: {
				below: ${entry.medianForwardPE},
				color: "rgb(30, 180, 20)"
			},
			lines: { show: true, fill: true },
			points: { show: true }
		}, {
			data: d2,
			color: "rgb(70, 70, 70)"
		}], {
			yaxis: {
				min: 7 
			}
		});

		var d3 = [];
		var d4 = [];
	<c:forEach var="e" items="${entry.entries}">
		d3.push([${e.year}, ${e.earningsEuro}]);
		<c:if test="${e.earningsChangePercent != null}">
			d4.push([${e.year}-1, ${e.earningsChangePercent}]);
		</c:if>
	</c:forEach>
		$.plot("#placeholderEarnings", [{
			data: d3,
			label: "Gewinn in €",
			color: "rgb(0, 0, 0)",
			lines: { show: true },
			points: { show: true }
		}, {
			data: d4,
			label: "Veränderung in Prozent",
			bars: { show: true },
			color: "rgb(30, 180, 20)",
			threshold: {
				below: 0,
				color: "rgb(200, 20, 30)"
			},
			yaxis: 2
		}], {
		   yaxes: [ { 
				tickFormatter: euroFormatter
		   }, {
				alignTicksWithAxis: 1,
				position: "right",
				tickFormatter: percentFormatter
			}],
			legend: { position: "nw" }
		});	

		var d5 = [];
	<c:forEach var="e" items="${entry.entries}">
		d5.push([${e.year}, ${e.dividend}]);
	</c:forEach>
		$.plot("#placeholderDividend", [{
			data: d5,
			label: "Dividende in Heimat-Währung",
			color: "rgb(0, 0, 0)",
			lines: { show: true },
			points: { show: true }
		}], {
			legend: { position: "nw" }
		});	

	});
	function euroFormatter(v, axis) {
		return v.toFixed(axis.tickDecimals) + "€";
	}
	function percentFormatter(v, axis) {
		return v.toFixed(axis.tickDecimals) + "%";
	}
	</script>
</head>
<body>
	<jsp:include page="nav.jsp" />

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<h1 class="page-header">${entry.name}</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-1 lead">
				KGV:<br>${watchListEntry.pePosition}. Platz
			</div>		
			<div class="col-md-2 lead">
				Payback-Time:<br>${watchListEntry.paybackTime}. Platz
			</div>		
			<div class="col-md-2 lead">
				Performance:<br>${watchListEntry.growthPosition}. Platz
			</div>		
			<div class="col-md-2 lead">
				Robustheit:<br>${watchListEntry.robustnessPosition}. Platz
			</div>		
			<div class="col-md-2 lead">
				Größe:<br>${watchListEntry.sizePosition}. Platz
			</div>
		</div>
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				Platzierung bezieht sich auf insgesamt ${watchlistSize} Werte.

				<h2>Kurs-Gewinn-Verhältnis (KGV)</h2>
				<div class="demo-container">
					<div id="placeholder" class="demo-placeholder"></div>
				</div>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Geschäftsjahr</th>
							<th>KGV (forward)</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="e" items="${entry.entries}">
						<c:if test="${e.forwardPE != '0'}">
							<tr>
							<td>${e.year}</td><td>${e.forwardPE}</td>
							</tr>
						</c:if>
					</c:forEach>
						<tr>
						<td><strong>Median KGV</strong></td><td><strong>${entry.medianForwardPE}</strong></td>
						</tr>
						<c:choose>
							<c:when test="${entry.peRelation < 0}">
								<tr>
									<td><strong>Abweichung zum Median</strong></td>
									<td class="success"><strong>${entry.peRelation}%</strong></td>
								</tr>
							</c:when>
							<c:when test="${entry.peRelation > 10}">
								<tr>
									<td><strong>Abweichung zum Median</strong></td>
									<td class="danger"><strong>${entry.peRelation}%</strong></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td><strong>Abweichung zum Median</strong></td>
									<td><strong>${entry.peRelation}%</strong></td>
								</tr>
							</c:otherwise>
						</c:choose>
						
					</tbody>
					<tfoot>
						<tr><td colspan="2"><sub>Alle Angaben ohne Gewähr.</sub></td></tr>
					</tfoot>
				</table>

				<h2>Ergebnis pro Aktie (Euro)</h2>
				<div class="demo-container">
					<div id="placeholderEarnings" class="demo-placeholder"></div>
				</div>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Geschäftsjahr</th>
							<th>Ergebnis (€)</th>
							<th>Veränderung (%)</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="e" items="${entry.entries}">
						<tr>
						<td>${e.year}</td>
						<td>${e.earningsEuro}</td>
						<td>
							<c:if test="${e.earningsChangePercent != null}">
								${e.earningsChangePercent}%
							</c:if>
						</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr><td colspan="3"><sub>Alle Angaben ohne Gewähr. Daten für zukünfig Geschäftsjahre beruhen auf Schätzungen.</sub></td></tr>
					</tfoot>
				</table>

				<h2>Dividende</h2>
				<div class="demo-container">
					<div id="placeholderDividend" class="demo-placeholder"></div>
				</div>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Jahr</th>
							<th>Dividende</th>
							<th>Veränderung (%)</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="e" items="${entry.entries}">
						<c:if test="${e.dividend != null}">
							<tr>
								<td>${e.year}</td>
								<td>${e.dividend}</td>
								<td>
									<c:if test="${e.dividendChangePercent != null}">
										${e.dividendChangePercent}%
									</c:if>
								</td>
							</tr>
						</c:if>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr><td colspan="3"><sub>Alle Angaben ohne Gewähr.</sub></td></tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>

    <script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body> 
</html>