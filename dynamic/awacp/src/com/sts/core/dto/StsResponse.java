package com.sts.core.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.awacp.entity.Architect;
import com.awacp.entity.AwInventory;
import com.awacp.entity.AwfInventory;
import com.awacp.entity.Bidder;
import com.awacp.entity.Contractor;
import com.awacp.entity.Engineer;
import com.awacp.entity.Factory;
import com.awacp.entity.GeneralContractor;
import com.awacp.entity.InvMultiplier;
import com.awacp.entity.ItemShipped;
import com.awacp.entity.JInventory;
import com.awacp.entity.JobOrder;
import com.awacp.entity.MarketingTemplate;
import com.awacp.entity.MnD;
import com.awacp.entity.MnDType;
import com.awacp.entity.OrderBook;
import com.awacp.entity.Pdni;
import com.awacp.entity.Product;
import com.awacp.entity.QuoteNote;
import com.awacp.entity.SbcInventory;
import com.awacp.entity.ShipTo;
import com.awacp.entity.ShippedVia;
import com.awacp.entity.Spec;
import com.awacp.entity.SplInventory;
import com.awacp.entity.Takeoff;
import com.awacp.entity.TaxEntry;
import com.awacp.entity.Trucker;
import com.sts.core.entity.User;

@XmlRootElement
@XmlSeeAlso({ Bidder.class, Takeoff.class, Architect.class, Engineer.class, Contractor.class, User.class, Spec.class,
		ShipTo.class, ShippedVia.class, QuoteNote.class, ItemShipped.class, MnD.class, MnDType.class, Product.class,
		GeneralContractor.class, Pdni.class, Trucker.class, JInventory.class, SbcInventory.class, SplInventory.class,
		AwfInventory.class, AwInventory.class, MarketingTemplate.class, InvMultiplier.class, TaxEntry.class,
		JobOrder.class, OrderBook.class, Factory.class })
public class StsResponse<T> {
	private String status;
	private String message;
	private int totalCount;
	private List<T> results;

	public StsResponse() {
		super();
	}

	public StsResponse(int totalCount, List<T> results) {
		super();
		this.totalCount = totalCount;
		this.results = results;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getResults() {
		return results;
	}

	public StsResponse<T> setResults(List<T> results) {
		this.results = results;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
