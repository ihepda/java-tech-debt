package io.github.ihepda.techdebt.report;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.javaparser.TechDebtResource;
import io.github.ihepda.techdebt.utils.StringUtils;

public class XmlReporter extends AbstractFileReporter {

	@Override
	protected void generateReport(List<TechDebtResource> resources) {
		try {
			Path ouputDir = getOutputDirectory();
			String fileName = this.getFileName();
			OutputStream os = new FileOutputStream(ouputDir.resolve(fileName).toFile());
			
			
			XMLStreamWriter writer = createWriter(os);
			writer.writeStartDocument();
			writer.writeStartElement("techDebtReport");
			for (TechDebtResource resource : resources) {
				generateReport(resource, writer);
			}
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Error generating report", e);
		}
	}
	
	private XMLStreamWriter createWriter(OutputStream os) {
		try {
			return XMLOutputFactory.newInstance().createXMLStreamWriter(os, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException("Error creating XML writer", e);
		}
	}

	
	private void generateReport(TechDebtResource resource, XMLStreamWriter writer) throws XMLStreamException {
		if (resource.elements().isEmpty()) {
			return;
		}
		writer.writeStartElement("resource");
		writer.writeAttribute("path", resource.path().toString());
		writer.writeAttribute("name", resource.resourceName());
        writer.writeStartElement("technicalDebts");
		for (TechDebtElement element : resource.elements()) {
			writer.writeStartElement("technicalDebt");
			writer.writeAttribute("severity", element.getSeverity().name());
			writer.writeAttribute("line", Integer.toString(element.getLineNumber()));
			writer.writeAttribute("type", element.getType().name());
			writer.writeAttribute("effort", element.getEffort().name());
			if (StringUtils.isNotBlank(element.getSolution())) {
				writer.writeAttribute("solution", element.getSolution());
			}
			if (StringUtils.isNotBlank(element.getAuthor())) {
				writer.writeAttribute("author", element.getAuthor());
			}
			if (StringUtils.isNotBlank(element.getDate())) {
				writer.writeAttribute("date", element.getDate());
			}
			if (StringUtils.isNotBlank(element.getRefComment())) {
				writer.writeAttribute("refComment", element.getRefComment());
			}
			
			writer.writeStartElement("comment");
			writer.writeCharacters(element.getComment());
			writer.writeEndElement();

			writer.writeStartElement("elementAt");
			writer.writeCharacters(element.getElementAt());
			writer.writeEndElement();
			
			writer.writeEndElement();
		}
		writer.writeEndElement();
		writer.writeEndElement();
	}

	@Override
	protected String getDefaultFileNamePattern() {
		return "tech-debts-report.xml";
	}

}
