package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "OrderBookInvItem.getByOrderBookId", query = "SELECT ob FROM OrderBookInvItem ob WHERE ob.archived = 'false' AND ob.bookId = :bookId"),
		@NamedQuery(name = "OrderBookInvItem.getAllByOrderBookId", query = "SELECT ob FROM OrderBookInvItem ob WHERE ob.bookId = :bookId"),
		@NamedQuery(name = "OrderBookInvItem.getByBookAndInvItemId", query = "SELECT ob FROM OrderBookInvItem ob WHERE ob.archived = 'false' AND ob.bookId = :bookId AND ob.invItemId = :invItemId"),
		@NamedQuery(name = "OrderBookInvItem.getByAllBookAndInvItemId", query = "SELECT ob FROM OrderBookInvItem ob WHERE ob.bookId = :bookId AND ob.invItemId = :invItemId"),
		@NamedQuery(name = "OrderBookInvItem.getAllByInventory", query = "SELECT obii FROM OrderBookInvItem obii WHERE obii.archived = 'false' AND LOWER(obii.invType) =:invType GROUP BY obii.bookId"),
		@NamedQuery(name = "OrderBookInvItem.getCountByInventory", query = "SELECT COUNT(obii.id) FROM OrderBookInvItem obii WHERE obii.archived = 'false' AND LOWER(obii.invType) =:invType")

})
public class OrderBookInvItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long bookId; // required
	private Long invItemId; // required
	private String itemDescription; // required
	private Integer orderQty; // required
	private Integer stockQty; // required
	private Double size; // required
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user updated this record.

	private String userNameOrEmail;
	private String invType;

	/**
	 * Transient fields
	 */
	private String obNumber;
	private String customerName;
	private Double listPrice;
	private Double billableCost;
	private Calendar estDate;
	private String comment;
	private String orbf;
	private Double netCost;

	public OrderBookInvItem() {
		super();
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getStockQty() {
		return stockQty;
	}

	public void setStockQty(Integer stockQty) {
		this.stockQty = stockQty;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	@Transient
	public String getUserNameOrEmail() {
		return userNameOrEmail;
	}

	public void setUserNameOrEmail(String userNameOrEmail) {
		this.userNameOrEmail = userNameOrEmail;
	}

	@NotNull
	public Long getInvItemId() {
		return invItemId;
	}

	public void setInvItemId(Long invItemId) {
		this.invItemId = invItemId;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	@Transient
	public String getObNumber() {
		return obNumber;
	}

	public void setObNumber(String obNumber) {
		this.obNumber = obNumber;
	}

	@Transient
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Transient
	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	@Transient
	public Double getBillableCost() {
		return billableCost;
	}

	public void setBillableCost(Double billableCost) {
		this.billableCost = billableCost;
	}

	@Transient
	public Calendar getEstDate() {
		return estDate;
	}

	public void setEstDate(Calendar estDate) {
		this.estDate = estDate;
	}

	@Transient
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public String getOrbf() {
		return orbf;
	}

	public void setOrbf(String orbf) {
		this.orbf = orbf;
	}

	@Transient
	public Double getNetCost() {
		return netCost;
	}

	public void setNetCost(Double netCost) {
		this.netCost = netCost;
	}

}
