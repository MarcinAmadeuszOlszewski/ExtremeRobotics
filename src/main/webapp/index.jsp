<%-- 
    Document   : start
    Created on : 2017-07-15, 22:47:23
    Author     : MO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>ER</title>
</head>
<body>

	<form action="Start" method="get" id="form_id">

		<table style="width: 100%; height: 500px">
			<tr>
				<td style="text-align: center"><h2>KURSY WALUT</h2></td>
			</tr>
			<tr>
				<td style="text-align: center">Strona przedstawia wykresy 10 kursów walut.</td>
			</tr>
			<tr>
				<td style="text-align: center">Aby wyświetlić wykres należy wybrać datę początkową, końcową oraz conajmniej 1 walutę.</td>
			</tr>
			<tr>
				<td style="text-align: center">Opcjonalnie można zaznaczyć aby wyświetlana była linia trendu oraz wybrać ilość dni do prognozowania.</td>
			</tr>
			<tr>
				<td style="text-align: center">Wykres można powiększać przy pomocy niebieskiego suwaka na górze lub rolki myszy, dodatkowo klikając na legendę można wyłączyć linie.</td>
			</tr>
			<tr>
				<td style="text-align: center">Kurs wyświetlany jest linią ciągłą, prognoza - przerywaną, linia trendu ciągłą półprzeźroczystą.</td>
			</tr>
			<tr>
				<td style="text-align: center"><button>Wchodzę</button></td>
			</tr>
		</table>


	</form>

</body>
</html>
