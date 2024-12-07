package io.github.ihepda.techdebt.report;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import io.github.ihepda.techdebt.filter.FilterManager;
import io.github.ihepda.techdebt.javaparser.TechDebtResource;
import io.github.ihepda.techdebt.utils.FileNamePatternUtils;

public abstract class AbstractFileReporter implements Reporter {

	public static final String OUTPUT_FILE_LOCATION  = "report.output.dir";
	public static final String MULTIPLE_REPORTS  = "report.multiple";
	public static final String FILENAME_PATTERN  = "report.filename.pattern";
	public static final String FILTER_REPORT  = "report.filter";
	
	private Path outputDirectory;
	private boolean multipleReports;
	private String filenamePattern;
	private FilterManager filterManager;

	protected abstract String getDefaultFileNamePattern();
	
	protected Path getOutputDirectory() {
		return outputDirectory;
	}

	protected void setOutputDirectory(Path outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	protected boolean isMultipleReports() {
		return multipleReports;
	}

	protected void setMultipleReports(boolean multipleReports) {
		this.multipleReports = multipleReports;
	}

	protected String getFilenamePattern() {
		return filenamePattern;
	}

	protected void setFilenamePattern(String filenamePattern) {
		this.filenamePattern = filenamePattern;
	}

	protected String getFileName() {
		String pattern = getFilenamePattern() != null ? getFilenamePattern() : getDefaultFileNamePattern();
		return FileNamePatternUtils.generateFileName(pattern, Collections.emptyMap());
		
	}
        
	
	@Override
	public void init(Properties properties) {
		outputDirectory = Path.of(properties.getProperty(OUTPUT_FILE_LOCATION));
		multipleReports = Boolean.parseBoolean(properties.getProperty(MULTIPLE_REPORTS));
		filenamePattern = properties.getProperty(FILENAME_PATTERN);
		this.filterManager = new FilterManager(properties.getProperty(FILTER_REPORT));
	}

	@Override
	public void report(List<TechDebtResource> resources) {
		if (outputDirectory == null) {
			throw new IllegalStateException("Output directory is not set");
		}
		if (outputDirectory.toFile().exists()) {
			if (!outputDirectory.toFile().isDirectory()) {
				throw new IllegalStateException("Output directory is not a directory");
			}
		} else {
			outputDirectory.toFile().mkdirs();
		}
		for (TechDebtResource techDebtResource : resources) {
			techDebtResource.elements().removeIf(e -> !filterManager.matches(e));
		}
		generateReport(resources);
	}
	
	protected abstract void generateReport(List<TechDebtResource> resources);
}
