package io.github.ihepda.techdebt.filter;

import java.util.regex.Pattern;

import io.github.ihepda.techdebt.utils.StringUtils;

public class LikeOperation extends CompareOperation{

	private Pattern likePattern;
	
	@Override
	protected boolean compare(Comparable<?> techDebtValue, Comparable<?> value) {
		if (techDebtValue instanceof String s) {
            return likePattern.matcher(s).matches();
		}
		return false;
	}
	
	@Override
	protected void setValue(Comparable<?> value) {
		if (value instanceof String s) {
			String regExp = StringUtils.sqlLikeToRegExp(s);
			likePattern = Pattern.compile(regExp, Pattern.DOTALL);
		} 
		super.setValue(value);
	}

	@Override
	protected String operation() {
		return "LIKE";
	}

}
