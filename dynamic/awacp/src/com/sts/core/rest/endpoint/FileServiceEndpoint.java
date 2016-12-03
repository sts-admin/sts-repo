package com.sts.core.rest.endpoint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.config.AppPropConfig;
import com.sts.core.entity.File;
import com.sts.core.service.FileService;
import com.sts.core.util.FileUtils;
import com.sts.core.web.filter.CrossOriginFilter;

public class FileServiceEndpoint extends CrossOriginFilter {
	@Autowired
	private FileService fileService;

	@GET
	@Path("/file/{fileId}")
	@Produces(MediaType.APPLICATION_JSON)
	public File getFile(@PathParam("fileId") Long fileId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.fileService.findFile(fileId);
	}

	@GET
	@Path("/updateFileSource")
	@Produces(MediaType.APPLICATION_JSON)
	public File updateFileSource(@QueryParam("fileSource") String fileSource,
			@QueryParam("fileSourceId") Long fileSourceId, @QueryParam("fileId") Long fileId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.fileService.updateFileSource(fileSource, fileSourceId, fileId);
	}

	@POST
	@Path("/uploadFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public File upload(Attachment attachment) throws IOException {
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
		String baseFolderPath = AppPropConfig.resourceWritePath;
		return this.fileService.saveFile(is, name, contentType, baseFolderPath);
	}

	@POST
	@Path("/uploadFiles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public List<File> uploadFiles(List<Attachment> attachments) throws IOException {
		List<File> fileInfos = new ArrayList<File>();
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
			fileInfos.add(this.fileService.saveFile(is, name, contentType, baseFolderPath));
		}
		return fileInfos;
	}

	@GET
	@Path("/listAllFiles")
	@Produces(MediaType.APPLICATION_JSON)
	public List<File> listAllFiles(@Context HttpServletResponse servletResponse) throws IOException {
		return this.fileService.listFiles();
	}

	@GET
	@Path("/listAllFilesBySource/{source}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<File> listAllFilesBySource(@PathParam("source") String fileSource,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.fileService.listFiles(fileSource);
	}

	@GET
	@Path("/listFilesBySource/{source}/{sourceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<File> listAllFilesBySource(@PathParam("source") String source, @PathParam("sourceId") Long sourceId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.fileService.listFiles(source, sourceId);
	}

	@GET
	@Path("/downloadFile/{fileId}")
	public void downloadFile(@PathParam("fileId") Long fileId, @Context HttpServletResponse response)
			throws IOException {
		System.err.println("fileId = "+ fileId);
		File awacpFile = fileService.findFile(fileId);
		String fileName = awacpFile.getCreatedName() + awacpFile.getExtension();

		java.io.File file = new java.io.File(AppPropConfig.resourceWritePath + fileName);
		try (InputStream fileInputStream = new FileInputStream(file);
				OutputStream output = response.getOutputStream();) {
			response.setContentType("application/x-download");
			response.setContentLength((int) (file.length()));
			if(file != null){
				System.err.println("file name = "+ file.getName());
			}
			
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			IOUtils.copy(fileInputStream, output);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

}
