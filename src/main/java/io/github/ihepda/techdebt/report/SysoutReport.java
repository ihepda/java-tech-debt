package io.github.ihepda.techdebt.report;

import java.util.Set;

import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.TechDebt.Severity;

public class SysoutReport extends AbstractReport {


	@Override
	public boolean report(Set<TechDebtElement> elements) {
		elements = this.getOrderedSet(elements);
		Severity currentSeverity = null;
		System.out.println("******* Starting to dump techdebt report *******");
		for (TechDebtElement techDebtElement : elements) {
			if(!this.isSeverityOrdered()) {
					System.out.append(techDebtElement.getElementKind().toString()).append(" ==> ");
					System.out.append(techDebtElement.getFullName()).append('\n');
					System.out.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
					System.out.append("Comment = " ).append(techDebtElement.getComment()).append('\n');
			} else {
				if(currentSeverity != techDebtElement.getSeverity()) {
					currentSeverity = techDebtElement.getSeverity();
					System.out.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
				}
				System.out.append(techDebtElement.getElementKind().toString()).append(" ==> ");
				System.out.append(techDebtElement.getFullName()).append('\n');
				System.out.append("Comment = " ).append(techDebtElement.getComment()).append('\n');
			}
		}
		System.out.println("************************************************");
		return true;
	}

}
