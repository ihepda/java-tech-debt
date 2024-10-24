package io.github.ihepda.techdebt;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;

import io.github.ihepda.techdebt.report.SimpleReport;
import io.github.ihepda.techdebt.report.SysoutReport;
import io.github.ihepda.techdebt.report.TechDebtReport;
import io.github.ihepda.techdebt.report.XmlReport;

@SupportedAnnotationTypes(
		{"io.github.ihepda.techdebt.TechDebt", "io.github.ihepda.techdebt.TechDebts"})
@SupportedOptions(
		{
			TechDebtAnnotationProcessor.DISABLED_PROCESSOR
			, TechDebtReport.REPORT_IMPLEMENTATION
			, TechDebtReport.OUTPUT_DIRECTORY_PARAMETER
			, TechDebtReport.OUTPUT_NAME_PARAMETER
			, TechDebtReport.SEVERITY_ORDER_PARAMETER
			})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TechDebtAnnotationProcessor extends AbstractProcessor {
	
	public static final String DISABLED_PROCESSOR = "techdebt.disabled";
	private boolean disabled = false;
	private TechDebtReport reporter;
	
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		String disabledString = processingEnv.getOptions().get(DISABLED_PROCESSOR);
		disabled = Boolean.parseBoolean(disabledString);
		reporter = this.getReportImplementation();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if(roundEnv.processingOver() || disabled) return false;
		TreeSet<TechDebtElement> elements = new TreeSet<>((element1, element2) ->  element1.hashCode() - element2.hashCode());
		for (TypeElement annotation : annotations) {
	        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
	        for (Element element : annotatedElements) {
	        	System.out.println("Element: " + element);
	        	
	        	TechDebts debts = element.getAnnotation(TechDebts.class);
	        	System.out.println("TechDebts: " + debts);
	        	TechDebt[] techDebts;
	        	if(debts != null)
	        		techDebts = debts.value();
	        	else
	        		techDebts = element.getAnnotationsByType(TechDebt.class);
	        	for (TechDebt techDebt : techDebts) {
		        	TechDebtElement techDebtElement = new TechDebtElement(techDebt, element);
		        	elements.add(techDebtElement);
				}
			}
	    }
		return executeReport(elements);
	}

	private boolean executeReport(TreeSet<TechDebtElement> elements) {
		Properties p = new Properties();
		p.putAll(processingEnv.getOptions());
		TechDebtReport report = this.reporter;
		report.init(p);
		return report.report(elements);
	}

	private TechDebtReport getReportImplementation() {
		String implementationClassName = processingEnv.getOptions().get(TechDebtReport.REPORT_IMPLEMENTATION);
		if(implementationClassName == null || implementationClassName.trim().length() == 0) {
			return new SysoutReport();
		}else if(Objects.equals(implementationClassName, "sysout")) {
			return new SysoutReport();
		} else if(Objects.equals(implementationClassName, "simple")) {
			return new SimpleReport();
		} else if(Objects.equals(implementationClassName, "xml")) {
			return new XmlReport();
		} 
		try {
			return (TechDebtReport) Class.forName(implementationClassName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.err.println("Error to instantiate the class : " + implementationClassName + " using the default");
			e.printStackTrace();
			return new SimpleReport();
		}
		
	}
	
}
