package com.sts.core.service;

import java.io.InputStream;
import java.util.List;

import com.sts.core.entity.File;

public interface FileService {

	public File findFile(Long fileId);

	public File saveFile(InputStream is, String name, String contentType, String basePath);

	public List<File> listFiles();
	
	public List<File> listFiles(String fileSource);

	public File updateFile(File file);

}
