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
import io.github.ihepda.techdebt.maven.plugin.filter.CounterByType;

@Mojo(name = "check"
, defaultPhase = LifecyclePhase.VERIFY
, requiresDependencyResolution = ResolutionScope.RUNTIME
, requiresProject = true, threadSafe = true)
public class TechDebtCheckerMojo extends AbstractMojo {

	@Parameter(property = "techdebt.checker.filter", required = false)
	private String filter;
	
	@Parameter(property = "source.dir", defaultValue = "${project.build.sourceDirectory}", required = true)
	private List<String> sources;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Path[] paths = sources.stream().map(Paths::get).toArray(Path[]::new);
		SourcesNavigator navigator = new SourcesNavigator();
		Log log = this.getLog();
		MavenInternalLogger logger = new MavenInternalLogger(log);
		navigator.setInternalLogger(logger);
		List<TechDebtResource> tds = navigator.navigate(paths);
		List<TechDebtElement> list = tds.stream().flatMap(td-> td.elements().stream()).toList();
		CounterByType counter = new CounterByType();
		for (TechDebtElement techDebtElement : list) {
			Severity severity = techDebtElement.getSeverity();
			Type type = techDebtElement.getType();
			counter.add(severity, type);
		}
	}
	

}
