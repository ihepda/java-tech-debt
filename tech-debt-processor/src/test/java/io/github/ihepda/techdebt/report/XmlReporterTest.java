package io.github.ihepda.techdebt.report;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.ihepda.techdebt.javaparser.SourcesNavigator;
import io.github.ihepda.techdebt.javaparser.TechDebtResource;
import io.github.ihepda.techdebt.utils.ConsoleLogger;

class XmlReporterTest {

	@Test
	void test() throws IOException {
		XmlReporter xmlReporter = new XmlReporter();
		Properties properties = new Properties();
		properties.setProperty(AbstractFileReporter.OUTPUT_FILE_LOCATION, "./target/techdebts");
		xmlReporter.init(properties);
		SourcesNavigator sourcesNavigator = new SourcesNavigator();	
		sourcesNavigator.setInternalLogger(new ConsoleLogger());
		List<TechDebtResource> navigate = sourcesNavigator.navigate(Paths.get("./src/testcases"));
		xmlReporter.report(navigate);
		assertNotNull(xmlReporter);
	}

}
