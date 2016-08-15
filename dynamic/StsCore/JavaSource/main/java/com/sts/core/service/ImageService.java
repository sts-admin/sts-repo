package com.sts.core.service;

import java.io.InputStream;
import java.util.List;

import com.sts.core.entity.Image;

public interface ImageService {

	public Image findImage(Long imageId);
	public Image saveImage(InputStream is, String name, String contentType, String basePath);
	public List<Image> listImages();
	public Image updateImage(Image image);
	
}
