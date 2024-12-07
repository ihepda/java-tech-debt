package io.github.ihepda.techdebt.filter;

import java.util.Objects;

enum FilterConditionType {
    EQUALS("=") {
		@Override
		public boolean compare(Object value, Object filterValue) {
			return Objects.equals(value, filterValue);
		}
	},
    NOT_EQUALS("!=") {
		@Override
		public boolean compare(Object value, Object filterValue) {
			return !Objects.equals(value, filterValue);
		}
	},
    CONTAINS("~") {

		@Override
		public boolean compare(Object value, Object filterValue) {
			return value.toString().contains(filterValue.toString());
			
		}
		
	},
    NOT_CONTAINS("!~") {
		@Override
		public boolean compare(Object value, Object filterValue) {
			return !value.toString().contains(filterValue.toString());
		}
	};

    private String conditionString;

    FilterConditionType(String conditionString) {
        this.conditionString = conditionString;
    }
    
    public String getConditionString() {
		return conditionString;
	}
    
    public abstract boolean compare(Object value, Object filterValue);

    public static FilterConditionType resolveConditionType(String conditionString) {
		for (FilterConditionType conditionType : FilterConditionType.values()) {
			if (conditionType.conditionString.equals(conditionString)) {
				return conditionType;
			}
		}
		return null;
    }
}