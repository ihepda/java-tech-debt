package io.github.ihepda.techdebt;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A container for multiple {@link TechDebt} annotations.
 */
@Retention(SOURCE)
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, PACKAGE })
public @interface TechDebts {
	TechDebt[] value();
}
