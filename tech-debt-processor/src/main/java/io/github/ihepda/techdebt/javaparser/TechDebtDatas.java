package io.github.ihepda.techdebt.javaparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.ihepda.techdebt.TechDebtElement;

public record TechDebtDatas(List<TechDebtElement> elements, java.util.Map<String, CommentInfo> refComments ) {
	public static TechDebtDatas newInstance() {
		return new TechDebtDatas(new ArrayList<>(), new HashMap<>());
	}
}
