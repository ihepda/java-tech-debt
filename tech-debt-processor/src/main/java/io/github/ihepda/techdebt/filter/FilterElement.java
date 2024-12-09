package io.github.ihepda.techdebt.filter;

import java.util.function.BiFunction;
import java.util.function.Function;

import io.github.ihepda.techdebt.TechDebt.Effort;
import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;
import io.github.ihepda.techdebt.TechDebtElement;

public enum FilterElement {
	SEVERITY(Severity::valueOf, Severity::valueOf, TechDebtElement::getSeverity, (c1, c2) -> ((Severity)c1).compareTo((Severity) c2)),
	TYPE(Type::valueOf, Type::valueOf,TechDebtElement::getType, (c1, c2) -> ((Type)c1).compareTo((Type) c2)),
	EFFORT(Effort::valueOf, Effort::valueOf, TechDebtElement::getEffort, (c1, c2) -> ((Effort)c1).compareTo((Effort) c2)),
	COMMENT(s -> s, Integer::valueOf,  TechDebtElement::getComment, (c1, c2) -> ((String)c1).compareTo((String)c2)),
	SOLUTION(s -> s, Integer::valueOf, TechDebtElement::getSolution, (c1, c2) -> ((String)c1).compareTo((String)c2)),
	AUTHOR(s -> s, Integer::valueOf, TechDebtElement::getAuthor, (c1, c2) -> ((String)c1).compareTo((String)c2))
	;
	
	public static FilterElement fromString(String filterElement) {
		for (FilterElement element : FilterElement.values()) {
			if (element.name().equalsIgnoreCase(filterElement)) {
				return element;
			}
		}
		return null;
	}
	private Function<String, Comparable<?>> resolver;
	private Function<Integer, Comparable<?>> resolverNumeric;
	private Function<TechDebtElement, Comparable<?>> extractor;
	private BiFunction<Comparable<?>, Comparable<?>, Integer> comparator;

	private FilterElement(Function<String, Comparable<?>> resolver
			, Function<Integer, Comparable<?>> resolverNumeric
			, Function<TechDebtElement, Comparable<?>> extractor
			, BiFunction<Comparable<?>, Comparable<?>, Integer> comparator) {
		this.resolver = resolver;
		this.extractor = extractor;
		this.resolverNumeric = resolverNumeric;
		this.comparator = comparator;
	}
	
	public Integer compare(Comparable<?> base, Comparable<?> input) {
		return this.comparator.apply(base, input);
	}
	
	public Comparable<?> extract(TechDebtElement techDebtElement) {
        return extractor.apply(techDebtElement);
	}
	
	public Comparable<?> resolve(String value) {
		if (value == null) {
			return null;
		}
		return resolver.apply(value);
	}
	
	public Comparable<?> resolve(Integer value) {
		if (value == null) {
			return null;
		}
		return resolverNumeric.apply(value);
	}
	
}
