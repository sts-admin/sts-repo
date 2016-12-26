package com.awacp.service;

import com.awacp.entity.Bidder;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface BidderService {
	public StsResponse<Bidder> listBidders(int pageNumber, int pageSize);

	public Bidder getBidder(Long bidderId);

	public Bidder saveBidder(Bidder bidder) throws StsDuplicateException;

	public Bidder updateBidder(Bidder bidder) throws StsDuplicateException;
	
	public String delete(Long id);

}
