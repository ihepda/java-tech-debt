package io.github.ihepda.techdebt.utils;

import java.io.PrintStream;
import java.util.Arrays;

public class ConsoleLogger implements InternalLogger {

	private Pair<String,Throwable> parseMessage(String message, boolean searchTrace, Object[] args) {
		message = message == null? "":message.replace("{}", "%s");
		if (args == null || args.length == 0) {
			return Pair.of(message);
		} 
		if (!searchTrace) {
			return Pair.of(String.format(message, args));
		}
		Object lastObject = args[args.length - 1];
		if (!(lastObject instanceof Throwable)) {
			return Pair.of(String.format(message, args));
		}
		Object[] copyOf = Arrays.copyOf(args, args.length-1);
		return new Pair<>(String.format(message, copyOf), (Throwable)lastObject);
	}
	
	private String stackTraceToString(Throwable t) {
		StringBuilder sb = new StringBuilder();
		sb.append(t.getMessage()).append("\n");
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			sb.append(stackTraceElement.toString()).append("\n");
		}
		return sb.toString();
		
	}
	
	private void printMessage(PrintStream ps, String prefix, String message, Throwable t) {
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
		ps.println(prefix + pair.first());
		ps.println(prefix + this.stackTraceToString(t));
	}

	private void printMessage(PrintStream ps, String prefix, String message, Object[] args) {
		Pair<String, Throwable> pair = this.parseMessage(message, true, args);
		ps.println(prefix + pair.first());
		if (pair.second() != null) {
			ps.println(prefix + this.stackTraceToString(pair.second()));
		}
	}
	
	@Override
	public void error(String message, Throwable t) {
		this.printMessage(System.err, "[ERROR] ", message, t);
	}

	@Override
	public void error(String message, Object... args) {
		this.printMessage(System.err, "[ERROR] ", message, args);
	}

	@Override
	public void info(String message, Throwable t) {
		this.printMessage(System.out, "[INFO] ", message, t);
	}

	@Override
	public void info(String message, Object... args) {
		this.printMessage(System.out, "[INFO] ", message, args);
	}

	@Override
	public void warn(String message, Throwable t) {
		this.printMessage(System.out, "[WARN] ", message, t);
	}

	@Override
	public void warn(String message, Object... args) {
		this.printMessage(System.out, "[WARN] ", message, args);
	}

	@Override
	public void debug(String message, Throwable t) {
		this.printMessage(System.out, "[DEBUG] ", message, t);
	}

	@Override
	public void debug(String message, Object... args) {
		this.printMessage(System.out, "[DEBUG] ", message, args);
	}
	
}
