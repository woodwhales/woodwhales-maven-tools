package org.woodwhales.maven.controller.params;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenInfoRequestParam {

	@NotBlank(message = "jar 文件路径不允许为空")
	private String jarFilePath;
	
	@NotBlank(message = "maven 坐标信息不允许为空")
	private String mavenInfo;
	
}
