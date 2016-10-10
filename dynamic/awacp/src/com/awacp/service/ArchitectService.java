package com.awacp.service;

import java.util.List;

import com.awacp.entity.Architect;

public interface ArchitectService {

	public Architect updateArchitect(Architect architect);

	public Architect saveArchitect(Architect architect);

	public Architect getArchitect(Long architectId);

	public List<Architect> listArchitects();

}
