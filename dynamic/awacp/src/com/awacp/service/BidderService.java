package com.awacp.service;

import java.util.List;

import com.awacp.entity.Bidder;

public interface BidderService {

	public List<Bidder> listBidders();

	public Bidder getBidder(Long bidderId);

	public Bidder saveBidder(Bidder bidder);

	public Bidder updateBidder(Bidder bidder);

}
