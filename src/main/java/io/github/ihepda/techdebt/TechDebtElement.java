package io.github.ihepda.techdebt;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import io.github.ihepda.techdebt.TechDebt.Severity;

public class TechDebtElement {
	private TechDebt techDebt;
	private Element element;
	
	public TechDebtElement(TechDebt techDebt, Element element) {
		super();
		this.techDebt = techDebt;
		this.element = element;
	}
	
	public Severity getSeverity() {
		return this.techDebt.severity();
	}
	
	public String getComment() {
		return this.techDebt.comment();
	}
	
	
	
	public ElementKind getElementKind() {
		return this.element.getKind();
	}
	
	public String getFullName() {
		return getFullName(this.element);
	}
	
	private String getFullName(Element element) {
		if(element == null) return null;
		if (element instanceof QualifiedNameable) {
			return ((QualifiedNameable) element).getQualifiedName().toString();
		} else if(element instanceof VariableElement) {
			String parentFullName = this.getFullName(element.getEnclosingElement());
			StringBuilder sb = new StringBuilder();
			if(parentFullName != null) {
				sb.append(parentFullName);
				if(element.getEnclosingElement().getKind() == ElementKind.CONSTRUCTOR ||
						element.getEnclosingElement().getKind() == ElementKind.METHOD )
					sb.append("=>parameter ");
				else	
					sb.append('.');
			}
			sb.append(element.getSimpleName());
			return sb.toString();
		} else if(element instanceof ExecutableElement){
			ExecutableElement executableElement = (ExecutableElement) element;
			String parentFullName = this.getFullName(element.getEnclosingElement());
			StringBuilder sb = new StringBuilder();
			if(parentFullName != null)
				sb.append(parentFullName).append('.');
			sb.append(element.getSimpleName());
			sb.append('(');
			List<? extends VariableElement> parameters = executableElement.getParameters();
			boolean first = true;
			for (VariableElement typeParameterElement : parameters) {
				if(!first)
					sb.append(',');
				first = false;
				sb.append(typeParameterElement.asType()).append(' ');
				sb.append(typeParameterElement.getSimpleName());
			}
			sb.append(")->").append(executableElement.getReturnType());
			return sb.toString();
			
		}else if(element instanceof TypeParameterElement){
			String parentFullName = this.getFullName(element.getEnclosingElement());
			StringBuilder sb = new StringBuilder();
			if(parentFullName != null)
				sb.append(parentFullName).append("=>parameter(");
			sb.append(element.getSimpleName());
			sb.append(')');
		}
		return null;
	}

	@CheckForNull
	public Name getClassName() {
		Element internalElement = this.element;
		ElementKind kind = internalElement.getKind();
		while(kind == ElementKind.CLASS) {
			internalElement = internalElement.getEnclosingElement();
			if(internalElement == null) break;
			kind = internalElement.getKind();
		} 
		if(internalElement == null) return null;
		return ((TypeElement) internalElement).getQualifiedName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TechDebtElement [techDebt=");
		builder.append(techDebt);
		builder.append(", element=");
		builder.append(element);
		builder.append(", fullName=");
		builder.append(this.getFullName());
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
