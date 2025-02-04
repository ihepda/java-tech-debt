package io.github.ihepda.techdebt.maven.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;
import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.javaparser.SourcesNavigator;
import io.github.ihepda.techdebt.javaparser.TechDebtResource;
import io.github.ihepda.techdebt.maven.plugin.filter.CounterByData;
import io.github.ihepda.techdebt.maven.plugin.filter.FilterManager;
import io.github.ihepda.techdebt.utils.StringUtils;

@Mojo(name = "check"
, defaultPhase = LifecyclePhase.PROCESS_SOURCES
, requiresProject = true, threadSafe = true)
public class TechDebtCheckerMojo extends AbstractMojo {

	@Parameter(property = "techdebt.checker.filter", required = false)
	private String filter;
	
	@Parameter(property = "source.dir", defaultValue = "${project.build.sourceDirectory}", required = true)
	private List<String> sources;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Log log = this.getLog();
		if(StringUtils.isBlank(filter)) {
			log.info("No filter defined! Bypass check");
			return;
		}
		log.debug("Filter defined : " + filter);
		Path[] paths = sources.stream().map(Paths::get).toArray(Path[]::new);
		SourcesNavigator navigator = new SourcesNavigator();
		MavenInternalLogger logger = new MavenInternalLogger(log);
		navigator.setInternalLogger(logger);
		List<TechDebtResource> tds = navigator.navigate(paths);
		List<TechDebtElement> list = tds.stream().flatMap(td-> td.elements().stream()).toList();
		CounterByData counter = new CounterByData();
		for (TechDebtElement techDebtElement : list) {
			Severity severity = techDebtElement.getSeverity();
			Type type = techDebtElement.getType();
			counter.add(severity, type);
		}
		log.debug("Tech debt elements found : " + counter.getCount());
		FilterManager filterManager = new FilterManager(filter, logger);
		boolean check = filterManager.check(counter);
		if (check) {
			throw new MojoFailureException("Tech debt check failed!");
		}
	}
	

}
