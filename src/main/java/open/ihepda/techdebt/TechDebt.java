package open.ihepda.techdebt;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Repeatable(TechDebts.class)
@Retention(SOURCE)
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, PACKAGE })
public @interface TechDebt {

	String comment();
	Severity severity();
	
	public enum Severity {
		TRIVIAL(-10),
		MINOR(10),
		MAJOR(20),
		SEVERE(30),
		CATASTROPHIC(40);
		private int priority;

		private Severity(int priority) {
			this.priority = priority;
		}
		public int getPriority() {
			return priority;
		}
		
	}
}
