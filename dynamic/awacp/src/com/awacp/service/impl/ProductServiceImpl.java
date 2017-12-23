package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Product;
import com.awacp.service.ProductService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class ProductServiceImpl extends CommonServiceImpl<Product> implements ProductService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Product> listProducts(int pageNumber, int pageSize) {
		StsResponse<Product> results = listAll(pageNumber, pageSize, Product.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public Product getProduct(Long id) {
		return getEntityManager().find(Product.class, id);
	}

	@Override
	@Transactional
	public Product saveProduct(Product Product) throws StsDuplicateException {
		if (isExistsByName(Product.getProductName(), "productName", Product.getClass().getSimpleName(),
				getEntityManager())) {
			throw new StsDuplicateException("duplicate_name");
		}
		getEntityManager().persist(Product);
		getEntityManager().flush();
		return Product;
	}

	@Override
	@Transactional
	public Product updateProduct(Product Product) {
		Product = getEntityManager().merge(Product);
		getEntityManager().flush();
		return Product;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> filter(String keyword) {
		if (keyword == null || keyword.isEmpty())
			return null;
		return getEntityManager().createNamedQuery("Product.filterByNameMatch")
				.setParameter("keyword", "%" + keyword.toLowerCase() + "%").getResultList();
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Product entity = getProduct(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

}
