package com.sts.core.service;

import java.util.List;

import com.sts.core.entity.Country;

public interface CountryService {
	public List<Country> findAll();

	public Country findById(Long countryId);

}