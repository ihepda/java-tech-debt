package io.github.ihepda.techdebt.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.ihepda.techdebt.TechDebtElement;

public class InOperation extends CompareOperation {

	private List<Comparable<?>> params = new ArrayList<>();
	
	@Override
	protected void setValue(Comparable<?> value) {
		this.params.add(value);
	}
	
	@Override
	public boolean execute(TechDebtElement techDebt) {
		Comparable<?> comparable = this.getElement().extract(techDebt);
		for (Comparable<?> param : params) {
			if (compare(comparable, param)) {
				return true;
			}
		}

		return false;
	}
	
	
	@Override
	protected boolean compare(Comparable<?> techDebtValue, Comparable<?> value) {
		return Objects.equals(value, techDebtValue);
	}

	@Override
	protected String operation() {
		return "IN";
	}

	
	@Override
	public void dump(StringBuilder sb) {
		sb.append(this.getElement())
		.append(' ');
		if(isNot()) {
            sb.append("NOT ");
        }
		sb.append(this.operation())
		.append(" (");
		for (Comparable<?> comparable : params) {
			sb.append(comparable);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		
		
	}


}
