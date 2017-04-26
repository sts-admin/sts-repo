package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwInventory;
import com.awacp.service.AwInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.FileService;
import com.sts.core.service.impl.CommonServiceImpl;

public class AwInventoryServiceImpl extends CommonServiceImpl<AwInventory> implements AwInventoryService {
	private EntityManager entityManager;
	
	@Autowired
	private FileService fileService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<AwInventory> listAwInventories(int pageNumber, int pageSize) {
		StsResponse<AwInventory> results = listAll(pageNumber, pageSize, AwInventory.class.getSimpleName(),
				getEntityManager());
		initWithDetail(results.getResults());

		return results;
	}

	private void initWithDetail(List<AwInventory> inventories) {
		if (inventories == null || inventories.isEmpty()) {
			return;
		}
		for (AwInventory awInventory : inventories) {
			enrichInventory(awInventory);
		}
	}

	private AwInventory enrichInventory(AwInventory inv) {
		inv.setImageCount(fileService.getFileCount(StsCoreConstant.INV_AW_IMAGE_DOC, inv.getId()));

		return inv;
	}

	@Override
	public AwInventory getAwInventory(Long id) {
		return getEntityManager().find(AwInventory.class, id);
	}

	@Override
	@Transactional
	public AwInventory saveAwInventory(AwInventory AwInventory) {
		getEntityManager().persist(AwInventory);
		getEntityManager().flush();
		return AwInventory;
	}

	@Override
	@Transactional
	public AwInventory updateAwInventory(AwInventory AwInventory) {
		getEntityManager().merge(AwInventory);
		getEntityManager().flush();
		return AwInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		AwInventory entity = getAwInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryDTO> listInvItems() {
		StringBuffer sb = new StringBuffer(
				"SELECT NEW com.sts.core.dto.InventoryDTO(entity.id, entity.item, entity.quantity, entity.size) FROM ")
						.append(AwInventory.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		return getEntityManager().createQuery(sb.toString()).getResultList();
	}
}
