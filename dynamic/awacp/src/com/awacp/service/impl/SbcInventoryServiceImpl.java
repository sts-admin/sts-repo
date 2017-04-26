package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.SbcInventory;
import com.awacp.service.SbcInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.FileService;
import com.sts.core.service.impl.CommonServiceImpl;

public class SbcInventoryServiceImpl extends CommonServiceImpl<SbcInventory> implements SbcInventoryService {
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
	public StsResponse<SbcInventory> listSbcInventories(int pageNumber, int pageSize) {
		StsResponse<SbcInventory> results = listAll(pageNumber, pageSize, SbcInventory.class.getSimpleName(),
				getEntityManager());

		initWithDetail(results.getResults());

		return results;
	}

	private void initWithDetail(List<SbcInventory> inventories) {
		if (inventories == null || inventories.isEmpty()) {
			return;
		}
		for (SbcInventory awInventory : inventories) {
			enrichInventory(awInventory);
		}
	}

	private SbcInventory enrichInventory(SbcInventory inv) {
		inv.setImageCount(fileService.getFileCount(StsCoreConstant.INV_SBC_IMAGE_DOC, inv.getId()));

		return inv;
	}

	@Override
	public SbcInventory getSbcInventory(Long id) {
		return getEntityManager().find(SbcInventory.class, id);
	}

	@Override
	@Transactional
	public SbcInventory saveSbcInventory(SbcInventory SbcInventory) {
		getEntityManager().persist(SbcInventory);
		getEntityManager().flush();
		return SbcInventory;
	}

	@Override
	@Transactional
	public SbcInventory updateSbcInventory(SbcInventory sbcInventory) {
		getEntityManager().merge(sbcInventory);
		getEntityManager().flush();
		return sbcInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		SbcInventory entity = getSbcInventory(id);
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
						.append(SbcInventory.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		return getEntityManager().createQuery(sb.toString()).getResultList();
	}
}
