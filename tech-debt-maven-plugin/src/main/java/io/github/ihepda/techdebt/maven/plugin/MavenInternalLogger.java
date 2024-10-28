package io.github.ihepda.techdebt.maven.plugin;

import java.io.PrintStream;
import java.util.Arrays;

import org.apache.maven.plugin.logging.Log;

import io.github.ihepda.techdebt.utils.InternalLogger;
import io.github.ihepda.techdebt.utils.Pair;

public class MavenInternalLogger implements InternalLogger{

	private Log log;
	
	public MavenInternalLogger(Log log) {
		this.log = log;
	}
	
	
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
	
	private void printMessage(Log log, String message, Throwable t) {
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
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
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
		log.error(pair.first(), t);
	}

	@Override
	public void error(String message, Object... args) {
		Pair<String, Throwable> pair = this.parseMessage(message, true, args);
		if (pair.second() != null) {
			log.error(pair.first(), pair.second());
		} else {
			log.error(pair.first());
		}
	}

	@Override
	public void info(String message, Throwable t) {
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
		log.info(pair.first(), t);
		
	}

	@Override
	public void info(String message, Object... args) {
		Pair<String, Throwable> pair = this.parseMessage(message, true, args);
		if (pair.second() != null) {
			log.info(pair.first(), pair.second());
		} else {
			log.info(pair.first());
		}
		
	}

	@Override
	public void warn(String message, Throwable t) {
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
		log.warn(pair.first(), t);
		
	}

	@Override
	public void warn(String message, Object... args) {
		Pair<String, Throwable> pair = this.parseMessage(message, true, args);
		if (pair.second() != null) {
			log.warn(pair.first(), pair.second());
		} else {
			log.warn(pair.first());
		}
		
	}

	@Override
	public void debug(String message, Throwable t) {
		Pair<String, Throwable> pair = this.parseMessage(message, false, null);
		log.debug(pair.first(), t);
		
	}

	@Override
	public void debug(String message, Object... args) {
		Pair<String, Throwable> pair = this.parseMessage(message, true, args);
		if (pair.second() != null) {
			log.debug(pair.first(), pair.second());
		} else {
			log.debug(pair.first());
		}
		
	}

}
