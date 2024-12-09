package io.github.ihepda.techdebt.filter;

public abstract class LogicOperation implements FilterOperation{

	private FilterOperation left;
	private FilterOperation right;
	
	
	protected abstract String operation();
	
	protected void addOperation(FilterOperation operation) {
		if(left == null) {
			left = operation;
		} else if (right == null) {
			right = operation;
		} else {
			throw new IllegalStateException("Operation already set");
		}
	}
	
	public FilterOperation getLeft() {
		return left;
	}

	public FilterOperation getRight() {
		return right;
	}
	
	@Override
	public void dump(StringBuilder sb) {
		sb.append("(");
		left.dump(sb);
		sb.append(" ").append(operation()).append(" ");
		right.dump(sb);
		sb.append(")");
	}
	
	
}
