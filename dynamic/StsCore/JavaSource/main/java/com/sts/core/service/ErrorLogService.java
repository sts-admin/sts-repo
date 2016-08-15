package com.sts.core.service;

import java.util.List;

import com.sts.core.entity.ErrorLog;

public interface ErrorLogService {
	public ErrorLog save(ErrorLog log);

	public void delete(Long errorLogId);

	public Integer deleteAll();

	public ErrorLog getById(Long errorLogId);

	public List<ErrorLog> getAll();
}
