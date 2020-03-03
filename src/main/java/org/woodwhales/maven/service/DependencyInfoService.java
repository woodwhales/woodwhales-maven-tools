package org.woodwhales.maven.service;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.woodwhales.maven.dto.MavenDependencyInfo;

import com.thoughtworks.xstream.XStream;

@Service
public class DependencyInfoService {
	
	private XStream xstream;
	
	@PostConstruct
	public void init() {
		xstream = new XStream();
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(new Class[]{MavenDependencyInfo.class});
		// springboot项目中不是使用的默认classloader，必须手动指定类加载器，否则无法类型强转
		xstream.setClassLoader(DependencyInfoService.class.getClassLoader());
		xstream.alias("dependency", MavenDependencyInfo.class);
	}
	
	public MavenDependencyInfo fromXML(String xml) {
		if(StringUtils.isBlank(xml)) {
			return null;
		}
		
		return (MavenDependencyInfo)xstream.fromXML(xml);
	}

}
