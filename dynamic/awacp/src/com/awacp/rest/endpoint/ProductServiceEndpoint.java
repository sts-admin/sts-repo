package com.awacp.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Product;
import com.awacp.service.ProductService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ProductServiceEndpoint extends CrossOriginFilter {

	@Autowired
	ProductService productService;

	@GET
	@Path("/listProducts/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Product> listProducts(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.productService.listProducts(pageNumber, pageSize);
	}

	@GET
	@Path("/getProduct/{productId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Product getProduct(@PathParam("productId") Long productId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.productService.getProduct(productId);
	}

	@POST
	@Path("/saveProduct")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Product saveProduct(Product product, @Context HttpServletResponse servletResponse) throws Exception {
		return this.productService.saveProduct(product);
	}

	@POST
	@Path("/updateProduct")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Product updateProduct(Product product, @Context HttpServletResponse servletResponse) throws IOException {
		return this.productService.updateProduct(product);
	}

	public void setProductService(ProductService ProductService) {
		this.productService = ProductService;
	}

}