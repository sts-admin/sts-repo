package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.JInventory;
import com.awacp.service.JInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.FileService;
import com.sts.core.service.impl.CommonServiceImpl;

public class JInventoryServiceImpl extends CommonServiceImpl<JInventory> implements JInventoryService {
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
	public StsResponse<JInventory> listJInventories(int pageNumber, int pageSize) {
		StsResponse<JInventory> results = listAll(pageNumber, pageSize, JInventory.class.getSimpleName(),
				getEntityManager());
		initWithDetail(results.getResults());

		return results;
	}

	private void initWithDetail(List<JInventory> inventories) {
		if (inventories == null || inventories.isEmpty()) {
			return;
		}
		for (JInventory awInventory : inventories) {
			enrichInventory(awInventory);
		}
	}

	private JInventory enrichInventory(JInventory inv) {
		inv.setImageCount(fileService.getFileCount(StsCoreConstant.INV_J_IMAGE_DOC, inv.getId()));

		return inv;
	}

	@Override
	public JInventory getJInventory(Long id) {
		return getEntityManager().find(JInventory.class, id);
	}

	@Override
	@Transactional
	public JInventory saveJInventory(JInventory JInventory) {
		getEntityManager().persist(JInventory);
		getEntityManager().flush();
		return JInventory;
	}

	@Override
	@Transactional
	public JInventory updateJInventory(JInventory JInventory) {
		getEntityManager().merge(JInventory);
		getEntityManager().flush();
		return JInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		JInventory entity = getJInventory(id);
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
						.append(JInventory.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");

		return getEntityManager().createQuery(sb.toString()).getResultList();
	}
}
