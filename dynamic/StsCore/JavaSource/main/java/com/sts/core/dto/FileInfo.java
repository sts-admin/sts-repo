package com.sts.core.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileInfo {
	private Long id;
	private String status;
	private String url;
	private String name;
	private String originalName;
	private String ext;

	/**
	 * 
	 */
	public FileInfo() {
		super();
	}

	/**
	 * @param status
	 * @param originalName
	 * @param name
	 * @param ext
	 */
	public FileInfo(String status, String originalName, String name, String ext) {
		super();
		this.status = status;
		this.originalName = originalName;
		this.name = name;
		this.ext = ext;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @param originalName
	 *            the originalName to set
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
