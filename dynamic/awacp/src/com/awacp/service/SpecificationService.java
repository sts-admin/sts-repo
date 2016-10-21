package com.awacp.service;

import com.awacp.entity.Spec;
import com.sts.core.dto.StsResponse;

public interface SpecificationService {

	public Spec updateSpecification(Spec spec);

	public Spec saveSpecification(Spec spec);

	public Spec getSpecification(Long architectId);

	public StsResponse<Spec> listSpecifications(int pageNumber, int pageSize);

}
