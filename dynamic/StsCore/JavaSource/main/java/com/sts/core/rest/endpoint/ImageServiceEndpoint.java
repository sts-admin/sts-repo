package com.sts.core.rest.endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.entity.Image;
import com.sts.core.service.ImageService;
import com.sts.core.util.FileUtils;
import com.sts.core.web.filter.CrossOriginFilter;

public class ImageServiceEndpoint extends CrossOriginFilter {
	@Autowired
	private ImageService imageService;

	@GET
	@Path("/image/{imageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Image getImage(@PathParam("imageId") Long imageId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.imageService.findImage(imageId);
	}

	@POST
	@Path("/uploadFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Image upload(Attachment attachment) throws IOException {
		DataHandler dataHandler = attachment.getDataHandler();
		MultivaluedMap<String, String> map = attachment.getHeaders();
		String contentType = null;
		if (map != null && !map.isEmpty()) {
			if (map.containsKey("Content-Type")) {
				List<String> types = map.get("Content-Type");
				if (types != null && !types.isEmpty()) {
					contentType = types.get(0);
				}
			}
		}
		String name = FileUtils.getFileName(map);
		InputStream is = dataHandler.getInputStream();
		String baseFolderPath = "";
		return this.imageService.saveImage(is, name, contentType, baseFolderPath);
	}

	@POST
	@Path("/uploadFiles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public List<Image> uploadFiles(List<Attachment> attachments) throws IOException {
		List<Image> fileInfos = new ArrayList<Image>();
		if (attachments == null) {
			return null;
		}
		DataHandler dataHandler = null;
		MultivaluedMap<String, String> map = null;
		String contentType = null;
		for (Attachment attachment : attachments) {
			dataHandler = attachment.getDataHandler();
			map = attachment.getHeaders();
			if (map != null && !map.isEmpty()) {
				if (map.containsKey("Content-Type")) {
					List<String> types = map.get("Content-Type");
					if (types != null && !types.isEmpty()) {
						contentType = types.get(0);
					}
				}
			}
			String name = FileUtils.getFileName(map);
			InputStream is = dataHandler.getInputStream();
			String baseFolderPath = "";
			fileInfos.add(this.imageService.saveImage(is, name, contentType, baseFolderPath));
		}
		return fileInfos;
	}

	@GET
	@Path("/listAllImages")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Image> listAllImages(@Context HttpServletResponse servletResponse) throws IOException {
		return this.imageService.listImages();
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

}
