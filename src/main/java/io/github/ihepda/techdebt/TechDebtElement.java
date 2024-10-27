
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

    public TechDebtElement() {
        this.severity = Severity.MINOR;
        this.effort = Effort.MEDIUM;
        this.type = Type.FIX;
        this.author = "";
        this.date = "";
        this.solution = "";
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

	@Override
	public String toString() {
		return "TechDebtElement [severity=" + severity + ", type=" + type + ", comment=" + comment + ", refComment="
				+ refComment + ", effort=" + effort + ", solution=" + solution + ", author=" + author + ", date=" + date
				+ ", elementAt=" + elementAt + ", lineNumber=" + lineNumber + ", fileName=" + fileName + "]";
	}

    
}
