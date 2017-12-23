package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: SiteColor
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "SiteColor.findAll", query = "SELECT mi FROM SiteColor mi WHERE mi.archived = 'false' ORDER BY mi.dateCreated DESC")

})
public class SiteColor extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String loginPageBgColor;
	private String loginBoxColor;
	private String topHeaderColor;
	private String menuBarBgColor;
	private String menuItemBgColor;
	private String menuItemBgColorOnMouseOver;
	private String activeMenuItemColor;
	private String takeoffDashboardBoxColor;
	private String takeoffDashboardBoxHoverColor;
	private String takeoffFormBgColor;
	private String takeoffViewSearchRowColor;
	private String takeoffViewHeadingRow;

	public SiteColor() {
		super();
	}

	public String getLoginPageBgColor() {
		return loginPageBgColor == null ? "" : loginPageBgColor;
	}

	public String getLoginBoxColor() {
		return loginBoxColor == null ? "" : loginBoxColor;
	}

	public String getTopHeaderColor() {
		return topHeaderColor == null ? "" : topHeaderColor;
	}

	public String getMenuBarBgColor() {
		return menuBarBgColor == null ? "" : menuBarBgColor;
	}

	public String getMenuItemBgColor() {
		return menuItemBgColor == null ? "" : menuItemBgColor;
	}

	public String getMenuItemBgColorOnMouseOver() {
		return menuItemBgColorOnMouseOver == null ? "" : menuItemBgColorOnMouseOver;
	}

	public String getActiveMenuItemColor() {
		return activeMenuItemColor == null ? "" : activeMenuItemColor;
	}

	public String getTakeoffDashboardBoxColor() {
		return takeoffDashboardBoxColor == null ? "" : takeoffDashboardBoxColor;
	}

	public String getTakeoffDashboardBoxHoverColor() {
		return takeoffDashboardBoxHoverColor == null ? "" : takeoffDashboardBoxHoverColor;
	}

	public String getTakeoffFormBgColor() {
		return takeoffFormBgColor == null ? "" : takeoffFormBgColor;
	}

	public String getTakeoffViewSearchRowColor() {
		return takeoffViewSearchRowColor == null ? "" : takeoffViewSearchRowColor;
	}

	public String getTakeoffViewHeadingRow() {
		return takeoffViewHeadingRow == null ? "" : takeoffViewHeadingRow;
	}

	public void setLoginPageBgColor(String loginPageBgColor) {
		this.loginPageBgColor = loginPageBgColor;
	}

	public void setLoginBoxColor(String loginBoxColor) {
		this.loginBoxColor = loginBoxColor;
	}

	public void setTopHeaderColor(String topHeaderColor) {
		this.topHeaderColor = topHeaderColor;
	}

	public void setMenuBarBgColor(String menuBarBgColor) {
		this.menuBarBgColor = menuBarBgColor;
	}

	public void setMenuItemBgColor(String menuItemBgColor) {
		this.menuItemBgColor = menuItemBgColor;
	}

	public void setMenuItemBgColorOnMouseOver(String menuItemBgColorOnMouseOver) {
		this.menuItemBgColorOnMouseOver = menuItemBgColorOnMouseOver;
	}

	public void setActiveMenuItemColor(String activeMenuItemColor) {
		this.activeMenuItemColor = activeMenuItemColor;
	}

	public void setTakeoffDashboardBoxColor(String takeoffDashboardBoxColor) {
		this.takeoffDashboardBoxColor = takeoffDashboardBoxColor;
	}

	public void setTakeoffDashboardBoxHoverColor(String takeoffDashboardBoxHoverColor) {
		this.takeoffDashboardBoxHoverColor = takeoffDashboardBoxHoverColor;
	}

	public void setTakeoffFormBgColor(String takeoffFormBgColor) {
		this.takeoffFormBgColor = takeoffFormBgColor;
	}

	public void setTakeoffViewSearchRowColor(String takeoffViewSearchRowColor) {
		this.takeoffViewSearchRowColor = takeoffViewSearchRowColor;
	}

	public void setTakeoffViewHeadingRow(String takeoffViewHeadingRow) {
		this.takeoffViewHeadingRow = takeoffViewHeadingRow;
	}

}
