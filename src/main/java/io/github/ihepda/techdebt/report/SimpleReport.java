package io.github.ihepda.techdebt.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import java.util.Set;

import io.github.ihepda.techdebt.TechDebt;
import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.TechDebt.Severity;

public class SimpleReport extends AbstractReport {

	private File output;
	@Override
	public void init(Properties parameters) {
		super.init(parameters);
		this.initOutputDirectory(parameters);
	}
	
	private void initOutputDirectory(Properties parameters) {
		String outputDir = parameters.getProperty(TechDebtReport.OUTPUT_DIRECTORY_PARAMETER);
		if(outputDir == null || outputDir.trim().length() == 0)
			outputDir = System.getProperty("java.io.tmpdir");
		File f = new File(outputDir);
		if(!f.exists())
			f.mkdirs();
		if(!f.isDirectory())
			throw new IllegalArgumentException("Parameter " + outputDir + " is not a directory");
		String outputFileName = parameters.getProperty(TechDebtReport.OUTPUT_NAME_PARAMETER);
		if(outputFileName == null || outputFileName.trim().length() == 0)
			outputFileName = TechDebt.class.getSimpleName()+".report";
		File outputFile = new File(f, outputFileName);
		if(outputFile.isDirectory())
			throw new IllegalArgumentException("Parameter " + outputDir + "/"+outputFileName+" is a directory");
		if(outputFile.exists() && !outputFile.delete())
			throw new IllegalArgumentException("The system can't delete " + outputFile);
		this.output = outputFile;
	}

	@Override
	public boolean report(Set<TechDebtElement> elements) {
		elements = this.getOrderedSet(elements);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
			Severity currentSeverity = null;
			for (TechDebtElement techDebtElement : elements) {
				if(!this.isSeverityOrdered()) {
					writer.append(techDebtElement.getElementKind().toString()).append(" ==> ");
					writer.append(techDebtElement.getFullName()).append('\n');
					writer.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
					writer.append("Comment = " ).append(techDebtElement.getComment()).append('\n');
				} else {
					if(currentSeverity != techDebtElement.getSeverity()) {
						currentSeverity = techDebtElement.getSeverity();
						writer.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
					}
					writer.append(techDebtElement.getElementKind().toString()).append(" ==> ");
					writer.append(techDebtElement.getFullName()).append('\n');
					writer.append("Comment = " ).append(techDebtElement.getComment()).append('\n');
				}
			}
			writer.flush();
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
