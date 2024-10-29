package io.github.ihepda.techdebt.report;

import java.util.List;
import java.util.Properties;

import io.github.ihepda.techdebt.javaparser.TechDebtResource;

public interface Reporter {

	void init(Properties properties);
	void report(List<TechDebtResource> resources);
}
