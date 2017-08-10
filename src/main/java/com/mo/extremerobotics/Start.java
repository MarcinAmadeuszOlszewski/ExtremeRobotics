package com.mo.extremerobotics;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mo.extremerobotics.chart.ChartCreate;
import com.mo.extremerobotics.conf.SelectOptions;
import com.mo.extremerobotics.directorywatcher.Watcher;
import com.mo.extremerobotics.download.Downloader;
import com.mo.extremerobotics.download.DownloaderInitial;
import com.mo.extremerobotics.download.DownloaderXml;

public class Start extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameterValues("itemSelected") != null&&checkPeriodBeetwen(request)>0) {
			
			int future = Integer.parseInt(request.getParameter("future"));
			ChartCreate dto = new ChartCreate();
			String chart = dto.createChart(request.getParameter("dateFrom"), request.getParameter("dateTo"), request.getParameterValues("itemSelected"), future, request.getParameter("trend"));
			request.setAttribute("chart", chart);
			
			request.setAttribute("dateFrom", request.getParameter("dateFrom"));
			request.setAttribute("dateTo", request.getParameter("dateTo"));
			request.setAttribute("itemSelected", Arrays.toString(request.getParameterValues("itemSelected")));
			request.setAttribute("future", future);
			request.setAttribute("trend", request.getParameter("trend"));
		} else {
			LocalDateTime timePoint = LocalDateTime.now();
			request.setAttribute("dateFrom", timePoint.minusMonths(1).toString().substring(0, 10));
			request.setAttribute("dateTo", timePoint.toString().substring(0, 10));
			request.setAttribute("future", 0);
			request.setAttribute("trend", "");
		}
		request.setAttribute("selectOpt", SelectOptions.createSelectOptions());

		RequestDispatcher rd = request.getRequestDispatcher("work.jsp");
		rd.forward(request, response);
	}

	private long checkPeriodBeetwen(HttpServletRequest request) {
		LocalDate dateFrom = LocalDate.parse(request.getParameter("dateFrom"));
		LocalDate dateTo = LocalDate.parse(request.getParameter("dateTo"));
		long daysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo);
		return daysBetween;
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "ExtremeRobotics";
	}

	@Override
	public void init() throws ServletException {
		Downloader.verifyAndCreateStandardPaths();
		startWatcher();

		DownloaderInitial di = new DownloaderInitial();
		if (!di.isTableFilled()) {
			di.downloadHistory();
		}

		startSystematicXmlDownload();
	}

	private void startWatcher() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Watcher(Paths.get(Downloader.DEST_DIR)).processEvents();

			}
		}, "Thread: Directory Watcher...").start();
	}

	private void startSystematicXmlDownload() {
		new Thread(new Runnable() {
			DownloaderXml downloaderXml = new DownloaderXml();

			@Override
			public void run() {
				while (true) {
					downloaderXml.dowloadActualCurrency();
					try {
						Thread.sleep(15 * 60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}, "Thread: Dowload daily XMLs...").start();
	}

}
