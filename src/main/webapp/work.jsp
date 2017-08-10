<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- 
    Document   : start
    Created on : 2017-07-15, 22:47:23
    Author     : MO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>ER</title>

<style>
#chartdiv {
	width: 100%;
	height: 800px;
}
</style>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#datepickerFrom").datepicker({
			dateFormat : "yy-mm-dd",
			minDate : new Date(2014, 1 - 1, 1)
		});
	});
	$(function() {
		$("#datepickerTo").datepicker({
			dateFormat : "yy-mm-dd",
			minDate : new Date(2014, 1 - 1, 1)
		});
	});
</script>

<script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
<script src="https://www.amcharts.com/lib/3/serial.js"></script>
<script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
<link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
<script src="https://www.amcharts.com/lib/3/themes/light.js"></script>

</head>
<body>

	<form action="Start" method="post" id="form_id">

		<table style="width: 1200px">
			<tr>
				<td style="text-align: right">Data od:</td>
				<td><input type="text" id="datepickerFrom" name="dateFrom"
					value="${requestScope.dateFrom}" required></td>
				<td style="text-align: right">Data do:</td>
				<td><input type="text" id="datepickerTo" name="dateTo"
					value="${requestScope.dateTo}" required></td>
				<td style="text-align: right">Waluty:</td>
				<td><select name="itemSelected" size="5" multiple required
					id="mc">
						<c:forEach items="${requestScope.selectOpt}" var="entry">
							<option value="${entry.key}" ${requestScope.itemSelected.contains(entry.key) ? 'selected="selected"' : ''} >${entry.value}</option>
						</c:forEach>
				</select>
				</td>
				<td style="text-align: right">Prognozowane dni:</td>
				<td><input type="number" min="0" step="1" id="future" name="future"  maxlength="4" size="4" style="width: 40px;"
					value="${requestScope.future}" required></td>
				<td style="text-align: right">Linia trendu:</td>
				<td><input type="checkbox" id="trend" name="trend" 
				${requestScope.trend.equals("trend") ? 'checked="checked"':''}
					value="trend" ></td>
					
				<td style="text-align: right"><button>ok</button></td>
			</tr>
		</table>
	</form>




	<!-- Chart code -->
	<script>
	${requestScope.chart}
		</script>

	<!-- HTML -->
	<div id="chartdiv"></div>

</body>
</html>
