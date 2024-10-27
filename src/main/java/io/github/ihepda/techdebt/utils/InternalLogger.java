package io.github.ihepda.techdebt.utils;

public interface InternalLogger {

	void error(String message, Throwable t);
	void error(String message, Object... args);
	void info(String message, Throwable t);
	void info(String message, Object... args);
	void warn(String message, Throwable t);
	void warn(String message, Object... args);
	void debug(String message, Throwable t);
	void debug(String message, Object... args);
	
	
}
