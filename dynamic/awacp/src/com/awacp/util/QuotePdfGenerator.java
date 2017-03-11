package com.awacp.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.awacp.entity.MnD;
import com.awacp.entity.Pdni;
import com.awacp.entity.Product;
import com.awacp.entity.QuoteNote;
import com.awacp.entity.Takeoff;
import com.awacp.entity.Worksheet;
import com.awacp.entity.WsManufacturerInfo;
import com.awacp.entity.WsProductInfo;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class QuotePdfGenerator {
	private static final Rectangle PAGE_SIZE = PageSize.A4;
	private String pdfFilePath;
	private String logoPath;
	private Document document;
	private Worksheet worksheet;
	private PdfWriter pdfWriter;
	private float GRAY_FILL = 0.90f;

	public QuotePdfGenerator(String pdfFilePath, String logoPath, Worksheet worksheet) {
		this.pdfFilePath = pdfFilePath;
		this.logoPath = logoPath;
		this.worksheet = worksheet;
	}

	public void generate() throws Exception {
		document = new Document(PAGE_SIZE);
		pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(new File(this.pdfFilePath)));
		document.open();
		document.add(new Chunk(""));
		writeMetaInformation();
		addHeaderInformation();
		addQuoteInformation();
		addProductInformation();
		addNotes();
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

	private void addHeaderInformation() throws Exception {
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(PageSize.A4.getWidth() - 60);
		table.setLockedWidth(true);
		PdfPCell cell;
		
		cell = new PdfPCell(new Phrase(""));
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(Image.getInstance(logoPath), true);
		cell.setColspan(2);		
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("PH. #(212) 679-8550", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		
		cell = new PdfPCell(new Phrase("270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Fax #(212) 213-5067", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Website:  www.awacp.org", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Email: edwgs@awacp.org", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setColspan(4);
		cell.setFixedHeight(1f);
		cell.setBackgroundColor(Color.black);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		document.add(table);

	}

	private void addQuoteInformation() throws Exception {
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(PageSize.A4.getWidth() - 60);
		table.setLockedWidth(true);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Project #: ", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setGrayFill(GRAY_FILL);
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(worksheet.getTakeoff().getJobName(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Address: ", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(worksheet.getTakeoff().getJobAddress(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Plan Date ", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Quote No", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setBorder(Rectangle.LEFT);
		cell.setUseVariableBorders(true);
		cell.setBorderColorLeft(Color.white);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Quote Date", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setBorder(Rectangle.LEFT);
		cell.setUseVariableBorders(true);
		cell.setBorderColorLeft(Color.white);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.LEFT);
		cell.setUseVariableBorders(true);
		cell.setBorderColorLeft(Color.white);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("" + worksheet.getTakeoff().getDateCreated().getTime(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(worksheet.getTakeoff().getQuoteId(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("" + worksheet.getTakeoff().getQuoteDate().getTime(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("" + worksheet.getTakeoff().getUserCode(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Engineer", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Architecture", FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.BOLD)));
		cell.setColspan(2);
		cell.setBorder(Rectangle.LEFT);
		cell.setUseVariableBorders(true);
		cell.setBorderColorLeft(Color.white);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("" + worksheet.getTakeoff().getEngineerName(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("" + worksheet.getTakeoff().getArchitectureName(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setColspan(4);
		cell.setFixedHeight(0.5f);
		cell.setBackgroundColor(Color.black);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		document.add(table);

	}

	private void addProductInformation() throws Exception {
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(PageSize.A4.getWidth() - 60);
		table.setLockedWidth(true);
		PdfPCell cell;
		for(WsManufacturerInfo wsManufacturerInfo: worksheet.getManufacturerItems()){
			cell = new PdfPCell(new Phrase(wsManufacturerInfo.getManufacturer().getProductName(), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
			cell.setColspan(4);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Qty./Ft.", FontFactory.getFont(FontFactory.HELVETICA, 8)));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setGrayFill(GRAY_FILL);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Description", FontFactory.getFont(FontFactory.HELVETICA, 8)));
			cell.setColspan(3);
			cell.setBorder(Rectangle.LEFT);
			cell.setUseVariableBorders(true);
			cell.setBorderColorLeft(Color.white);
			cell.setGrayFill(GRAY_FILL);
			table.addCell(cell);
			
			for(WsProductInfo pInfo: wsManufacturerInfo.getProductItems()){				
				cell = new PdfPCell(new Phrase(""+pInfo.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(""+pInfo.getProduct().getProductName(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
				cell.setColspan(3);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}
			cell = new PdfPCell(new Phrase(""));
			cell.setColspan(4);
			cell.setFixedHeight(0.5f);
			cell.setBackgroundColor(Color.black);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("TOTAL NET PRICE (F.O.B. Factory) FREIGHT ALLOWED: $"+wsManufacturerInfo.getTotalAmount(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
			cell.setColspan(4);
			cell.setBorder(Rectangle.BOTTOM);			
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(""));
			cell.setColspan(4);
			cell.setBorder(Rectangle.NO_BORDER);			
			table.addCell(cell);
			
			
			
			if(wsManufacturerInfo.getPdnis() != null){
				cell = new PdfPCell(new Phrase("PDNI: Price Does Not Include", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
				cell.setColspan(4);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setGrayFill(GRAY_FILL);
				table.addCell(cell);
				
				int index = 0;
				for(Pdni pdni: wsManufacturerInfo.getPdnis()){
					cell = new PdfPCell(new Phrase(""+ ++index + ") ", FontFactory.getFont(FontFactory.HELVETICA, 8)));
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(""+pdni.getPdniName(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
					cell.setColspan(3);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
				}
				cell = new PdfPCell(new Phrase(""));
				cell.setColspan(4);
				cell.setFixedHeight(1f);
				cell.setBackgroundColor(Color.black);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}			
		}
		document.add(table);
	}

	private void addNotes() throws Exception {
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(PageSize.A4.getWidth() - 60);
		table.setLockedWidth(true);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase("Terms of Sale", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
		cell.setColspan(4);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setGrayFill(GRAY_FILL);
		table.addCell(cell);

		for (QuoteNote note : worksheet.getNotes()) {
			cell = new PdfPCell(new Phrase(". " + note.getNote(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
			cell.setColspan(4);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
		}

		
		document.add(table);
	}

	private void addFooter() throws Exception {
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(PageSize.A4.getWidth() - 60);
		table.setLockedWidth(true);
		PdfPCell cell;
		cell = new PdfPCell(new Phrase(""));
		cell.setColspan(4);
		cell.setFixedHeight(0.5f);
		cell.setBackgroundColor(Color.black);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(
				new Phrase("ALBERT WEISS AIR CONDITIONING PRODUCTS INC.. Copyright 2013. All rights reserved.", FontFactory.getFont(FontFactory.HELVETICA, 8)));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setColspan(4);
		cell.setFixedHeight(2f);
		cell.setBackgroundColor(Color.black);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		document.add(table);
	}

	public static void main(String args[]) {
		String filePath = "C:/Projects/repo/Debendra/quote.pdf";
		String logoPath = "C:/Projects/repo/Debendra/sts/static/awacp/resource/img/awacp_big_logo.png";
		Worksheet worksheet = new Worksheet();
		Takeoff takeoff = new Takeoff();
		takeoff.setJobName("ADMIRALS ROW BUILDING E");
		takeoff.setJobAddress("25 NAVY STREET 1ST FL-2ND FL ,ROOF FL BLDG E BROOKLYN NY");
		takeoff.setDateCreated(Calendar.getInstance());
		takeoff.setQuoteDate(Calendar.getInstance());
		takeoff.setQuoteId("Q-123-456");
		takeoff.setUserCode("DB");
		takeoff.setEngineerName("GEA (GLICKMAN ENGINEERING ASSOCIATES) ");
		takeoff.setArchitectureName("S9 ARCHITECTURE");
		Set<Pdni> pdnis = null;
		worksheet.setTakeoff(takeoff);
		
		WsManufacturerInfo mInfo = null;
		MnD manufacturer = null;
		Set<WsProductInfo> productItems = null;			
		Set<WsManufacturerInfo> manufacturerItems = null;
		WsProductInfo wsProdInfo = null ;
		pdnis = new HashSet<Pdni>();			
		
		mInfo =  new WsManufacturerInfo();
		mInfo.setId(1L);
		manufacturer = new MnD();
		productItems = new HashSet<WsProductInfo>();
		manufacturerItems = new HashSet<WsManufacturerInfo>();
		manufacturer.setProductName("ANEMOSTAT - A MESTEK COMPANY");
		mInfo.setManufacturer(manufacturer);
		mInfo.setTotalAmount(1300D);		
		
		wsProdInfo = new WsProductInfo();
		wsProdInfo.setId(1L);
		wsProdInfo.setQuantity(2);
		wsProdInfo.setProduct(new Product(1L, "SUPPLY REGISTER"));
		productItems.add(wsProdInfo);
		
		wsProdInfo = new WsProductInfo();
		wsProdInfo.setId(2L);
		wsProdInfo.setQuantity(20);
		wsProdInfo.setProduct(new Product(2L, "RETURN GRILLE"));
		productItems.add(wsProdInfo);
		
		wsProdInfo = new WsProductInfo();
		wsProdInfo.setId(3L);
		wsProdInfo.setQuantity(40);
		wsProdInfo.setProduct(new Product(3L, "PERFORATED RETURN GRILLES"));
		productItems.add(wsProdInfo);
		
		wsProdInfo = new WsProductInfo();
		wsProdInfo.setId(4L);
		wsProdInfo.setQuantity(9);
		wsProdInfo.setProduct(new Product(4L, "RETURN REGISTERS"));
		productItems.add(wsProdInfo);
		
		mInfo.setProductItems(productItems);
		
		Pdni pdni = new Pdni();
		pdni.setId(1L);
		pdni.setPdniName("FIRE DAMPERS, DOOR LOUVERS, FUSIBLE LINK DAMPERS ");
		pdnis.add(pdni);
		
		mInfo.setPdnis(pdnis);		
		manufacturerItems.add(mInfo);		
		
		mInfo =  new WsManufacturerInfo();
		mInfo.setId(2L);
		manufacturer = new MnD();
		productItems = new HashSet<WsProductInfo>();
		manufacturer.setProductName("ELECTRIC HEATERS INC.");
		mInfo.setManufacturer(manufacturer);
		mInfo.setTotalAmount(1800D);		
		
		wsProdInfo = new WsProductInfo();
		wsProdInfo.setId(5L);
		wsProdInfo.setQuantity(2);
		wsProdInfo.setProduct(new Product(1L, "SLIP-IN CONSTRUCTION, COMPLETE WITH AUTOMATIC THERMAL AND MANUAL THERMAL CUT-OUT. MAGNETIC CONTACTORS BUILT IN FUSES IF REQUIRED BY N.E.C. TRANSFORMER WITH PRIMARY FUSING, AIR FLOW SWITCH, DOOR INTERLOCKING DISCONNECT SWITCH, SCR CONTROLS, ROOM STATS W/DUCT SENSOR "));
		productItems.add(wsProdInfo);
		mInfo.setProductItems(productItems);
		manufacturerItems.add(mInfo);
		
		worksheet.setManufacturerItems(manufacturerItems);
		Set<QuoteNote> notes = new HashSet<QuoteNote>();

		QuoteNote note = new QuoteNote();
		note.setId(1L);
		note.setNote("PRICE IS BASED ON MECHANICAL PLANS AND SPECIFICATIONS ONLY");
		notes.add(note);

		note = new QuoteNote();
		note.setId(2L);
		note.setNote("PRICES ARE VALID FOR 30 DAYS ONLY");
		notes.add(note);

		note = new QuoteNote();
		note.setId(3L);
		note.setNote("STANDARD WHITE FINISH U.O.N.");
		notes.add(note);

		note = new QuoteNote();
		note.setId(4L);
		note.setNote("PRICE DOES NOT INCLUDE APPLICABLE SALES TAX");
		notes.add(note);
		
		worksheet.setNotes(notes);
		try {
			new QuotePdfGenerator(filePath, logoPath, worksheet).generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
