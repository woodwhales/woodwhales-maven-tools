package org.woodwhales.maven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenDependencyInfo {
	
	private String groupId;
	private String artifactId;
	private String version;

}
