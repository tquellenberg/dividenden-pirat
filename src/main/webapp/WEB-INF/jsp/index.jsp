<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><!DOCTYPE html>
<html lang="de">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Dividenden-Pirat</title>
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
	
	<!-- Custom styles for this template -->
    <link href="/css/divisammler.css" rel="stylesheet">
    
</head>
<body>
	<jsp:include page="nav.jsp" />
	<div class="container">
		<div class="jumbotron my-jumbotron">
			<h1>Dividenden-Pirat</h1>
			<p>Übersichtliche Darstellung der Daten der "Schwarm-Tabelle" aus dem Blog "Dividenden-Sammler"</p>
			<div style="text-align:center;">
			    <a class="btn btn-lg btn-primary preview-link" href="/schwarmtabelle">Schwarm-Tabelle</a>
  			</div>
		</div>

		<div class="page-header">
			<h2>Was ist die Schwarm-Tabelle?</h2>
			<p class="lead">Es handelt sich um eine öffentliche Tabelle mit Aktienkennzahlen, die durch den 
			Blog <a href="http://dividenden-sammler.de/10100/schwarm-working-kennzahlen/" target="_blank">"Dividenden-Sammler"</a> verwaltet wird.</p>
			<p>
			Der primäre Faktor bei der Tabelle sind folgende Kennzahlen:
			<ul>
				<li>Gewinn pro Aktie</li>
				<li>Dividende pro Aktie</li>
				<li>Kurs-Gewinn-Verhältnis (KGV)</li>
			</ul></p>
			<p>Die Zahlen werden durch freiwillige Helfer gepflegt und bereinigt. In der Summe ergibt sich 
			eine Liste von Aktien, die nach verschiedenen Kriterien sortiert werden kann. Ziel ist es Aktien 
			zu identifizieren, die aktuell kaufenswert erscheinen.</p>
			<p>Weder die Auswahl der Aktien noch deren Bewertung sollte zu einem automatschen Kauf 
			führen. Alle Angaben sind ohne Gewähr und sollten nur dazu genutzt werden, um Aktien 
			für die eigene Watchlist zu finden. <strong>Jeder Kaufentscheidung sollte eine eigene Bewertung der 
			Aktie voraus gehen.</strong></p>
		</div>
		<div class="page-header">
			<h2>Wozu diese Seite?</h2>
			<p class="lead">Diese Seite soll einen einfachen Zugang zu den vielen Kennzahlen und Bewertungen der "Schwarm-Tabelle" bieten.</p>
			<p>Alle hier gezeigten Tabellen basieren auf den Daten der 
			<a href="https://docs.google.com/spreadsheets/d/1XxGyi1EPsheM0ayIAqrnK-vLgz1hSkWD5fUdeSKN03M/edit?pli=1#gid=808011782" target="_blank">Schwarm-Tabelle</a>.
			Sie stellen diese aber in einer stark vereinfachten und grafisch aufbereiteten Form dar, um ein leichteres 
			Verständniss zu ermöglchen.</p>
		</div>
	</div>
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>