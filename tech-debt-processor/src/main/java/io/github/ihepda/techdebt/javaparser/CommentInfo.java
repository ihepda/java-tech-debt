package io.github.ihepda.techdebt.javaparser;

import java.util.concurrent.atomic.AtomicInteger;

public record CommentInfo(String comment, int startline, AtomicInteger endLine) {
	public static CommentInfo of(String comment, int startline) {
		return new CommentInfo(comment, startline, new AtomicInteger(-1));
	}
}
