package io.github.ihepda.techdebt.report;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import io.github.ihepda.techdebt.TechDebt;
import io.github.ihepda.techdebt.TechDebtElement;

public class XmlReport extends AbstractReport {

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
			outputFileName = TechDebt.class.getSimpleName()+".xml";
		File outputFile = new File(f, outputFileName);
		if(outputFile.isDirectory())
			throw new IllegalArgumentException("Parameter " + outputDir + "/"+outputFileName+" is a directory");
		if(outputFile.exists() && !outputFile.delete())
			throw new IllegalArgumentException("The system can't delete " + outputFile);
		this.output = outputFile;
	}

	@Override
	public boolean report(Set<TechDebtElement> elements) {
		 XMLOutputFactory xmlOutput = XMLOutputFactory.newInstance();
		 try(FileOutputStream fos = new FileOutputStream(this.output)) {
			 
			 XMLStreamWriter writer = xmlOutput.createXMLStreamWriter(fos, "UTF-8");
			 
			 writer.writeStartDocument();
			 writer.writeStartElement("report");
			 for (TechDebtElement techDebtElement : elements) {
				writer.writeStartElement("techdebt");
				writer.writeAttribute("severity", techDebtElement.getSeverity().toString());
				writer.writeAttribute("type", techDebtElement.getType());
				writer.writeAttribute("author", techDebtElement.getAuthor());
				writer.writeAttribute("date", techDebtElement.getDate());
				writer.writeAttribute("effort", techDebtElement.getEffort());
				writer.writeStartElement("name");
				writer.writeCharacters(techDebtElement.getFullName());
				writer.writeEndElement();

				writer.writeStartElement("comment");
				writer.writeCharacters(techDebtElement.getComment());
				writer.writeEndElement();
				
				writer.writeEndElement();
				
			}
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();
			return true;
			 
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}

}
