package com.awacp.service;

import com.awacp.entity.Bidder;
import com.sts.core.dto.StsResponse;

public interface BidderService {
	public StsResponse<Bidder> listBidders(int pageNumber, int pageSize);

	public Bidder getBidder(Long bidderId);

	public Bidder saveBidder(Bidder bidder);

	public Bidder updateBidder(Bidder bidder);

}
