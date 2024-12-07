package io.github.ihepda.techdebt.maven.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.javaparser.SourcesNavigator;
import io.github.ihepda.techdebt.javaparser.TechDebtResource;
import io.github.ihepda.techdebt.report.AbstractFileReporter;
import io.github.ihepda.techdebt.report.XmlReporter;
import io.github.ihepda.techdebt.utils.FileNamePatternUtils;

@Mojo(name = "report"
, defaultPhase = LifecyclePhase.SITE
, requiresDependencyResolution = ResolutionScope.RUNTIME
, requiresProject = true, threadSafe = true)
public class TechDebtReportMojo extends AbstractMavenReport {

	/**
	 * The filename pattern for the report.
	 */
	@Parameter(property = "report.filename.pattern", defaultValue = "techdebts-{date:yyyyMMdd}.xml", required = true)
	private String filenamePattern;

	@Parameter(property = "source.dir", defaultValue = "${project.build.sourceDirectory}", required = true)
	private List<String> sources;

	@Parameter(property = "report.filter", required = false)
	private String filter;
	
	@Override
	public String getOutputName() {
		return FileNamePatternUtils.generateFileName(filenamePattern, Collections.emptyMap());
	}

	@Override
	public String getName(Locale locale) {
		return "Technical Debts Report";
	}

	@Override
	public String getDescription(Locale locale) {
		return "A report of technical debts in the project";
	}

	private void buildHtml(List<TechDebtResource> tds) {
		Sink mainSink = getSink();
		if (mainSink == null) {
			return;
		}

		mainSink.head();
		mainSink.title();
		mainSink.text("Technical Debt Report for " + project.getName() + " " + project.getVersion());
		mainSink.title_();
		mainSink.head_();

		mainSink.body();

		for (TechDebtResource techDebtResource : tds) {
			if (techDebtResource.elements().isEmpty()) {
				continue;
			}
			mainSink.section1();
			mainSink.sectionTitle1();
			mainSink.text(techDebtResource.resourceName());
			mainSink.sectionTitle1_();
			mainSink.sectionTitle2();
			mainSink.text(techDebtResource.path().toString());
			mainSink.sectionTitle2_();

			List<TechDebtElement> elements = techDebtResource.elements();

			mainSink.table();
			//header
			mainSink.tableRow();
			mainSink.tableHeaderCell();
			mainSink.text("Element At");
			mainSink.tableHeaderCell_();
			mainSink.tableHeaderCell();
			mainSink.text("Severity");
			mainSink.tableHeaderCell_();
			mainSink.tableHeaderCell();
			mainSink.text("Type");
			mainSink.tableHeaderCell_();
			mainSink.tableHeaderCell();
			mainSink.text("Effort");
			mainSink.tableHeaderCell_();
			mainSink.tableHeaderCell();
			mainSink.text("line");
			mainSink.tableHeaderCell_();
			mainSink.tableHeaderCell();
			mainSink.text("comment");
			mainSink.tableHeaderCell_();
			mainSink.tableRow_();
			for (TechDebtElement element : elements) {
				mainSink.tableRow();
				mainSink.tableCell();
				mainSink.text(element.getElementAt());
				mainSink.tableCell_();
				mainSink.tableCell();
				mainSink.text(element.getSeverity().name());
				mainSink.tableCell_();
				mainSink.tableCell();
				mainSink.text(element.getType().name());
				mainSink.tableCell_();
				mainSink.tableCell();
				mainSink.text(element.getEffort().name());
				mainSink.tableCell_();
				mainSink.tableCell();
				mainSink.text(String.valueOf(element.getLineNumber()));
				mainSink.tableCell_();
				mainSink.tableCell();
				mainSink.text(element.getComment());
				mainSink.tableCell_();
				mainSink.tableRow_();
				
			}
			mainSink.table_();

			mainSink.section1_();
		}
		mainSink.body_();
	}

	private List<TechDebtResource> order(List<TechDebtResource> tds) {
		for (TechDebtResource techDebtResource : tds) {
			techDebtResource.elements().sort((a, b) -> {
				if (a.getLineNumber() == b.getLineNumber()) {
					return a.getSeverity().compareTo(b.getSeverity());
				}
				return Integer.compare(a.getLineNumber(), b.getLineNumber());
			});
		}
		return tds;
	}
	
	@Override
	protected void executeReport(Locale locale) throws MavenReportException {
		try {

			Path outputDir = Paths.get(this.getOutputDirectory());
			if (!outputDir.toFile().exists()) {
				outputDir.toFile().mkdirs();
			}
			if (!outputDir.toFile().isDirectory()) {
				throw new MavenReportException("The output directory is not a directory");
			}
			Path[] paths = sources.stream().map(Paths::get).toArray(Path[]::new);
			SourcesNavigator navigator = new SourcesNavigator();
			Log log = this.getLog();
			navigator.setInternalLogger(new MavenInternalLogger(log));
			List<TechDebtResource> tds = navigator.navigate(paths);
			tds = order(tds);
			
			XmlReporter reporter = new XmlReporter();
			Properties props = new Properties();
			props.put(AbstractFileReporter.OUTPUT_FILE_LOCATION, outputDir.toString());
			props.put(AbstractFileReporter.FILENAME_PATTERN, filenamePattern);
			props.put(AbstractFileReporter.FILTER_REPORT, filter);
			reporter.init(props);

			reporter.report(tds);
			
			this.buildHtml(tds);
		} catch (Exception e) {
			throw new MavenReportException("ERror to generate the report", e);
		}

	}

}
