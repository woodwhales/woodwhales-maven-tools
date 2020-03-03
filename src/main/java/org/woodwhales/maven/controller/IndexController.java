package org.woodwhales.maven.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	
	@GetMapping({"", "/", "index"})
	public String index() {
		return "index";
	}
	
	public static void main(String[] args) {
		getZipFileContent("C:\\Users\\woodwhales\\Desktop\\eureka-client-controller-0.0.2-RELEASE.jar");
	}

	public static List<String> getZipFileContent(String zipFile) {
		if (StringUtils.isEmpty(zipFile)) {
			return null;
		}

		List<String> results = null;
		try {
			results = unZip(new FileInputStream(new File(zipFile)));
		} catch (FileNotFoundException e) {
			log.warn("unzip process is filed! cause by : {}", e.getMessage());
			e.printStackTrace();
		}
		return results;
	}

	private static List<String> unZip(InputStream zipFile) {
		if (zipFile == null) {
			return null;
		}
		// get the zip file content
		List<String> lists = null;
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(zipFile);
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			log.debug("file size = {}", ze.getSize());
			lists = new ArrayList<>();
			while (ze != null) {
				log.debug("file name = {}", ze.getName());
				byte[] byteArray = IOUtils.toByteArray(zis);
				lists.add(new String(byteArray, StandardCharsets.UTF_8));
				ze = zis.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zis != null) {
				try {
					zis.closeEntry();
					zis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lists;
	}

}
