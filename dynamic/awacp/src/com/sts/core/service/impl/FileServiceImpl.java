package com.sts.core.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.Takeoff;
import com.sts.core.entity.File;
import com.sts.core.entity.User;
import com.sts.core.service.FileService;
import com.sts.core.service.UserService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.FileUtils;

public class FileServiceImpl implements FileService {

	private EntityManager entityManager;

	@Autowired
	UserService userService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public File findFile(Long fileId) {
		return getEntityManager().find(File.class, fileId);
	}

	@Override
	@Transactional
	public File saveFile(String name, String contentType, String basePath, byte[] contents) {
		String generatedName = ConversionUtil.getAlphaNumeric(System.currentTimeMillis());
		String modifiedName = generatedName.substring(2).toUpperCase();
		String fileExt = FileUtils.getExtension(name);
		// custom modified name, original name, and extension
		File file = new File(modifiedName, name, fileExt, contentType);
		if (contents != null) {
			// upload File to a directory at resourceReadPath
			String fileName = "" + modifiedName + fileExt;
			FileUtils.writeBinaryFileContent(basePath, fileName, contents);
			getEntityManager().persist(file); // persist File
			getEntityManager().flush();
			file.setFileName(fileName);
		}
		return file;
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
			String fileName = "" + modifiedName + fileExt;
			FileUtils.writeBinaryFileContent(basePath, fileName, is);
			getEntityManager().persist(file); // persist File
			getEntityManager().flush();
			file.setFileName(fileName);
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
		List<File> files = getEntityManager().createNamedQuery("File.findAllBySource")
				.setParameter("fileSource", fileSource).getResultList();
		if (files != null && !files.isEmpty()) {
			User user = null;
			for (File file : files) {
				file.setUserCode("---");
				user = userService.findUser(file.getCreatedById());
				if (user != null) {
					file.setUserCode(user.getUserCode());
				}
			}
		}
		return files;
	}

	@Override
	@Transactional
	public File updateFileSource(Long userId, String fileSource, Long fileSourceId, Long fileId) {
		File file = findFile(fileId);
		if (file != null) {
			file.setCreatedById(userId);
			file.setFileSource(fileSource);
			file.setFileSourceId(fileSourceId);
			getEntityManager().merge(file);
			return file;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<File> listFiles(String fileSource, Long fileSourceId) {
		List<File> files = getEntityManager().createNamedQuery("File.findAllBySourceAndSourceId")
				.setParameter("fileSource", fileSource).setParameter("fileSourceId", fileSourceId).getResultList();
		if (files != null && !files.isEmpty()) {
			User user = null;
			for (File file : files) {
				file.setUserCode("---");
				if (file.getCreatedById() == null) {
					continue;
				}
				user = userService.findUser(file.getCreatedById());
				if (user != null) {
					file.setUserCode(user.getUserCode());
				}
			}
		}
		return files;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getFileCount(String source, Long sourceId) {
		List objects = getEntityManager().createNamedQuery("File.getCountBySource")
				.setParameter("fileSource", source.toLowerCase()).setParameter("fileSourceId", sourceId)
				.getResultList();
		if (objects != null && !objects.isEmpty()) {
			Object o = objects.get(0);
			if (o != null)
				return ((Long) o).intValue();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<File> listFilesBySourceMatchAndId(String fileSource, String sourceId) {
		Long fileSourceId = null;
		if (fileSource.equalsIgnoreCase("takeoff")) {
			List<Takeoff> results = getEntityManager().createNamedQuery("Takeoff.getTakeoffByTakeoffId")
					.setParameter("takeoffId", sourceId).getResultList();
			if (results != null && !results.isEmpty()) {
				fileSourceId = results.get(0).getId();
			}
		}else if (fileSource.equalsIgnoreCase("quote")) {
			List<Takeoff> results = getEntityManager().createNamedQuery("Takeoff.getTakeoffByQuoteId")
					.setParameter("quoteId", sourceId).getResultList();
			if (results != null && !results.isEmpty()) {
				fileSourceId = results.get(0).getId();
			}
		}else if (fileSource.equalsIgnoreCase("joborder")) {
			List<JobOrder> results = getEntityManager().createNamedQuery("JobOrder.getByOrderId")
					.setParameter("orderNumber", sourceId.toLowerCase()).getResultList();
			if (results != null && !results.isEmpty()) {
				fileSource = "jo";
				fileSourceId = results.get(0).getId();
			}
		}else if (fileSource.equalsIgnoreCase("orderbook")) {
			List<OrderBook> results = getEntityManager().createNamedQuery("OrderBook.getByNumber")
					.setParameter("orderBookNumber", sourceId).getResultList();
			if (results != null && !results.isEmpty()) {
				fileSource = "ob";
				fileSourceId = results.get(0).getId();
			}
		}
		if (fileSourceId == null) {
			return null;
		}
		return getEntityManager().createNamedQuery("File.findAllBySourceMatchAndId")
				.setParameter("keyword", "" + fileSource.toLowerCase() + "%").setParameter("fileSourceId", fileSourceId)
				.getResultList();
	}

	@Transactional
	@Override
	public String archiveFile(Long fileId) {
		File file = getEntityManager().find(File.class, fileId);
		if (file != null) {
			file.setArchived(true);
			getEntityManager().merge(file);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
