package io.github.ihepda.techdebt.filter;

import io.github.ihepda.techdebt.TechDebtElement;

public abstract class CompareOperation implements FilterOperation {
	private FilterElement element;
	
	private Comparable<?> value;
	
	private boolean not = false;
	
	
	public boolean isNot() {
		return not;
	}
	
	public void setNot(boolean not) {
		this.not = not;
	}
	
	public FilterElement getElement() {
		return element;
	}
	
	public Comparable<?> getValue() {
		return value;
	}
	
	@Override
	public boolean execute(TechDebtElement techDebt) {
		Comparable<?> comparable = this.element.extract(techDebt);
		boolean compare = this.compare(comparable, value);
		return not? !compare : compare;
	}
	
	protected abstract boolean compare(Comparable<?> techDebtValue, Comparable<?> value);

	protected abstract String operation();
	
	public void setFilterElement(FilterElement element) {
        this.element = element;
    }
	
	protected void setValue(Comparable<?> value) {
        this.value = value;
	}
	
	public void setLiteral(Comparable<?> value) {
		if(this.element == null || value == null) {
			this.setValue(value);
		}else if(value instanceof String s) {
			this.setValue(this.element.resolve(s));
		} else if (value instanceof Integer i) {
			this.setValue(this.element.resolve(i));
		}
	}

	@Override
	public void dump(StringBuilder sb) {
		sb.append(element)
		.append(' ');
		if(not) {
            sb.append("NOT ");
        }
		sb.append(this.operation())
		.append(' ')
		.append(value);
	}
	
}
