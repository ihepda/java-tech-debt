package io.github.ihepda.techdebt;


import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(TechDebts.class)
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface TechDebt {

	String comment();
	String author() default "";
	String date() default "";
	Severity severity() default Severity.MINOR;
	Type type() default Type.FIX;
	Effort effort() default Effort.MEDIUM;
	
	
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
	public enum Type {
		FIX,
		PERFORMANCE,
		SECURITY,
		MAINTAINABILITY,
		REMOVE;
	}
	
	public enum Effort {
		MICRO,
		SMALL,
		MEDIUM,
		LARGE,
		HUGE,
		MASSIVE
	}
}
