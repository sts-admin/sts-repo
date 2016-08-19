package com.sts.core.service;

import java.util.List;

import com.sts.core.entity.State;

public interface StateService {
	public List<State> findAll();

	public List<State> findAllByCountry(Long countryId);

	public State findById(Long stateId);

}