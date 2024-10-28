package io.github.ihepda.techdebt.javaparser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.ihepda.techdebt.utils.ConsoleLogger;

class SourcesNavigatorTest {

	@Test
	void test() throws IOException {
		SourcesNavigator sourcesNavigator = new SourcesNavigator();	
		sourcesNavigator.setInternalLogger(new ConsoleLogger());
		List<TechDebtResource> navigate = sourcesNavigator.navigate(Paths.get("./src/testcases"));
		assertNotNull(navigate);
		assertEquals(2, navigate.size());
		System.out.println(navigate);
	}

}
