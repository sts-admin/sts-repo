package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwfInventory;
import com.awacp.service.AwfInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.FileService;
import com.sts.core.service.impl.CommonServiceImpl;

public class AwfInventoryServiceImpl extends CommonServiceImpl<AwfInventory> implements AwfInventoryService {
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
	public StsResponse<AwfInventory> listAwfInventories(int pageNumber, int pageSize) {
		StsResponse<AwfInventory> results = listAll(pageNumber, pageSize, AwfInventory.class.getSimpleName(),
				getEntityManager());
		initWithDetail(results.getResults());

		return results;
	}

	private void initWithDetail(List<AwfInventory> inventories) {
		if (inventories == null || inventories.isEmpty()) {
			return;
		}
		for (AwfInventory awfInventory : inventories) {
			enrichInventory(awfInventory);
		}
	}

	private AwfInventory enrichInventory(AwfInventory inv) {
		inv.setImageCount(fileService.getFileCount(StsCoreConstant.INV_AWF_IMAGE_DOC, inv.getId()));

		return inv;
	}

	@Override
	public AwfInventory getAwfInventory(Long id) {
		return getEntityManager().find(AwfInventory.class, id);
	}

	@Override
	@Transactional
	public AwfInventory saveAwfInventory(AwfInventory AwfInventory) {
		getEntityManager().persist(AwfInventory);
		getEntityManager().flush();
		return AwfInventory;
	}

	@Override
	@Transactional
	public AwfInventory updateAwfInventory(AwfInventory AwfInventory) {
		getEntityManager().merge(AwfInventory);
		getEntityManager().flush();
		return AwfInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		AwfInventory entity = getAwfInventory(id);
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
						.append(AwfInventory.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		return getEntityManager().createQuery(sb.toString()).getResultList();
	}
}
