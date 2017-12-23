package com.awacp.util;

import java.io.File;
import java.io.FileOutputStream;

import com.awacp.entity.ShipmentStatus;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class PodPdfGenerator {
	private static final Rectangle PAGE_SIZE = PageSize.A6;
	private String pdfFilePath;
	private String logoPath;
	private Document document;
	private ShipmentStatus status;
	private PdfWriter pdfWriter;

	public PodPdfGenerator(String pdfFilePath, String logoPath, ShipmentStatus status) {
		this.pdfFilePath = pdfFilePath;
		this.logoPath = logoPath;
		this.status = status;
	}

	public void generate() throws Exception {
		document = new Document(PAGE_SIZE);
		pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(new File(this.pdfFilePath)));
		document.open();
		document.add(new Chunk(""));
		writeMetaInformation();
		addContent();
		addFooter();
		document.close();
		pdfWriter.close();

	}

	private void writeMetaInformation() {
		document.addAuthor("Debendra Barik");
		document.addCreator("AWACP");
		document.addSubject("Quote worksheet");
		document.addCreationDate();
		document.addTitle("Quote detail");
	}
	private void addContent() throws Exception {
		Image image = Image.getInstance(logoPath);
		Paragraph p = new Paragraph("");
		p.add(image);
		p.add("Proof of Delivery");
		document.add(p);		
		document.add(new Phrase("\n"));

		p = new Paragraph(new Chunk("Dear Customer,", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		document.add(p);		
		p = new Paragraph(new Phrase("This notice serves as proof of delivery for the shipment listed below.", FontFactory.getFont(FontFactory.HELVETICA, 8)));	
		document.add(p);
		document.add(new Phrase("\n"));
		p = new Paragraph("Tracking Number:					"+status.getTrackingNumber(), FontFactory.getFont(FontFactory.HELVETICA, 8));
		document.add(p);
		p = new Paragraph(new Phrase("Service:					"+status.getServiceProvider(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		document.add(p);
		p = new Paragraph(new Phrase("Delivered On:					"+status.getDeliveredOn(), FontFactory.getFont(FontFactory.HELVETICA, 8)));		
		document.add(p);
		p = new Paragraph(new Phrase("Delivered To:					"+status.getDeliveredTo(), FontFactory.getFont(FontFactory.HELVETICA, 8)));		
		document.add(p);
		p = new Paragraph(new Phrase("Received By:	"+status.getReceivedBy(), FontFactory.getFont(FontFactory.HELVETICA, 8)));		
		document.add(p);
		p = new Paragraph(new Phrase("Left At:	"+status.getLeftAt(), FontFactory.getFont(FontFactory.HELVETICA, 8)));		
		document.add(p);
		
		 document.add( Chunk.NEWLINE );
	}

	private void addFooter() throws Exception {
		Paragraph p = new Paragraph(new Chunk("Thank you for giving us this opportunity to serve you.", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		document.add(p);
		p = new Paragraph(new Chunk("Sincerely, ", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		document.add(p);
		p = new Paragraph(new Chunk("UPS, ", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		document.add(p);
		document.add(new Phrase("\n"));

		String str = ""+status.getLastLine();
		Chunk lastLine = new Chunk(str, FontFactory.getFont(FontFactory.HELVETICA, 8));
		p = new Paragraph(lastLine);
		document.add(p);
	}

	public static void main(String args[]) {
		ShipmentStatus ss = new ShipmentStatus();
		ss.setServiceProvider("UPS GROUND");
		ss.setDeliveredOn("12/08/2017 12:32 P.M.");
		ss.setDeliveredTo("BROOKLYN, NY, US");
		ss.setReceivedBy("OLGA");
		ss.setLeftAt("Inside Delivery");
		ss.setLastLine("Tracking results provided by UPS:   30/11/2017 12:56   ET");
		ss.setProviderLogo("pod_img_ups.gif");
		ss.setTrackingNumber("1Z1536630359143967668912563");
		String filePath = "E:/InstalledSoftwares/xampp/htdocs/tutorial/resource/" + ss.getTrackingNumber() + ".pdf";
		String logoPath = "E:/InstalledSoftwares/xampp/htdocs/tutorial/images/" + ss.getProviderLogo();

		try {
			new PodPdfGenerator(filePath, logoPath, ss).generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
