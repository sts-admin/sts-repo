package com.awacp.service;

import com.awacp.entity.MnDType;
import com.sts.core.dto.StsResponse;

public interface MnDTypeService {

	public MnDType updateMnDType(MnDType MnDType);

	public MnDType saveMnDType(MnDType MnDType);

	public MnDType getMnDType(Long architectId);

	public StsResponse<MnDType> listMnDTypes(Long mndId, int pageNumber, int pageSize);

	public String delete(Long id);

	public String setMessage(String message, Long id);

	public String setFlag(boolean flagValue, Long id);

}
