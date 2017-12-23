package com.awacp.service;

import com.awacp.entity.Factory;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface FactoryService {

	public Factory saveFactory(Factory Factory) throws StsDuplicateException;

	public Factory getFactory(Long factoryId);

	public StsResponse<Factory> listFactories(int pageNumber, int pageSize);

	public String delete(Long id);

}