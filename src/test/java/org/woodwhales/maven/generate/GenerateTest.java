package org.woodwhales.maven.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.woodwhales.maven.dto.MavenDependencyInfo;

import com.thoughtworks.xstream.XStream;

public class GenerateTest {

	private String xml = "<!-- https://mvnrepository.com/artifact/com.google.api/gax -->\n<dependency>\n    <groupId>com.google.api</groupId>\n    <artifactId>gax</artifactId>\n    <version>1.54.0</version>\n</dependency>";
	
	private static XStream xstream;
	
	@BeforeAll
	public static void Init() {
		xstream = new XStream();
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(new Class[]{MavenDependencyInfo.class});
		xstream.alias("dependency", MavenDependencyInfo.class);
	}
	
	@Test
	public void test() {
		MavenDependencyInfo mavenInfo = (MavenDependencyInfo)xstream.fromXML(xml);
		assertEquals("gax", mavenInfo.getArtifactId());
		assertEquals("com.google.api", mavenInfo.getGroupId());
		assertEquals("1.54.0", mavenInfo.getVersion());
	}
}
