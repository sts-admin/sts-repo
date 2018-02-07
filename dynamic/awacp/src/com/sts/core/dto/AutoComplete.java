package com.sts.core.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AutoComplete {
	private List<String> result = new ArrayList<String>();

	/**
	 * 
	 */
	public AutoComplete() {
		super();
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

}
