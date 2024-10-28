package io.github.ihepda.techdebt.utils;

import java.util.function.Consumer;

public class StringUtils {
	private StringUtils() {}

	public static void setStringIfNotBlank(String value, Consumer<String> setter) {
		if (isNotBlank(value)) {
			setter.accept(value);
		}
	}
	
	public static int extractIndexFromCollectionIndex(String val) {
		if(isBlank(val)) {
			return 0;
		}
		StringBuilder indexStr = new StringBuilder();
		val.chars().filter(Character::isDigit).forEach(c -> indexStr.append((char)c));
		return Integer.valueOf(indexStr.toString());
	}

	public static String extractKeyFromMapReference(String val) {
		if(isBlank(val)) {
			return val;
		}
		val = val.trim().substring(1,val.length()-1);
		return decode(val);
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}
	
	public static String decodeTextBlock(String text) {
		if(StringUtils.isBlank(text))
			return null;
		text = text.substring(3, text.length()-3);
		return decode(text);
	}
	
	public static String decode(String text) {
		if(StringUtils.isBlank(text))
			return null;
		return text.substring(1,text.length()-1).replace("\\\"", "\"").replace("\\\\", "\\");
	}

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isWhitespace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
