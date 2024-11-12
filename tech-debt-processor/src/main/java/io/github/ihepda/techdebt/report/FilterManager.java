package io.github.ihepda.techdebt.report;

import java.util.Map;

public class FilterManager {

    private Map<String, Object> filters;
    
    private enum ConditionType {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
        NOT_CONTAINS;

        private String conditionString;

        ConditionType(String conditionString) {
            this.conditionString = conditionString;
        }

        public ConditionType resolveConditionType(String conditionString) {
        }
    }

}
