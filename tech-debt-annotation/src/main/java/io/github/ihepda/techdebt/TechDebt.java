package io.github.ihepda.techdebt;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark technical debt in the codebase. Technical
 * debt refers to the implied cost of additional rework caused by choosing an
 * easy solution now instead of using a better approach that would take longer.
 * 
 * 
 * Attributes:<br/> 
 * - comment: A description of the technical debt.<br/> 
 * - author: The author who identified the technical debt. <br/>
 * - date: The date when the technical debt was identified. <br/>
 * - severity: The severity level of the technical debt. <br/>
 * - type: The type of technical debt. - effort: The estimated effort required to
 * address the technical debt.
 * 
 * Enums: <br/>
 * - Severity: Defines the priority levels of the technical debt. <br/>
 * - Type:Defines the categories of technical debt. <br/> 
 * - Effort: Defines the effort levels required to address the technical debt.
 */

@Repeatable(TechDebts.class)
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, METHOD, PACKAGE, MODULE, PARAMETER, TYPE, LOCAL_VARIABLE})
public @interface TechDebt {

	/**
	 * A description of the technical debt.
	 * @return
	 */
	String comment() default "";
	
	/**
     * A reference to a comment that provides more information about the technical debt.
     */
	String refComment() default "";

	/**
	 * The author who identified the technical debt.
	 * 
	 * @return
	 */
	String author() default "";

	/**
	 * The date when the technical debt was identified.
	 * 
	 * @return
	 */
	String date() default "";
	/**
	 * The severity level of the technical debt.
	 * @return
	 */
	Severity severity() default Severity.MINOR;
	/**
	 * The type of technical debt.
	 * @return
	 */
	Type type() default Type.FIX;
	/**
	 * The estimated effort required to address the technical debt.
	 * @return
	 */
	Effort effort() default Effort.MEDIUM;
	
	/**
	 * The solution to address the technical debt.
	 * 
	 * @return
	 */
	String solution() default "";
	
	/**
	 * Indicates if to get all java element in the comment o not. 
	 * if false it saves only the element name and not its content (ie: class or method) 
	 * Default is false
	 * @return
	 */
	boolean allElement() default false;
	
	/**
	 * The ticket number to track the technical debt.
	 * 
	 * @return
	 */
	String ticket() default "";
	
	/**
	 * Defines the priority levels of the technical debt.
	 */
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
		
		public static Severity valueOf(int priority) {
			for (Severity severity : values()) {
				if (severity.priority == priority) {
					return severity;
				}
			}
			return null;
		}
		
	}
	/**
	 * Defines the categories of technical debt.
	 */
	public enum Type {
		FIX,
		PERFORMANCE,
		SECURITY,
		MAINTAINABILITY,
		REMOVE;
		
		public static Type valueOf(int priority) {
			for (Type type : values()) {
				if (type.ordinal() == priority) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * Defines the effort levels required to address the technical debt.
	 */
	public enum Effort {
	    MASSIVE(20),
	    HUGE(10),
	    LARGE(5),
	    MEDIUM(0),
	    SMALL(-5),
	    MICRO(-10);

		
		private int priority;

		private Effort(int priority) {
			this.priority = priority;
		}
		public int getPriority() {
			return priority;
		}
		
		public static Effort valueOf(int priority) {
            for (Effort effort : values()) {
                if (effort.priority == priority) {
                    return effort;
                }
            }
            return null;
		}
	}
}
