package org.woodwhales.maven.controller.vo;

import java.util.Objects;

import org.woodwhales.maven.dto.MavenDependencyInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MavenCommandInfo {
	
	private String groupId;
	private String artifactId;
	private String version;
	private String command;
	
	public static MavenCommandInfo newInstance(MavenDependencyInfo dependencyInfo, String command) {
		Objects.requireNonNull(dependencyInfo, "不允许maven坐标信息为空");
		
		return MavenCommandInfo.builder()
								.artifactId(dependencyInfo.getArtifactId())
								.groupId(dependencyInfo.getGroupId())
								.version(dependencyInfo.getVersion())
								.command(command)
								.build();
	}

}
