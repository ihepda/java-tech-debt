package io.github.ihepda.techdebt.utils;

public record Pair<T,U>(T first, U second) {
	public static <K, U> Pair<K, U> of(K first) {
		return new Pair<>(first, null);
	}
}
