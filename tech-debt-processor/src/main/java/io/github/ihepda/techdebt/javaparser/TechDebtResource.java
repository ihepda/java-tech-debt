package io.github.ihepda.techdebt.javaparser;

import java.nio.file.Path;
import java.util.List;

import io.github.ihepda.techdebt.TechDebtElement;

public record TechDebtResource(String resourceName, Path path, List<TechDebtElement> elements) {

}
