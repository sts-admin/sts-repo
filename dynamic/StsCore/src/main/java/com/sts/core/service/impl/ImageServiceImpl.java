package com.sts.core.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.sts.core.entity.Image;
import com.sts.core.service.ImageService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.FileUtils;

public class ImageServiceImpl implements ImageService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Image findImage(Long imageId) {
		return getEntityManager().find(Image.class, imageId);
	}

	@Override
	@Transactional
	public Image saveImage(InputStream is, String name, String contentType, String basePath) {

		String generatedName = ConversionUtil.getAlphaNumeric(System.currentTimeMillis());
		String modifiedName = generatedName.substring(2).toUpperCase();
		String fileExt = FileUtils.getExtension(name);
		// custom modified name, original name, and extension
		Image image = new Image(modifiedName, name, fileExt, contentType);
		if (is != null) {
			// upload image to a directory at resourceReadPath
			String imageFileName = "" + modifiedName + fileExt;
			FileUtils.writeBinaryFileContent(basePath, imageFileName, is);
			getEntityManager().persist(image); // persist image
			getEntityManager().flush();
			image.setImageFileName(imageFileName);
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> listImages() {
		Query query = getEntityManager().createNamedQuery("Image.findAll");
		return query.getResultList();
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public Image updateImage(Image image) {
		return getEntityManager().merge(image);
	}
}
