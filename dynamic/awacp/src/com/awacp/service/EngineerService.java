package com.awacp.service;

import java.util.List;

import com.awacp.entity.Engineer;

public interface EngineerService {

	public List<Engineer> listEngineers();

	public Engineer getEngineer(Long engineerId);

	public com.awacp.entity.Engineer saveEngineer(Engineer engineer);

	public com.awacp.entity.Engineer updateEngineer(Engineer engineer);

}
