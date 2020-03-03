package org.woodwhales.maven.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class JarTest {

	@Test
	public void test() throws IOException {
		JarFile jarFile = new JarFile(new File("C:\\Users\\woodwhales\\Desktop\\xstream-1.4.11.jar"));
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
		
		for (String key : properties.stringPropertyNames()) {
            System.out.println(key + "=" + properties.getProperty(key));
        }
		
		String version = (String)properties.get("version");
		String groupId = (String)properties.get("groupId");
		String artifactId = (String)properties.get("artifactId");
		
		assertEquals("1.4.11", version);
		assertEquals("com.thoughtworks.xstream", groupId);
		assertEquals("xstream", artifactId);
		
		jarFile.close();
		
	}
	
}
