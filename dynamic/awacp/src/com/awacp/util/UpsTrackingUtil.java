package com.awacp.util;

import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.awacp.entity.ShipmentStatus;
import com.sts.core.constant.StsCoreConstant;

public class UpsTrackingUtil {
	private static boolean DEBUG = false;
	private static final int TIMEOUT = 0; // Default 30 second(s)
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";
	private static final String REFERRER = "http://www.google.com";
	private static final int MAX_BODY_SIZE = 0;
	private static final boolean FOLLOW_REDIRECTS = true;
	public static final String trackingUrl = "https://www.ups.com/bd/en/Home.page";
	private static final String podUrl = "https://wwwapps.ups.com/WebTracking/processPOD?Requester=&tracknum=";

	public static ShipmentStatus track(String trackingNumber) throws Exception {
		ShipmentStatus ss = new ShipmentStatus();
		ss.setTrackingUrl(trackingUrl);
		ss.setTrackingNumber(trackingNumber);
		Connection.Response response = visitUrl(trackingUrl);
		if (response == null) {
			ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_CONNECT);
			return ss;
		}
		Document document = response.parse();
		if (document == null) {
			ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_PARSE_CONTENT);
			return ss;
		}
		// System.err.println(document.html());
		// Try to get search form reference in home page
		Element formElement = document.select("form[name=trackMod]").first();
		/* Global search form is unavailable or unable to get */
		if (formElement == null) {
			ss.setConnectionStatus(StsCoreConstant.NO_TRACKING_SEARCH_FORM);
			return ss;
		}

		FormElement form = (FormElement) formElement;
		form.getElementById("ups-track--qs").val(trackingNumber);

		Document result = form.submit().cookies(response.cookies()).userAgent(USER_AGENT).referrer(REFERRER)
				.header("Accept-Encoding", "gzip, deflate").timeout(TIMEOUT).maxBodySize(MAX_BODY_SIZE)
				.followRedirects(FOLLOW_REDIRECTS).get();

		// Current delivery status
		Elements statusElements = result.select("a#tt_spStatus");
		String statusText = "";
		if (statusElements == null || statusElements.select("a#tt_spStatus") == null) {
			ss.setConnectionStatus(StsCoreConstant.NO_STATUS);
			return ss;
		}
		statusText = statusElements.select("a#tt_spStatus").get(0).text();
		ss.setCurrStatusText(statusText);
		if (ss.getCurrStatusText().trim().equalsIgnoreCase("delivered")) {
			String podFinalUrl = podUrl + trackingNumber + "&refNumbers=&loc=en_BD";
			doLog(podFinalUrl);
			Connection.Response podResponse = visitUrl(podFinalUrl);
			if (podResponse == null) {
				ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_CONNECT_POD);
			}
			Document podDocument = podResponse.parse();
			if (podDocument == null) {
				ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_PARSE_POD_CONTENT);
				return ss;
			}
			if (podDocument.html().contains("Proof of Delivery")) {
				Elements podContentBody = podDocument.select("div.appBody");
				if (podContentBody == null) {
					ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_PARSE_POD_CONTENT);
					return ss;
				}
				Element content = podContentBody.select("dl.outHozFixed").first();
				if (content == null) {
					ss.setConnectionStatus(StsCoreConstant.UNABLE_TO_PARSE_POD_CONTENT);
					return ss;
				}
				ss.setDelivered(true);
				Elements dts = content.select("dt");
				Elements dds = content.select("dd");
				if (dts != null && dds != null) {
					Iterator<Element> dtsIterator = dts.iterator();
					Iterator<Element> ddsIterator = dds.iterator();
					while (dtsIterator.hasNext() && ddsIterator.hasNext()) {
						Element dt = (Element) dtsIterator.next();
						Element dd = (Element) ddsIterator.next();

						if (dt.text().equalsIgnoreCase("Tracking Number:")) {
							ss.setTrackingNumber(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Service:")) {
							ss.setServiceProvider(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Weight:")) {
							ss.setWeight(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Shipped/Billed On:")) {
							ss.setBilledOn(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Delivered On:")) {
							ss.setDeliveredOn(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Delivered To:")) {
							ss.setDeliveredTo(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Received By:")) {
							ss.setReceivedBy(dd.text());
						}
						if (dt.text().equalsIgnoreCase("Left At:")) {
							ss.setLeftAt(dd.text());
						}

					}
					Elements paragraphs = podContentBody.select("p");
					if (paragraphs != null) {
						for (Element p : paragraphs) {
							if (p.hasText() && p.html().contains("Tracking results provided by UPS:")) {
								ss.setLastLine(p.text());
								break;
							}
						}
					}

					System.out.println("Tracking Number: " + ss.getTrackingNumber());
					System.out.println("Service: " + ss.getServiceProvider());
					System.out.println("Weight: " + ss.getWeight());
					System.out.println("Shipped/Billed On: " + ss.getBilledOn());
					System.out.println("Delivered On: " + ss.getDeliveredOn());
					System.out.println("Delivered To: " + ss.getDeliveredTo());
					System.out.println("Received By: " + ss.getReceivedBy());
					System.out.println("Left At: " + ss.getLeftAt());
					System.out.println("Last line: " + ss.getLastLine());
				}
				ss.setPodGenerated(true);

			}
		}
		ss.setConnectionStatus(StsCoreConstant.SUCCESS);

		System.err.println("Track: end");

		return ss;
	}

	public static Response visitUrl(String url) throws Exception {
		return visitUrl(url, USER_AGENT, TIMEOUT, Connection.Method.GET);
	}

	public static Response visitUrl(String url, String userAgent, int timeout, Connection.Method method)
			throws Exception {
		return Jsoup.connect(url).userAgent(USER_AGENT).referrer(REFERRER).header("Accept-Encoding", "gzip, deflate")
				.timeout(TIMEOUT).maxBodySize(MAX_BODY_SIZE).followRedirects(FOLLOW_REDIRECTS)
				.method(Connection.Method.GET).execute();
	}

	private static void doLog(String output) {
		if (DEBUG)
			System.err.println(output);
	}

	public static void main(String args[]) {
		try {
			// UpsTrackingUtil.track("1Z1536630358119781");
			UpsTrackingUtil.track("1Z1536630359143967");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
