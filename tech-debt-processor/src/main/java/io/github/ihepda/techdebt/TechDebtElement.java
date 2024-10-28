
package io.github.ihepda.techdebt;

import io.github.ihepda.techdebt.TechDebt.Effort;
import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public class TechDebtElement {
    private Severity severity;
    private Type type;
    private String comment;
    private String refComment;
    private Effort effort;
    private String solution;
    private String author;
    private String date;
    private String elementAt;
    private int lineNumber;
    private String fileName;
    private boolean allElement;

    public TechDebtElement() {
        this.severity = Severity.MINOR;
        this.effort = Effort.MEDIUM;
        this.type = Type.FIX;
        this.author = "";
        this.date = "";
        this.solution = "";
        allElement = false;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRefComment() {
        return refComment;
    }

    public void setRefComment(String refComment) {
        this.refComment = refComment;
    }

    public Effort getEffort() {
        return effort;
    }

    public void setEffort(Effort effort) {
        this.effort = effort;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getElementAt() {
        return elementAt;
    }

    public void setElementAt(String elementAt) {
        this.elementAt = elementAt;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
	public boolean isAllElement() {
		return allElement;
	}

	public void setAllElement(boolean allElement) {
		this.allElement = allElement;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TechDebtElement [severity=");
		builder.append(severity);
		builder.append(", type=");
		builder.append(type);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", refComment=");
		builder.append(refComment);
		builder.append(", effort=");
		builder.append(effort);
		builder.append(", solution=");
		builder.append(solution);
		builder.append(", author=");
		builder.append(author);
		builder.append(", date=");
		builder.append(date);
		builder.append(", elementAt=");
		builder.append(elementAt);
		builder.append(", lineNumber=");
		builder.append(lineNumber);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", allElement=");
		builder.append(allElement);
		builder.append("]");
		return builder.toString();
	}

    
}
