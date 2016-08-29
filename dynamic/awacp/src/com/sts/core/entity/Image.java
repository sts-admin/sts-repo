package com.sts.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: MerchantUser
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Image.findAll", query = "Select i from Image i where i.archived = false ORDER BY i.id ASC") })
@XmlRootElement
public class Image extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String createdName;
	private String originalName;
	private String extension;
	private String size;
	private String other;
	private String contentType;

	// Transient
	private String imageFileName;

	public Image() {
		super();
	}

	/**
	 * @param createdName
	 * @param originalName
	 * @param extension
	 */
	public Image(String createdName, String originalName, String extension, String contentType) {
		super();
		this.createdName = createdName;
		this.originalName = originalName;
		this.extension = extension;
		this.contentType = contentType;
	}

	@NotNull
	@Column(nullable = false, length = 25)
	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	@NotNull
	@Column(nullable = false, length = 4)
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the contentType
	 */
	@NotNull
	@Column(nullable = false, length = 50)
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Transient
	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

}
