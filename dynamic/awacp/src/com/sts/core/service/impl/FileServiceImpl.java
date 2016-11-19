package com.sts.core.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.sts.core.entity.File;
import com.sts.core.service.FileService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.FileUtils;

public class FileServiceImpl implements FileService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public File findFile(Long FileId) {
		return getEntityManager().find(File.class, FileId);
	}

	@Override
	@Transactional
	public File saveFile(InputStream is, String name, String contentType, String basePath) {

		String generatedName = ConversionUtil.getAlphaNumeric(System.currentTimeMillis());
		String modifiedName = generatedName.substring(2).toUpperCase();
		String fileExt = FileUtils.getExtension(name);
		// custom modified name, original name, and extension
		File file = new File(modifiedName, name, fileExt, contentType);
		if (is != null) {
			// upload File to a directory at resourceReadPath
			String FileFileName = "" + modifiedName + fileExt;
			FileUtils.writeBinaryFileContent(basePath, FileFileName, is);
			getEntityManager().persist(file); // persist File
			getEntityManager().flush();
			file.setFileName(FileFileName);
		}
		return file;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<File> listFiles() {
		Query query = getEntityManager().createNamedQuery("File.findAll");
		return query.getResultList();
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public File updateFile(File file) {
		return getEntityManager().merge(file);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<File> listFiles(String fileSource) {
		return getEntityManager().createNamedQuery("File.findAllBySource").setParameter("fileSource", fileSource)
				.getResultList();
	}
}
