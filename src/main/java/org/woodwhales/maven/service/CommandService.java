package org.woodwhales.maven.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.woodwhales.maven.dto.MavenDependencyInfo;

import java.util.Objects;

@Service
public class CommandService {
	
	private String command = "mvn install:install-file -Dfile=\"%s\" -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=jar";

	public String buid(String jarFilePath, MavenDependencyInfo dependencyInfo) {
		Objects.requireNonNull(dependencyInfo, "不允许maven坐标信息为空");
		
		if(StringUtils.isBlank(jarFilePath)) {
			return StringUtils.EMPTY;
		}
		
		return String.format(command, jarFilePath, dependencyInfo.getGroupId(), dependencyInfo.getArtifactId(), dependencyInfo.getVersion());
	}

}
