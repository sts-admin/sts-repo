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
		@NamedQuery(name = "File.findAll", query = "SELECT file FROM File file WHERE file.archived = false ORDER BY file.id ASC"),
		@NamedQuery(name = "File.findAllBySource", query = "SELECT file FROM File file WHERE file.archived = false AND file.fileSource =:fileSource ORDER BY file.id ASC"),
		@NamedQuery(name = "File.findAllBySourceAndSourceId", query = "SELECT file FROM File file WHERE file.archived = false AND file.fileSource =:fileSource AND file.fileSourceId =:fileSourceId ORDER BY file.id ASC"),
		@NamedQuery(name = "File.getCountBySource", query = "SELECT COUNT(file) FROM File file WHERE file.archived = false AND LOWER(file.fileSource) =:fileSource AND file.fileSourceId =:fileSourceId ORDER BY file.id ASC")
})
@XmlRootElement
public class File extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String createdName;
	private String originalName;
	private String extension;
	private String size;
	private String other;
	private String contentType;
	private String fileSource;
	private Long fileSourceId;

	// Transient
	private String fileName;
	private String userCode;

	public File() {
		super();
	}

	/**
	 * @param createdName
	 * @param originalName
	 * @param extension
	 */
	public File(String createdName, String originalName, String extension, String contentType) {
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
	@Column(nullable = false, length = 6)
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
	@Column(nullable = false, length = 150)
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

	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public Long getFileSourceId() {
		return fileSourceId;
	}

	public void setFileSourceId(Long fileSourceId) {
		this.fileSourceId = fileSourceId;
	}

	@Transient
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
