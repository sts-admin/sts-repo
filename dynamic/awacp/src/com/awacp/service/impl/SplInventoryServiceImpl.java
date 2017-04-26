package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.SplInventory;
import com.awacp.service.SplInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.FileService;
import com.sts.core.service.impl.CommonServiceImpl;

public class SplInventoryServiceImpl extends CommonServiceImpl<SplInventory> implements SplInventoryService {
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
	public StsResponse<SplInventory> listSplInventories(int pageNumber, int pageSize) {
		StsResponse<SplInventory> results = listAll(pageNumber, pageSize, SplInventory.class.getSimpleName(),
				getEntityManager());

		initWithDetail(results.getResults());

		return results;
	}

	private void initWithDetail(List<SplInventory> inventories) {
		if (inventories == null || inventories.isEmpty()) {
			return;
		}
		for (SplInventory inventory : inventories) {
			enrichInventory(inventory);
		}
	}

	private SplInventory enrichInventory(SplInventory inv) {
		inv.setImageCount(fileService.getFileCount(StsCoreConstant.INV_SPL_IMAGE_DOC, inv.getId()));

		return inv;
	}

	@Override
	public SplInventory getSplInventory(Long id) {
		return getEntityManager().find(SplInventory.class, id);
	}

	@Override
	@Transactional
	public SplInventory saveSplInventory(SplInventory SplInventory) {
		getEntityManager().persist(SplInventory);
		getEntityManager().flush();
		return SplInventory;
	}

	@Override
	@Transactional
	public SplInventory updateSplInventory(SplInventory SplInventory) {
		getEntityManager().merge(SplInventory);
		getEntityManager().flush();
		return SplInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		SplInventory entity = getSplInventory(id);
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
						.append(SplInventory.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		return getEntityManager().createQuery(sb.toString()).getResultList();
	}
}
