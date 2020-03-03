package org.woodwhales.maven.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.woodwhales.maven.controller.params.MavenInfoRequestParam;
import org.woodwhales.maven.controller.vo.BaseVO;
import org.woodwhales.maven.controller.vo.MavenCommandInfo;
import org.woodwhales.maven.dto.MavenDependencyInfo;
import org.woodwhales.maven.service.CommandService;
import org.woodwhales.maven.service.DependencyInfoService;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GenerateController {
	
	@Autowired
	private DependencyInfoService dependencyInfoService;
	
	@Autowired
	private CommandService commandService;

	@PostMapping("/generate")
	public BaseVO<MavenCommandInfo> generate(@RequestBody @Valid MavenInfoRequestParam requestParam, BindingResult bindingResult) {
		log.debug("generate maven info, mavenInfo = {}", JSON.toJSONString(requestParam));
		
		if(bindingResult.hasErrors()){
			String errorMessage = bindingResult.getFieldError().getDefaultMessage();
			return BaseVO.fail(-1, errorMessage, null);
		}
		
		MavenDependencyInfo dependencyInfo = dependencyInfoService.fromXML(requestParam.getMavenInfo());
		
		String command = commandService.buid(requestParam.getJarFilePath(), dependencyInfo);
		
		MavenCommandInfo commandInfo = MavenCommandInfo.newInstance(dependencyInfo, command);
		
		return BaseVO.success("success", commandInfo);
	}
	
	@PostMapping("/upload")
	public BaseVO<MavenCommandInfo> upload(@RequestParam("file") MultipartFile file, String jarFilePath) {
		
		String originalFilename = file.getOriginalFilename();
		if(!StringUtils.endsWith(originalFilename, ".jar")) {
			return BaseVO.fail(-1, "文件必须为 jar 文件", null);
		}
		
		if(StringUtils.isBlank(jarFilePath)) {
			return BaseVO.fail(-1, "jar 文件路径不允许为空", null);
		}
		
		MavenDependencyInfo dependencyInfo = null;
		try {
			dependencyInfo = unzip(file);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseVO.fail(-1, "jar 文件内容解压失败，请检查 jar 文件内容完整性", null);
		}
		
		String command = commandService.buid(jarFilePath, dependencyInfo);
		
		MavenCommandInfo commandInfo = MavenCommandInfo.newInstance(dependencyInfo, command);
		
		return BaseVO.success("success", commandInfo);
	}

	private MavenDependencyInfo unzip(MultipartFile file) {
		MavenDependencyInfo mavenDependencyInfo = null;
		File tempJarFile = new File(file.getOriginalFilename());
		
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), tempJarFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		try (JarFile jarFile = new JarFile(tempJarFile);) {
			
			Enumeration<JarEntry> entries = jarFile.entries();
			JarEntry targetJarEntry = null;
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) entries.nextElement();
				
				if(StringUtils.endsWith(jarEntry.getName(), "pom.properties")) {
					targetJarEntry = jarEntry;
					break;
				}
			}
			
			InputStream input = jarFile.getInputStream(targetJarEntry);
			
			Properties properties = new Properties();
			
			properties.load(input);
			
			String version = (String)properties.get("version");
			String groupId = (String)properties.get("groupId");
			String artifactId = (String)properties.get("artifactId");
			mavenDependencyInfo = new MavenDependencyInfo(groupId, artifactId, version);
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// 会在本地产生临时文件，用完后需要删除
		if (tempJarFile.exists()) {
			tempJarFile.delete();
		}
		
		return mavenDependencyInfo;
	}
}

