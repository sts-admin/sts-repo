package com.sts.core.util;

import java.util.Comparator;

import com.sts.core.dto.MenuItem;

public class MenuItemComparator implements Comparator<MenuItem> {

	@Override
	public int compare(MenuItem o1, MenuItem o2) {
		return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
	}

}
