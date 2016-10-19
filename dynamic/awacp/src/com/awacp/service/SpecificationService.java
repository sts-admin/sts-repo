package com.awacp.service;

import com.awacp.entity.Specification;
import com.sts.core.dto.StsResponse;

public interface SpecificationService {

	public Specification updateSpecification(Specification specification);

	public Specification saveSpecification(Specification specification);

	public Specification getSpecification(Long architectId);

	public StsResponse<Specification> listSpecifications(int pageNumber, int pageSize);

}
