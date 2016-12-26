package com.awacp.service;

import com.awacp.entity.MnD;
import com.sts.core.dto.StsResponse;

public interface MnDService {

	public MnD updateMnD(MnD MnD);

	public MnD saveMnD(MnD MnD);

	public MnD getMnD(Long architectId);

	public StsResponse<MnD> listMnDs(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
