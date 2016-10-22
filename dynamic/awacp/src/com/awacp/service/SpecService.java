package com.awacp.service;

import com.awacp.entity.Spec;
import com.sts.core.dto.StsResponse;

public interface SpecService {

	public Spec updateSpec(Spec spec);

	public Spec saveSpec(Spec spec);

	public Spec getSpec(Long architectId);

	public StsResponse<Spec> listSpecs(int pageNumber, int pageSize);

}
