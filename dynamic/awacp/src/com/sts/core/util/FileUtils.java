package com.sts.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.helpers.IOUtils;

import com.sts.core.config.AppPropConfig;
import com.sts.core.exception.StsCoreRuntimeException;

public class FileUtils {

	public static String baseFolderPath = AppPropConfig.resourceReadPath;

	/**
	 * Read file(text) contents as String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getTextFileContent(String directoryPath, String fileName) {
		try {
			return IOUtils.toString(new FileInputStream(new File(directoryPath + fileName)));
		} catch (FileNotFoundException fnfe) {
			throw new StsCoreRuntimeException(fnfe.getMessage());
		} catch (IOException ioe) {
			throw new StsCoreRuntimeException(ioe.getMessage());
		}
	}

	/**
	 * Read file(text) contents as String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getTextFileContent(String fileName) {
		return getTextFileContent(baseFolderPath, fileName);
	}

	/**
	 * Read file(binary) contents as byte array
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] getBinaryFileContent(String filePath) {
		try {
			return IOUtils.readBytesFromStream(new FileInputStream(new File(filePath)));
		} catch (FileNotFoundException fnfe) {
			throw new StsCoreRuntimeException(fnfe.getMessage());
		} catch (IOException ioe) {
			throw new StsCoreRuntimeException(ioe.getMessage());
		}
	}

	/**
	 * Write String contents into a file
	 * 
	 * @param fileName
	 * @param contents
	 * @return
	 */
	public static boolean writeTextFileContent(String fileName, String contents) {
		return writeTextFileContent(baseFolderPath, fileName, contents);
	}

	/**
	 * Write String contents into a file
	 * 
	 * @param folderPath
	 * @param fileName
	 * @param contents
	 * @return
	 */
	public static boolean writeTextFileContent(String folderPath, String fileName, String contents) {
		return FileUtils.writeBinaryFileContent(folderPath, fileName, contents.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Write byte[] contents into a file
	 * 
	 * @param filePath
	 * @param contents
	 * @return
	 */
	public static boolean writeBinaryFileContent(String fileName, byte[] contents) {
		return writeBinaryFileContent(baseFolderPath, fileName, contents);
	}

	/**
	 * Write byte[] contents into a file
	 * 
	 * @param filePath
	 * @param contents
	 * @return
	 */
	public static boolean writeBinaryFileContent(String folder, String fileName, byte[] contents) {
		try {
			InputStream stream = new ByteArrayInputStream(contents);
			// create the directory if doesn't exist
			File dir = new File(folder);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			IOUtils.transferTo(stream, new File(dir, fileName));
			return true;
		} catch (FileNotFoundException fnfe) {
			throw new StsCoreRuntimeException(fnfe.getMessage());
		} catch (IOException ioe) {
			throw new StsCoreRuntimeException(ioe.getMessage());
		}
	}

	public static boolean writeBinaryFileContent(String folder, String fileName, InputStream is) {
		try {
			byte[] contents = new byte[is.available()];
			is.read(contents);
			writeBinaryFileContent(folder, fileName, contents);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(".");
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			// No extension.
			return "";
		}
	}
}
