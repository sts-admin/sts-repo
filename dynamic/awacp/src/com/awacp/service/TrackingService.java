package com.awacp.service;

import com.awacp.entity.ShipmentStatus;
import com.awacp.entity.Tracking;
import com.sts.core.dto.StsResponse;

public interface TrackingService {

	public Tracking updateTracking(Tracking tracking) throws Exception;;

	public Tracking saveTracking(Tracking tracking) throws Exception;;

	public Tracking getTracking(Long id);
	
	public Tracking getTrackingByOrderNumber(Long orderBookId);

	public StsResponse<Tracking> listTrackings(int pageNumber, int pageSize);

	public String delete(Long id);

	public ShipmentStatus trackShipment(String trackingNumber, String ss) throws Exception;

	public void updateShipment(Long trackingId) throws Exception;
	
	public ShipmentStatus shipmentStatus(Long orderBookId);

}
