package io.github.ihepda.techdebt.javaparser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.github.ihepda.techdebt.utils.ConsoleLogger;

class SourcesNavigatorTest {

	@Test
	void test() throws IOException {
		SourcesNavigator sourcesNavigator = new SourcesNavigator();	
		sourcesNavigator.setInternalLogger(new ConsoleLogger());
		sourcesNavigator.navigate(Paths.get("./src/testcases"));
	}

}
