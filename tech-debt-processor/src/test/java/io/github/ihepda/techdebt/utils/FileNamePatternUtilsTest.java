package io.github.ihepda.techdebt.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class FileNamePatternUtilsTest {

	@Test
	void test() {
		
		String fileName = FileNamePatternUtils.generateFileName("MyTest.xml", Collections.emptyMap());
		assertEquals("MyTest.xml", fileName);
		fileName = FileNamePatternUtils.generateFileName("MyTest_{name}.xml", Collections.singletonMap("name", "CDA"));
		assertEquals("MyTest_CDA.xml", fileName);
		fileName = FileNamePatternUtils.generateFileName("MyTest_{date:yyyy}.xml", Collections.singletonMap("name", "CDA"));
		LocalDateTime now = LocalDateTime.now();
		assertEquals("MyTest_"+now.format(DateTimeFormatter.ofPattern("yyyy"))+".xml", fileName);
		fileName = FileNamePatternUtils.generateFileName("MyTest_{date:yyyyMMdd}.xml", Collections.singletonMap("name", "CDA"));
		now = LocalDateTime.now();
		assertEquals("MyTest_"+now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))+".xml", fileName);
	}

}
