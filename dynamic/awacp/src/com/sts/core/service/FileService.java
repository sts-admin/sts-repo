package com.sts.core.service;

import java.io.InputStream;
import java.util.List;

import com.sts.core.entity.File;

public interface FileService {

	public File findFile(Long fileId);

	public File saveFile(InputStream is, String name, String contentType, String basePath);

	public File saveFile(String name, String contentType, String basePath, byte[] contents);

	public List<File> listFiles();

	public List<File> listFiles(String fileSource);

	public List<File> listFiles(String source, Long sourceId);

	public File updateFile(File file);

	public File updateFileSource(Long userId, String fileSource, Long fileSourceId, Long fileId);

}
