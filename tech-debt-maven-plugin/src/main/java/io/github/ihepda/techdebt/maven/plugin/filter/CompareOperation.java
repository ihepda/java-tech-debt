package io.github.ihepda.techdebt.maven.plugin.filter;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public abstract class CompareOperation implements FilterOperation {
	private Enum<?> element;
	
	private int value;
	
	private boolean not = false;
	
	
	public boolean isNot() {
		return not;
	}
	
	public void setNot(boolean not) {
		this.not = not;
	}
	
	public Enum<?> getElement() {
		return element;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public boolean execute(CounterByData counterByType) {
		int count = 0;
		
		if (element instanceof Severity s) {
			count = counterByType.getCount(s);
		} else if (element instanceof Type t) {
			count = counterByType.getCount(t);
		} else {
			count = counterByType.getCount();
		}
		
		boolean compare = this.compare(count, this.value);
		return not? !compare : compare;
	}
	
	protected abstract boolean compare(int count, int value);

	protected abstract String operation();
	
	public void setFilterElement(Enum<?> element) {
        this.element = element;
    }
	
	public void setLiteral(int value) {
		this.value = value;
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
