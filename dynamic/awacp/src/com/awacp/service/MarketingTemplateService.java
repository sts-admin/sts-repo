package com.awacp.service;

import com.awacp.entity.MarketingTemplate;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface MarketingTemplateService {

	public MarketingTemplate updateMarketingTemplate(MarketingTemplate MarketingTemplate) throws StsDuplicateException;

	public MarketingTemplate saveMarketingTemplate(MarketingTemplate MarketingTemplate) throws StsDuplicateException;

	public MarketingTemplate getMarketingTemplate(Long marketingTemplateId);

	public StsResponse<MarketingTemplate> listMarketingTemplates(int pageNumber, int pageSize);

	public String delete(Long id);

}
