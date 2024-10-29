package io.github.ihepda.techdebt.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNamePatternUtils {

    private static final Pattern datePattern = Pattern.compile("\\{date:([^}]+)\\}");
    
	private FileNamePatternUtils() {}

    public static String generateFileName(String pattern, Map<String, String> additionalData) {
    	String fileName = pattern;

        // Replace date placeholders with specified format
        Matcher dateMatcher = datePattern.matcher(fileName);
        while (dateMatcher.find()) {
            LocalDateTime now = LocalDateTime.now();
            String dateFormat = dateMatcher.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            fileName = fileName.replace(dateMatcher.group(0), now.format(formatter));
        }

        // Replace additional placeholders
        for (Map.Entry<String, String> entry : additionalData.entrySet()) {
            fileName = fileName.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return fileName;
    }

}

