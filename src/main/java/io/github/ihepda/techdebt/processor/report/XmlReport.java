package io.github.ihepda.techdebt.processor.report;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import io.github.ihepda.techdebt.TechDebt;
import io.github.ihepda.techdebt.processor.TechDebtElement;

/**
 * Simple report implementation that writes the technical debt elements to a
 * file in an XML format.
 */
public class XmlReport extends AbstractReport {

	private File output;
	@Override
	public void init(Properties parameters) {
		super.init(parameters);
		this.initOutputDirectory(parameters);
	}
	
	private void initOutputDirectory(Properties parameters) {
		String outputDir = parameters.getProperty(TechDebtReport.OUTPUT_DIRECTORY_PARAMETER,
				System.getProperty("java.io.tmpdir"));
		File dir = new File(outputDir);

		if (!dir.exists() && !dir.mkdirs()) {
			throw new IllegalArgumentException("Unable to create directory: " + outputDir);
		}
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Parameter " + outputDir + " is not a directory");
		}

		String outputFileName = parameters.getProperty(TechDebtReport.OUTPUT_NAME_PARAMETER,
				TechDebt.class.getSimpleName() + ".xml");
		File outputFile = new File(dir, outputFileName);

		if (outputFile.isDirectory()) {
			throw new IllegalArgumentException("Parameter " + outputDir + "/" + outputFileName + " is a directory");
		}
		if (outputFile.exists() && !outputFile.delete()) {
			throw new IllegalArgumentException("The system can't delete " + outputFile);
		}

		this.output = outputFile;
	}

	@Override

	public boolean report(Set<TechDebtElement> elements) {
		try (FileOutputStream fos = new FileOutputStream(this.output)) {
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");

			writer.writeStartDocument();
			writer.writeStartElement("report");

			for (TechDebtElement element : elements) {
				writer.writeStartElement("techdebt");
				writer.writeAttribute("severity", element.getSeverity().toString());
				writer.writeAttribute("type", element.getType());
				writer.writeAttribute("author", element.getAuthor());
				writer.writeAttribute("date", element.getDate());
				writer.writeAttribute("effort", element.getEffort());

				writer.writeStartElement("name");
				writer.writeCharacters(element.getFullName());
				writer.writeEndElement();

				writer.writeStartElement("comment");
				writer.writeCharacters(element.getComment());
				writer.writeEndElement();

				writer.writeEndElement();
			}

			writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
