package com.awacp.service;

import java.util.List;

import com.awacp.entity.Product;
import com.sts.core.dto.StsResponse;

public interface ProductService {

	public Product updateProduct(Product Product);

	public Product saveProduct(Product Product);

	public Product getProduct(Long productId);

	public StsResponse<Product> listProducts(int pageNumber, int pageSize);

	public List<Product> filter(String keyword); // match name

}
